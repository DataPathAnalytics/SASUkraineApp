package com.datapath.sasu.integration.prozorro.monitoring;

import com.datapath.sasu.dao.DAO;
import com.datapath.sasu.dao.entity.Monitoring;
import com.datapath.sasu.integration.prozorro.monitoring.containers.MonitoringAPI;
import com.datapath.sasu.integration.prozorro.monitoring.containers.MonitoringAPIResponse;
import com.datapath.sasu.integration.prozorro.monitoring.containers.MonitoringsAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.datapath.sasu.Constants.MONITORING_START;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class MonitoringLoader {

    private static final String OFFSET = "offset";
    private final DAO dao;
    private final MonitoringHandler handler;
    private RestTemplate restTemplate;

    @Value("${prozorro.monitoring.url}")
    private String baseUrl;

    public MonitoringLoader(DAO dao, MonitoringHandler handler) {
        this.dao = dao;
        this.handler = handler;
    }

    public void load(LocalDateTime offset) {
        restTemplate = new RestTemplate();

        String url = new DefaultUriBuilderFactory(baseUrl).builder()
                .queryParam(OFFSET, offset)
                .build().toString();

        List<MonitoringAPI> monitorings;

        do {
            log.info("Load monitoring from [{}]", url);
            MonitoringsAPIResponse response = restTemplate.getForObject(url, MonitoringsAPIResponse.class);

            if (response == null || isEmpty(response.getData())) break;

            monitorings = response.getData();
            monitorings.forEach(tender -> load(tender.getId()));

            url = response.getNextPage().getUri();
        }
        while (monitorings.size() > 5);
        log.info("Finished loading monitoring");
    }

    public void loadLastModified() {
        load(getLastModifiedTenderDate());
    }

    private void load(String id) {
        log.info("Saving monitoring {}", id);
        MonitoringAPIResponse response = restTemplate.getForObject(baseUrl + "/" + id, MonitoringAPIResponse.class);
        MonitoringAPI monitoringAPI = response.getData();
        handler.handle(monitoringAPI);
    }

    private LocalDateTime getLastModifiedTenderDate() {
        Optional<Monitoring> lastModifiedMonitoring = dao.getLastModifiedMonitoring();
        if (lastModifiedMonitoring.isPresent()) {
            return lastModifiedMonitoring.get().getDateModified();
        }
        return MONITORING_START.toLocalDateTime();
    }

}
