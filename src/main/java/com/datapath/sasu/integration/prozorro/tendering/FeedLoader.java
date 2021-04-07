package com.datapath.sasu.integration.prozorro.tendering;

import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPI;
import com.datapath.sasu.integration.prozorro.tendering.containers.TendersAPIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.datapath.sasu.Constants.MONITORING_START;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class FeedLoader {

    @Value("${prozorro.tendering.url}")
    private String apiUrl;
    @Autowired
    private AsyncTenderHandler tenderHandler;

    @Autowired
    private TenderingAPIManager tenderingApiManager;

    public void load() {
        String url = apiUrl + "?feed=changes&descending=1";

        List<TenderAPI> tenders;

        do {
            log.info("Load tenders from [{}]", url);

            TendersAPIResponse response = tenderingApiManager.getTenders(url);

            if (response == null || isEmpty(response.getData())) break;
            tenders = response.getData();

            for (TenderAPI tender : tenders) {
                if (tender.getDateModified().isAfter(MONITORING_START)) {
                    try {
                        tenderHandler.handle(tender.getId());
                    } catch (Exception ex) {
                        log.error("Error while processing tender.", ex);
                    }
                }
            }

            String nextPageUrl = response.getNextPage().getUri();
            if (url.equalsIgnoreCase(nextPageUrl)) {
                break;
            } else {
                url = nextPageUrl;
            }

            // Check tenders size to avoid cycling from Prozorro. There were cases when same tenders repeated
        } while (tenders.size() > 5);
    }

}
