package com.datapath.sasu.integration.nbu;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExchangeRateAPI {
    private double rate;
    private String cc;
    @JsonProperty("exchangedate")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate exchangeDate;
}
