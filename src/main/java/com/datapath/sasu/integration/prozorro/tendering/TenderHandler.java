package com.datapath.sasu.integration.prozorro.tendering;

import com.datapath.sasu.dao.service.TenderDAOService;
import com.datapath.sasu.integration.Converter;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPI;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static com.datapath.sasu.Constants.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class TenderHandler {

    @Autowired
    private Converter converter;
    @Autowired
    private TenderDAOService daoService;
    @Autowired
    private TenderingAPIManager tenderingApiManager;

    @Transactional
    public void handle(String tenderId) {
        TenderAPIResponse response = tenderingApiManager.getTender(tenderId);
        var tenderAPI = response.getData();
        if (isProcessable(tenderAPI)) {
            log.info("Handling tender with hash - {}, date modified - {}", tenderAPI.getId(), tenderAPI.getDateModified());
            var tenderEntity = converter.convert(tenderAPI);
            daoService.save(tenderEntity);
        } else {
            log.debug("Tender {} skipped", tenderAPI.getId());
        }
    }

    public void handleBindToMonitoring(String tenderId) {
        TenderAPIResponse response = tenderingApiManager.getTender(tenderId);
        var tenderAPI = response.getData();
        if (isProcessableBindToMonitoring(tenderAPI)) {
            log.info("Handling tender with hash - {}, date modified - {}", tenderAPI.getId(), tenderAPI.getDateModified());
            var tenderEntity = converter.convert(tenderAPI);
            daoService.save(tenderEntity);
        } else {
            log.debug("Tender {} skipped", tenderAPI.getId());
        }
    }

    private boolean isProcessableBindToMonitoring(TenderAPI tender) {
        if (tender.getDate() == null) return false;

        if (!OPEN_METHOD_TYPES.contains(tender.getProcurementMethodType())
                && !LIMITED_METHOD_TYPES.contains(tender.getProcurementMethodType())) return false;

        if (OPEN_METHOD_TYPES.contains(tender.getProcurementMethodType())) {
            if (tender.getTenderPeriod().getStartDate().toLocalDate().isAfter(LocalDate.now())) {
                return false;
            }
        }

        if (LIMITED_METHOD_TYPES.contains(tender.getProcurementMethodType())) {
            if (isEmpty(tender.getAwards())) {
                return false;
            }
        }
        return true;
    }

    private boolean isProcessable(TenderAPI tender) {
        if (tender.getDate() == null) return false;
        if (tender.getDate().isBefore(MONITORING_START)) return false;

        if (!OPEN_METHOD_TYPES.contains(tender.getProcurementMethodType())
                && !LIMITED_METHOD_TYPES.contains(tender.getProcurementMethodType())) return false;

        if (OPEN_METHOD_TYPES.contains(tender.getProcurementMethodType())) {
            if (tender.getTenderPeriod().getStartDate().toLocalDate().isAfter(LocalDate.now())) {
                return false;
            }
        }

        if (LIMITED_METHOD_TYPES.contains(tender.getProcurementMethodType())) {
            if (isEmpty(tender.getAwards())) {
                return false;
            }
        }
        return true;
    }

}
