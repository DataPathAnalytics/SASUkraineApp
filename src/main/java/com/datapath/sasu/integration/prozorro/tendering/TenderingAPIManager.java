package com.datapath.sasu.integration.prozorro.tendering;


import com.datapath.sasu.CookieInterceptor;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPIResponse;
import com.datapath.sasu.integration.prozorro.tendering.containers.TendersAPIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Component
public class TenderingAPIManager {

    @Value("${prozorro.tendering.url}")
    private String apiUrl;

    private RestTemplate restTemplate;

    public TenderingAPIManager() {
        restTemplate = new RestTemplateBuilder()
                .setReadTimeout(Duration.ofMinutes(3))
                .setConnectTimeout(Duration.ofMinutes(3))
                .additionalInterceptors(new CookieInterceptor())
                .build();
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(3000))
    public TendersAPIResponse getTenders(String url) {
        return restTemplate.getForObject(url, TendersAPIResponse.class);
    }

    @Retryable(maxAttempts = 5, backoff = @Backoff(3000))
    public TenderAPIResponse getTender(String tenderId) {
        return restTemplate.getForObject(apiUrl + "/" + tenderId, TenderAPIResponse.class);
    }

}
