package com.currency.converter.controller;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.RequestParam;

import com.currency.converter.dto.currency.CurrencyApiResponse;
import com.currency.converter.errorhandling.ErrorResponse;
import com.currency.converter.errorhandling.ProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import reactor.core.publisher.Mono;

@Api(value = "Currency converter API")
public interface CurrencyControllerInterface {

    @ApiOperation(value = "Convert amount from currency to another",
            response = CurrencyApiResponse.class,
            responseHeaders = {
                    @ResponseHeader(name = "Content-Type", description = "always application/json", response = String.class),
            })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response", responseHeaders = {
                    @ResponseHeader(name = "Content-Type", description = "always application/json", response = CurrencyApiResponse.class),
            }, response = CurrencyApiResponse.class)
            , @ApiResponse(code = 400, message = "Bad Request", responseHeaders = {
            @ResponseHeader(name = "Content-Type", description = "always application/json", response = ErrorResponse.class),
    }, response = ErrorResponse.class)
    })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "amount", value = "the amount in source currency to be converted", required = true, dataType="double", type = "param"),
            @ApiImplicitParam(name = "source-currency", value = "Currency of amount", required = true, type = "param"),
            @ApiImplicitParam(name = "target-currency", value = "Target Currency of response ", required = true, type = "param")
    })
    Mono<CurrencyApiResponse> convertAmountToOtherCurrency(@RequestParam(name = "amount") BigDecimal amount,
                                                           @RequestParam(name = "source-currency") String sourceCurrency,
                                                           @RequestParam(name = "target-currency") String targetCurrency) throws ProcessingException;
}