package com.datapath.sasu.integration.prozorro.tendering;

import com.datapath.sasu.dao.entity.Tender;
import com.datapath.sasu.dao.service.TenderDAOService;
import com.datapath.sasu.integration.Converter;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPI;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.datapath.sasu.Constants.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class AsyncTenderHandler {

    public static final List<String> FILTER_METHOD_TYPES = List.of(ABOVE_THRESHOLD_UA, ABOVE_THRESHOLD_EU, NEGOTIATION, NEGOTIATION_QUICK);

    @Autowired
    private Converter converter;
    @Autowired
    private TenderDAOService daoService;
    @Autowired
    private TenderingAPIManager tenderingApiManager;

    @Transactional
    @Async("applicationTaskExecutor")
    public void handle(String tenderId) {
        TenderAPIResponse response = tenderingApiManager.getTender(tenderId);
        TenderAPI tenderAPI = response.getData();
        if (isProcessable(tenderAPI)) {
            log.info("Handling tender with hash - {}, date modified - {}", tenderAPI.getId(), tenderAPI.getDateModified());
            Tender tenderEntity = converter.convert(tenderAPI);
            daoService.save(tenderEntity);
        } else {
            log.debug("Tender {} skipped", tenderAPI.getId());
        }
    }

    private boolean isProcessable(TenderAPI tender) {
        if (tender.getDate() == null) return false;
        if (tender.getDate().isBefore(MONITORING_START)) return false;

        if (!FILTER_METHOD_TYPES.contains(tender.getProcurementMethodType())) return false;

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
