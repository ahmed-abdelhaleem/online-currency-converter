package com.currency.converter.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.currency.converter.dto.Constants;
import com.currency.converter.dto.currency.CurrencyApiResponse;
import com.currency.converter.errorhandling.ProcessingException;
import com.currency.converter.model.CurrencyCacheObject;
import com.currency.converter.repo.CurrencyRepo;
import com.currency.converter.restclient.CurrencyRestClient;

import reactor.core.publisher.Mono;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRestClient currencyRestClient;
    private final CurrencyRepo currencyRepo;

    @Value("${currency.cache.timeToLive.inSeconds}")
    private long currencyTTL;

    public CurrencyServiceImpl(CurrencyRestClient currencyRestClient, CurrencyRepo currencyRepo) {
        this.currencyRestClient = currencyRestClient;
        this.currencyRepo = currencyRepo;
    }

    @Override
    public Mono<CurrencyApiResponse> convertAmount(BigDecimal amount, String source, String target) {

        return retrieveCurrencyMultiplyValueFromCache(source, target).map(cachedCurrObj -> {
            if (source.equalsIgnoreCase(cachedCurrObj.getSourceCurrency())) {
                return cachedCurrObj.getSourceTargetValue();
            } else {
                return new BigDecimal(1).divide(cachedCurrObj.getSourceTargetValue(), 6, BigDecimal.ROUND_HALF_EVEN);
            }
        }).flatMap(value -> getCurrencyApiMonoResponse(amount, value)).switchIfEmpty(
                currencyRestClient.getResponse(source, target).map(mapObject -> {
                    String normalExtractionKey = source + "_" + target;
                    return mapObject.get(normalExtractionKey);
                }).flatMap(val ->
                        saveCurrencyCacheObject(source, target, val.toString())
                                .map(cachedCurrObj -> cachedCurrObj.getSourceTargetValue())
                                .flatMap(value -> getCurrencyApiMonoResponse(amount, value))
                )).onErrorResume(ex -> Mono.error(new ProcessingException(null, Constants.UNDEFINED_CURR.getValue(), HttpStatus.BAD_REQUEST)));
    }

    private Mono<CurrencyCacheObject> saveCurrencyCacheObject(String source, String target, String value) {
        CurrencyCacheObject currencyCacheObject = new CurrencyCacheObject();
        currencyCacheObject.setId(source + "_" + target);
        currencyCacheObject.setSourceCurrency(source);
        currencyCacheObject.setTargetCurrency(target);
        currencyCacheObject.setSourceTargetValue(new BigDecimal(value));
        return currencyRepo.save(currencyCacheObject);
    }

    private Mono<CurrencyCacheObject> retrieveCurrencyMultiplyValueFromCache(String source, String target) {

        return currencyRepo.findBySourceCurrencyAndTargetCurrency(source, target).
                switchIfEmpty(currencyRepo.findBySourceCurrencyAndTargetCurrency(target, source));
    }

    private BigDecimal calculateGivenAmountInTargetCurrency(BigDecimal amount,BigDecimal multiplyValue){
        return amount.multiply(multiplyValue).setScale(4, BigDecimal.ROUND_HALF_EVEN).stripTrailingZeros();
    }
    private Mono<CurrencyApiResponse> getCurrencyApiMonoResponse(BigDecimal amount, BigDecimal multiplyValue) {
        return Mono.just(new CurrencyApiResponse(calculateGivenAmountInTargetCurrency(amount,multiplyValue)));
    }
}