package com.currency.converter.repo;

import com.currency.converter.model.CurrencyCacheObject;

import reactor.core.publisher.Mono;

public interface CurrencyRepo {

    Mono<CurrencyCacheObject> save(CurrencyCacheObject currencyCacheObject);

    Mono<CurrencyCacheObject> findBySourceCurrencyAndTargetCurrency(String sourceCurrency,String targetCurrency);

    }
