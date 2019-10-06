package com.currency.converter;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.currency.converter.config.EmbeddedRedisServer;
import com.currency.converter.model.CurrencyCacheObject;
import com.currency.converter.repo.CurrencyRepoRedisImpl;

import reactor.test.StepVerifier;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyRepoTests {

    @Autowired
    private CurrencyRepoRedisImpl currencyRepoRedisImpl;

    @Autowired
    private EmbeddedRedisServer embeddedRedisServer;

    @Test
    public void saveCurrencyCacheObject() {
        CurrencyCacheObject currencyCacheObject = new CurrencyCacheObject();
        currencyCacheObject.setId("USD_EUR");
        currencyCacheObject.setSourceCurrency("USD");
        currencyCacheObject.setTargetCurrency("EUR");
        currencyCacheObject.setSourceTargetValue(new BigDecimal(1.2));
        StepVerifier.create(currencyRepoRedisImpl.save(currencyCacheObject)).expectNext(currencyCacheObject).expectComplete();
    }

    @Test
    public void saveAndGetCurrencyCacheObject() {
        CurrencyCacheObject currencyCacheObject = new CurrencyCacheObject();
        currencyCacheObject.setId("USD_EUR");
        currencyCacheObject.setSourceCurrency("USD");
        currencyCacheObject.setTargetCurrency("EUR");
        currencyCacheObject.setSourceTargetValue(new BigDecimal(1.2));
        StepVerifier.create(currencyRepoRedisImpl.save(currencyCacheObject).
                flatMap(result -> currencyRepoRedisImpl.findBySourceCurrencyAndTargetCurrency(result.getSourceCurrency(),
                        result.getTargetCurrency()))).expectNext(currencyCacheObject).expectComplete();
    }

    @After
    public void after() {
        embeddedRedisServer.stopRedis();
    }
}