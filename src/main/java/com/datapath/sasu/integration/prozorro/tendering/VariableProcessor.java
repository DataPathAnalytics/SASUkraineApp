package com.datapath.sasu.integration.prozorro.tendering;

import com.datapath.sasu.dao.entity.CpvCatalogue;
import com.datapath.sasu.dao.entity.Tender;
import com.datapath.sasu.integration.prozorro.tendering.containers.AwardAPI;
import com.datapath.sasu.integration.prozorro.tendering.containers.TenderAPI;

import java.time.LocalDate;

public interface VariableProcessor {

    Double getTenderExpectedValue(TenderAPI tender);

    Double getTenderValue(Tender tender);

    Double getAwardValue(AwardAPI award, LocalDate exchangeRateDate);

    CpvCatalogue getCpv(String code);

}
