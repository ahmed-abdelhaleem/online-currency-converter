package com.currency.converter.service;

import java.math.BigDecimal;

import com.currency.converter.dto.currency.CurrencyApiResponse;

import reactor.core.publisher.Mono;

public interface CurrencyService {

    Mono<CurrencyApiResponse> convertAmount(BigDecimal amount, String source, String target);
}
