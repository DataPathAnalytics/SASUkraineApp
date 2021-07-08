package com.datapath.sasu;

import com.datapath.sasu.integration.prozorro.monitoring.MonitoringDescendingLoader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.TimeZone;

import static com.datapath.sasu.Constants.UA_ZONE;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableCaching
@EnableRetry
public class SasuApplication {

    public static void main(String[] args) {
        var application = new SpringApplication(SasuApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMinutes(3))
                .setReadTimeout(Duration.ofMinutes(3))
                .build();
    }


    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(UA_ZONE));
    }

}
