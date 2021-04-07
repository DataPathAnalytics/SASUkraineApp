package com.datapath.sasu.integration.nbu;

import com.datapath.sasu.dao.DAO;
import com.datapath.sasu.dao.entity.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@Slf4j
public class NBULoaderImpl implements NBULoader {

    private static final String JSON = "json";
    private static final String VAL_CODE = "valcode";
    private static final String DATE = "date";
    private static final DateTimeFormatter REQUEST_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Autowired
    private DAO dao;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${nbu.url}")
    private String apiUrl;

    @Override
    public double getRate(String currency, LocalDate date) {

        Optional<ExchangeRate> exchangeRateDAO = dao.getExchangeRate(currency, date);
        if (exchangeRateDAO.isPresent()) {
            return exchangeRateDAO.get().getRate();
        } else {
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam(JSON)
                    .queryParam(VAL_CODE, currency)
                    .queryParam(DATE, date.format(REQUEST_DATE_FORMAT)).toUriString();

            ExchangeRateAPI[] response = restTemplate.getForObject(url, ExchangeRateAPI[].class);
            ExchangeRate daoRate = convert(response[0]);
            return dao.save(daoRate).getRate();
        }
    }

    private ExchangeRate convert(ExchangeRateAPI apiRate) {
        ExchangeRate daoRate = new ExchangeRate();
        daoRate.setCurrency(apiRate.getCc());
        daoRate.setExchangeDate(apiRate.getExchangeDate());
        daoRate.setRate(apiRate.getRate());
        return daoRate;
    }

}
