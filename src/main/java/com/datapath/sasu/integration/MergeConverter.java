package com.datapath.sasu.integration;

import com.datapath.sasu.dao.DAO;
import com.datapath.sasu.dao.entity.*;
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
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
public class MergeConverter implements Converter {

    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
    public static final String SAS_ROLE = "sas";

    @Autowired
    private DAO dao;
    @Autowired
    private VariableProcessor variableProcessor;

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

                var office = dao.getOffice(auditorAPI.getName()).orElse(new Office());
                office.setName(auditorAPI.getName());

                String regionAPI = auditorAPI.getAddress().getRegion();

                office.setRegion(dao.getRegion(regionAPI));
                monitoringEntity.setOffice(office);
            }
        }

        return monitoringEntity;
    }

}
