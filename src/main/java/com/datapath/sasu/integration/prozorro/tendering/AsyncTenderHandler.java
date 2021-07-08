package com.datapath.sasu.integration.prozorro.tendering;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AsyncTenderHandler {

    @Autowired
    private TenderHandler tenderHandler;

    @Async("applicationTaskExecutor")
    public void handle(String tenderId) {
        try {
            tenderHandler.handle(tenderId);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

}
