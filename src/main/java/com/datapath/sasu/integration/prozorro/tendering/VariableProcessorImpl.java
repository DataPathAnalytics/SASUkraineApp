package com.datapath.sasu.integration.prozorro.tendering;

import com.datapath.sasu.dao.DAO;
import com.datapath.sasu.dao.entity.Award;
import com.datapath.sasu.dao.entity.CpvCatalogue;
import com.datapath.sasu.dao.entity.Tender;
import com.datapath.sasu.integration.nbu.NBULoader;
import com.datapath.sasu.integration.prozorro.tendering.containers.AwardAPI;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPI;
import com.datapath.sasu.integration.prozorro.tendering.containers.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

import static com.datapath.sasu.Constants.OPEN_METHOD_TYPES;

@Component
@Slf4j
public class VariableProcessorImpl implements VariableProcessor {

    private static final String UAH = "UAH";
    public static final String COMPLETE = "complete";
    @Autowired
    private NBULoader nbuLoader;
    @Autowired
    private DAO dao;

    private List<CpvCatalogue> cpvs;

    @PostConstruct
    public void init() {
        cpvs = dao.getCpvCatalogue();
    }

    @Override
    public Double getTenderExpectedValue(TenderAPI tender) {

        LocalDate exchangeRateDate = OPEN_METHOD_TYPES.contains(tender.getProcurementMethodType())
                ? tender.getTenderPeriod().getStartDate().toLocalDate()
                : tender.getDate().toLocalDate();

        Value value = tender.getValue();

        String currency = value.getCurrency();
        Double amount = value.getAmount();
        if (currency.equalsIgnoreCase(UAH)) {
            return amount;
        } else {
            double rate = nbuLoader.getRate(currency, exchangeRateDate);
            return amount * rate;
        }
    }

    @Override
    public Double getTenderValue(Tender tender) {
        if (tender.getStatus().equalsIgnoreCase(COMPLETE)) {
            return tender.getAwards().stream().mapToDouble(Award::getValue).sum();
        } else {
            return tender.getExpectedValue();
        }
    }

    @Override
    public Double getAwardValue(AwardAPI award, LocalDate exchangeRateDate) {
        String currency = award.getValue().getCurrency();
        Double amount = award.getValue().getAmount();

        if (currency.equalsIgnoreCase(UAH)) return amount;

        double rate = nbuLoader.getRate(currency, exchangeRateDate);
        return amount * rate;
    }

    @Override
    public CpvCatalogue getCpv(String code) {
        return cpvs.stream()
                .filter(cpv -> cpv.getCpvCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Code not found " + code));
    }
}
