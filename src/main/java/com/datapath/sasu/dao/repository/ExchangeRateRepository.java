package com.datapath.sasu.dao.repository;


import com.datapath.sasu.dao.entity.ExchangeRate;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRateRepository extends CrudRepository<ExchangeRate, Integer> {

    Optional<ExchangeRate> findByCurrencyAndExchangeDate(String currency, LocalDate exchangeDate);

}
