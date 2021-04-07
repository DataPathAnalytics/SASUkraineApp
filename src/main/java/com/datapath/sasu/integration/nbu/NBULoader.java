package com.datapath.sasu.integration.nbu;

import java.time.LocalDate;

public interface NBULoader {

    double getRate(String currency, LocalDate date);

}
