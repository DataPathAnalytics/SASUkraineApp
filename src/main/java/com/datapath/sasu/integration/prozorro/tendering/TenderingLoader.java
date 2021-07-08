package com.datapath.sasu.integration.prozorro.tendering;

import com.datapath.sasu.CookieInterceptor;
import com.datapath.sasu.dao.entity.Tender;
import com.datapath.sasu.dao.service.TenderDAOService;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPI;
import com.datapath.sasu.integration.prozorro.tendering.containers.TendersAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TenderingLoader {

    private static final String LIMIT = "limit";
    private static final String OFFSET = "offset";
    private static final int TENDERS_LIMIT = 100;

    @Value("${prozorro.tendering.url}")
    private String apiUrl;
    @Autowired
    private TenderDAOService daoService;
    @Autowired
    private TenderHandler tenderHandler;
    private RestTemplate restTemplate;

    private void refreshRestTemplate() {
        restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new CookieInterceptor());
    }

    public void load() {
        refreshRestTemplate();

        LocalDateTime offsetDate = getOffset();

        String url = new DefaultUriBuilderFactory(apiUrl).builder()
                .queryParam(LIMIT, TENDERS_LIMIT)
                .queryParam(OFFSET, offsetDate)
                .build().toString();

        List<TenderAPI> tenders;

        do {
            log.info("Load tenders from [{}]", url);

            TendersAPIResponse response = restTemplate.getForObject(url, TendersAPIResponse.class);

            if (response == null || isEmpty(response.getData())) break;

            tenders = response.getData();
            url = response.getNextPage().getUri();

            tenders.forEach(tender -> saveTender(tender.getId()));


            // Check tenders size to avoid cycling from Prozorro. There were cases when same tenders repeated
        } while (tenders.size() > 5);
        log.info("Finished tendering loading");
    }

    private void saveTender(String id) {
        tenderHandler.handle(id);
    }

    private LocalDateTime getOffset() {
        return LocalDateTime.now().minusDays(1);
    }

    private LocalDateTime getLastModifiedTenderDate() {
        LocalDateTime offsetDate = MONITORING_START.toLocalDateTime();

        Optional<Tender> lastTender = daoService.getLastModifiedTender();
        if (lastTender.isPresent() && lastTender.get().getDateModified() != null) {
            offsetDate = lastTender.get().getDateModified();
        }
        return offsetDate;
    }

}
