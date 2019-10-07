package com.currency.converter.controller;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.currency.converter.dto.currency.CurrencyApiResponse;
import com.currency.converter.errorhandling.ProcessingException;
import com.currency.converter.service.CurrencyService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class CurrencyConverterController implements CurrencyControllerInterface{

    private final CurrencyService currencyService;

    public CurrencyConverterController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(value = "/currency/convert",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Mono<CurrencyApiResponse> convertAmountToOtherCurrency(@RequestParam(name = "amount") @Size(min=1, max=30)BigDecimal amount,
                                                           @RequestParam(name = "source-currency") String sourceCurrency,
                                                           @RequestParam(name = "target-currency") String targetCurrency) throws ProcessingException {
    	
    	if(amount.doubleValue()<=0) {
    		throw new ProcessingException(null, "Amount should be greater than zero",HttpStatus.BAD_REQUEST);
    	}
        return currencyService.convertAmount(amount, sourceCurrency, targetCurrency);
    }
}