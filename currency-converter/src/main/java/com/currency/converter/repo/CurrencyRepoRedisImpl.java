package com.currency.converter.repo;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import com.currency.converter.model.CurrencyCacheObject;

import reactor.core.publisher.Mono;

@Repository
public class CurrencyRepoRedisImpl implements CurrencyRepo {

    private final ReactiveRedisOperations<String, CurrencyCacheObject> reactiveRedisOperations;

    @Value("${currency.cache.timeToLive.inSeconds}")
    private long currencyTTL;

    public CurrencyRepoRedisImpl(ReactiveRedisOperations<String, CurrencyCacheObject> reactiveRedisOperations) {
        this.reactiveRedisOperations = reactiveRedisOperations;
    }

    @Override
    public Mono<CurrencyCacheObject> save(CurrencyCacheObject currencyCacheObject) {
        return reactiveRedisOperations.opsForValue().set(currencyCacheObject.getId(), currencyCacheObject,
                Duration.ofSeconds(currencyTTL)).map(x -> currencyCacheObject);
    }

    @Override
    public Mono<CurrencyCacheObject> findBySourceCurrencyAndTargetCurrency(String sourceCurrency, String targetCurrency) {
        String key = sourceCurrency + "_" + targetCurrency;
        return reactiveRedisOperations.opsForValue().get(key);
    }
}