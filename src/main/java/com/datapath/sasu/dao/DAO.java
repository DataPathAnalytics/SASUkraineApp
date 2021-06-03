package com.datapath.sasu.dao;

import com.datapath.sasu.dao.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DAO {

    Monitoring save(Monitoring monitoring);

    ExchangeRate save(ExchangeRate rate);

    ProcurementCategory getProcurementCategory(String name);

    Optional<ExchangeRate> getExchangeRate(String currency, LocalDate date);

    List<CpvCatalogue> getCpvCatalogue();

    Optional<Monitoring> getLastModifiedMonitoring();

    Optional<ProcuringEntity> getProcuringEntity(String outerId);

    Optional<Tender> getTender(String outerId);

    Optional<Monitoring> getMonitoring(String outerId);

    Optional<Violation> getViolation(String violationName);

    Optional<Reason> getReason(String reasonName);

    Region getRegion(String alias);

    Optional<Auditor> getAuditor(String email);

    Optional<Office> getOffice(Integer regionId);

    List<Integer> getTendersWithDuplicatedMonitoring();

    List<Monitoring> getTenderMonitoring(Integer tenderId);

    void delete(Monitoring monitoring);
}
