package com.datapath.sasu.integration.nbu;

import lombok.Data;

import java.util.List;

@Data
public class NbuResponse {
    private List<ExchangeRateAPI> rates;
}
