package com.datapath.sasu.integration;

import com.datapath.sasu.dao.DAO;
import com.datapath.sasu.dao.entity.*;
import com.datapath.sasu.integration.prozorro.calendar.CalendarHandler;
import com.datapath.sasu.integration.prozorro.monitoring.containers.ConclusionAPI;
import com.datapath.sasu.integration.prozorro.monitoring.containers.MonitoringAPI;
import com.datapath.sasu.integration.prozorro.monitoring.containers.Party;
import com.datapath.sasu.integration.prozorro.tendering.VariableProcessor;
import com.datapath.sasu.integration.prozorro.tendering.containers.ProcuringEntityAPI;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static com.datapath.sasu.Constants.OPEN_METHOD_TYPES;
import static com.datapath.sasu.Constants.UA_ZONE;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class MergeConverter implements Converter {

    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
    public static final String SAS_ROLE = "sas";
    public static final String CANCELLED = "cancelled";
    public static final String STOPPED = "stopped";
    public static final String OFFICE_NAME_PREFIX = "Територіальний підрозділ у ";
    public static final int MAX_MONITORING_DURATION = 15;
    public static final String COMPLETE = "complete";

    @Autowired
    private DAO dao;
    @Autowired
    private VariableProcessor variableProcessor;
    @Autowired
    private CalendarHandler calendarHandler;

    @Override
    public Tender convert(TenderAPI tenderAPI) {

        var daoTender = dao.getTender(tenderAPI.getId()).orElse(new Tender());
        daoTender.setHash(tenderAPI.getId());
        daoTender.setOuterId(tenderAPI.getTenderID());
        daoTender.setDateModified(toLocalDateTime(tenderAPI.getDateModified()));
        daoTender.setDate(toLocalDateTime(tenderAPI.getDate()));
        daoTender.setStatus(tenderAPI.getStatus());
        daoTender.setLocalMethod(tenderAPI.getProcurementMethodType());
        daoTender.setStartDate(LocalDate.parse(tenderAPI.getTenderID().substring(3, 13)));
        daoTender.setCompetitive(OPEN_METHOD_TYPES.contains(tenderAPI.getProcurementMethodType()));

        Double tenderExpectedValue = variableProcessor.getTenderExpectedValue(tenderAPI);
        daoTender.setExpectedValue(tenderExpectedValue);

        var procurementCategory = dao.getProcurementCategory(tenderAPI.getMainProcurementCategory());
        daoTender.setProcurementCategory(procurementCategory);

        var procuringEntity = getProcuringEntity(tenderAPI.getProcuringEntity());
        daoTender.setProcuringEntity(procuringEntity);

        mergeAwards(tenderAPI, daoTender);
        mergeItems(tenderAPI, daoTender);

        Double tenderValue = variableProcessor.getTenderValue(daoTender);
        daoTender.setValue(tenderValue);

        return daoTender;
    }

    private ProcuringEntity getProcuringEntity(ProcuringEntityAPI procuringEntityAPI) {
        String outerId = procuringEntityAPI.getIdentifier().getScheme() + procuringEntityAPI.getIdentifier().getId();
        var region = dao.getRegion(procuringEntityAPI.getAddress().getRegion());

        var procuringEntity = dao.getProcuringEntity(outerId).orElse(new ProcuringEntity());
        procuringEntity.setOuterId(outerId);
        procuringEntity.setName(procuringEntityAPI.getIdentifier().getLegalName());
        procuringEntity.setRegion(region);

        return procuringEntity;
    }

    private void mergeAwards(TenderAPI tenderAPI, Tender tenderEntity) {
        if (isEmpty(tenderAPI.getAwards())) return;

        if (!tenderAPI.getStatus().equalsIgnoreCase(COMPLETE)) {
            tenderEntity.clearAwards();
            return;
        }

        tenderAPI.getAwards().forEach(apiAward -> {
            var award = tenderEntity.getAwards().stream()
                    .filter(awardEntity -> awardEntity.getOuterId().equals(apiAward.getId()))
                    .findFirst().orElse(new Award());

            award.setOuterId(apiAward.getId());

            ZonedDateTime exchangeRateDate = OPEN_METHOD_TYPES.contains(tenderAPI.getProcurementMethodType())
                    ? tenderAPI.getTenderPeriod().getStartDate()
                    : tenderAPI.getDate();

            Double value = variableProcessor.getAwardValue(apiAward, exchangeRateDate.toLocalDate());
            award.setValue(value);
            award.setStatus(apiAward.getStatus());

            if (award.getId() == null) {
                tenderEntity.addAward(award);
            }
        });
        tenderEntity.getAwards().removeIf(award -> !"active".equals(award.getStatus()));
        tenderEntity.getAwards().removeIf(award -> award.getValue() == null);
        tenderEntity.getAwards().removeIf(award -> award.getValue() == 0);
        tenderEntity.getAwards().removeIf(award -> award.getValue() > tenderEntity.getExpectedValue());
    }

    private void mergeItems(TenderAPI tenderAPI, Tender tenderEntity) {
        if (isEmpty(tenderAPI.getItems())) return;

        tenderAPI.getItems().forEach(apiItem -> {
            TenderItem item = tenderEntity.getItems().stream()
                    .filter(itemEntity -> itemEntity.getOuterId().equals(apiItem.getId()))
                    .findFirst().orElse(new TenderItem());

            CpvCatalogue cpv = variableProcessor.getCpv(apiItem.getClassification().getId());

            item.setOuterId(apiItem.getId());
            item.setCpv(cpv);

            if (item.getId() == null) {
                tenderEntity.addItem(item);
            }
        });
    }

    private LocalDateTime toLocalDateTime(ZonedDateTime value) {
        return value != null ? value.withZoneSameInstant(UA_ZONE).toLocalDateTime() : null;
    }

    private LocalDate toLocalDate(ZonedDateTime value) {
        return value != null ? value.withZoneSameInstant(UA_ZONE).toLocalDate() : null;
    }

    @Override
    public Monitoring convert(MonitoringAPI monitoringAPI) {

        var monitoringEntity = dao.getMonitoring(monitoringAPI.getId()).orElse(new Monitoring());
        monitoringEntity.setOuterId(monitoringAPI.getId());
        monitoringEntity.setDateModified(toLocalDateTime(monitoringAPI.getDateModified()));
        monitoringEntity.setResult(monitoringAPI.getStatus());
        monitoringEntity.setStartDate(toLocalDateTime(monitoringAPI.getMonitoringPeriod().getStartDate()));
        monitoringEntity.setStartMonth(monitoringAPI.getMonitoringPeriod().getStartDate().format(ofPattern(YEAR_MONTH_FORMAT)));


        monitoringEntity.setTender(dao.getTender(monitoringAPI.getTenderId()).orElseThrow(() -> new EntityNotFoundException("Monitoring tender not found")));

        ConclusionAPI conclusion = monitoringAPI.getConclusion();
        if (conclusion != null && !isEmpty(conclusion.getViolationType())) {
            List<Violation> violations = conclusion.getViolationType()
                    .stream()
                    .map(violationName -> dao.getViolation(violationName)
                            .orElse(new Violation(violationName)))
                    .collect(toList());

            monitoringEntity.getViolations().clear();
            monitoringEntity.getViolations().addAll(violations);
        }

        if (conclusion != null && conclusion.getDatePublished() != null) {
            monitoringEntity.setConcluded(true);

            LocalDateTime endDate = toLocalDateTime(conclusion.getDatePublished());
            monitoringEntity.setEndDate(endDate);
            monitoringEntity.setEndMonth(endDate.format(ofPattern(YEAR_MONTH_FORMAT)));
        }

        //fixme review and refactor/simplify

        int workDaysCount = calcDuration(monitoringAPI, monitoringEntity);
        monitoringEntity.setDuration(workDaysCount);

        if (!isEmpty(monitoringAPI.getReasons())) {
            List<Reason> reasons = monitoringAPI.getReasons().stream()
                    .map(reasonName -> dao.getReason(reasonName).orElse(new Reason(reasonName)))
                    .collect(toList());

            monitoringEntity.getReasons().clear();
            monitoringEntity.getReasons().addAll(reasons);
        }

        if (!isEmpty(monitoringAPI.getParties())) {

            Party auditorAPI = monitoringAPI.getParties().stream()
                    .filter(party -> party.getRoles() != null && party.getRoles().contains(SAS_ROLE))
                    .max(comparing(Party::getDatePublished)).orElse(null);

            if (auditorAPI != null) {
                var auditorEntity = dao.getAuditor(auditorAPI.getContactPoint().getEmail()).orElse(new Auditor());
                auditorEntity.setEmail(auditorAPI.getContactPoint().getEmail());

                monitoringEntity.setAuditor(auditorEntity);

                String regionAPI = auditorAPI.getAddress().getRegion();
                if (auditorAPI.getName().equals("ПІВНІЧНИЙ ОФІС ДЕРЖАУДИТСЛУЖБИ ( обл.)")) {
                    regionAPI = "Київська область";
                }

                Region region = dao.getRegion(regionAPI);

                var office = dao.getOffice(region.getId()).orElse(new Office());
                office.setName(OFFICE_NAME_PREFIX + region.getCaseName());
                office.setRegion(region);
                monitoringEntity.setOffice(office);
            }
        }

        return monitoringEntity;
    }

    private int calcDuration(MonitoringAPI monitoringAPI, Monitoring monitoringEntity) {
        int workDaysCount;
        if (monitoringEntity.isConcluded()) {
            workDaysCount = calendarHandler.getWorkDaysCount(
                    monitoringEntity.getStartDate().toLocalDate(),
                    toLocalDate(monitoringAPI.getConclusion().getDatePublished())
            );
        } else if (List.of(CANCELLED, STOPPED).contains(monitoringAPI.getStatus())) {
            workDaysCount = calendarHandler.getWorkDaysCount(
                    monitoringEntity.getStartDate().toLocalDate(),
                    monitoringEntity.getDateModified().toLocalDate()
            );
        } else {
            workDaysCount = calendarHandler.getWorkDaysCount(
                    monitoringEntity.getStartDate().toLocalDate(),
                    LocalDate.now()
            );
        }
        return Math.min(workDaysCount, MAX_MONITORING_DURATION);
    }

}
