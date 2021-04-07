package com.datapath.sasu.dao;

import com.datapath.sasu.dao.entity.*;
import com.datapath.sasu.dao.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DAOImpl implements DAO {

    private static final String KYIV_REGION = "м. Київ";

    private final ProcuringCategoryRepository procuringCategoryRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    private final CpvCatalogueRepository cpvCatalogueRepository;
    private final MonitoringRepository monitoringRepository;
    private final ProcuringEntityRepository procuringEntityRepository;
    private final TenderRepository tenderRepository;
    private final ViolationRepository violationRepository;
    private final RegionRepository regionRepository;
    private final AuditorRepository auditorRepository;
    private final OfficeRepository officeRepository;


    @Cacheable("categories")
    public ProcurementCategory getProcurementCategory(String name) {
        return procuringCategoryRepository.findByNameEn(name);
    }

    @Override
    public Optional<ExchangeRate> getExchangeRate(String currency, LocalDate date) {
        return exchangeRateRepository.findByCurrencyAndExchangeDate(currency, date);
    }

    @Override
    public ExchangeRate save(ExchangeRate rate) {
        return exchangeRateRepository.save(rate);
    }

    @Override
    public List<CpvCatalogue> getCpvCatalogue() {
        return cpvCatalogueRepository.findAll();
    }

    @Override
    public Monitoring save(Monitoring monitoring) {

        monitoringRepository.findByOuterId(monitoring.getOuterId())
                .ifPresent(exist -> monitoring.setId(exist.getId()));

        return monitoringRepository.save(monitoring);
    }

    @Override
    public Optional<Monitoring> getLastModifiedMonitoring() {
        return monitoringRepository.findFirstByOrderByDateModifiedDesc();
    }

    @Override
    public Optional<ProcuringEntity> getProcuringEntity(String outerId) {
        return procuringEntityRepository.findByOuterId(outerId);
    }

    @Override
    public Optional<Tender> getTender(String hash) {
        return tenderRepository.findByHash(hash);
    }

    @Override
    public Optional<Monitoring> getMonitoring(String outerId) {
        return monitoringRepository.findByOuterId(outerId);
    }

    @Override
    public Optional<Violation> getViolation(String violationName) {
        return violationRepository.findByName(violationName);
    }

    @Override
    public Region getRegion(String alias) {
        return regionRepository.findRegionByAlias(alias != null ? alias : KYIV_REGION);
    }

    @Override
    public Optional<Auditor> getAuditor(String email) {
        return auditorRepository.findByEmail(email);
    }

    @Override
    public Optional<Office> getOffice(String name) {
        return officeRepository.findByName(name);
    }

}
