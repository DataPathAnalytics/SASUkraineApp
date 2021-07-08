package com.datapath.sasu.integration.prozorro.monitoring;

import com.datapath.sasu.integration.prozorro.monitoring.containers.MonitoringAPI;
import com.datapath.sasu.integration.prozorro.monitoring.containers.MonitoringAPIResponse;
import com.datapath.sasu.integration.prozorro.monitoring.containers.MonitoringsAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;

import static com.datapath.sasu.Constants.MONITORING_START;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class MonitoringDescendingLoader {

    public static final String DESCENDING = "descending";
    private final MonitoringHandler handler;
    private RestTemplate restTemplate;

    @Value("${prozorro.monitoring.url}")
    private String baseUrl;

    public MonitoringDescendingLoader(MonitoringHandler handler) {
        this.handler = handler;
    }

    public void load() {
        restTemplate = new RestTemplate();

        var url = new DefaultUriBuilderFactory(baseUrl).builder()
                .queryParam(DESCENDING, 1)
                .build().toString();

        List<MonitoringAPI> monitorings;
        do {
            log.info("Load monitoring from [{}]", url);
            MonitoringsAPIResponse response = restTemplate.getForObject(url, MonitoringsAPIResponse.class);

            if (response == null || isEmpty(response.getData())) break;

            monitorings = response.getData();
            for (MonitoringAPI monitoring : monitorings) {
                if (monitoring.getDateModified().isBefore(MONITORING_START)) continue;

                load(monitoring.getId());
            }

            url = response.getNextPage().getUri();
        }
        while (monitorings.size() > 5);

        log.info("Finished loading monitoring");
    }

    private void load(String id) {
        log.info("Saving monitoring {}", id);
        MonitoringAPIResponse response = restTemplate.getForObject(baseUrl + "/" + id, MonitoringAPIResponse.class);
        var monitoringAPI = response.getData();
        handler.handle(monitoringAPI);
    }

}
