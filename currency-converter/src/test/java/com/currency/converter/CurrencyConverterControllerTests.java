package com.currency.converter;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.currency.converter.config.EmbeddedRedisServer;
import com.currency.converter.dto.currency.CurrencyApiResponse;
import com.currency.converter.service.CurrencyService;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CurrencyConverterControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EmbeddedRedisServer embeddedRedisServer;

    @MockBean
    private CurrencyService currencyService;

    @Test
    public void givenMonoCurrencyResponse_whenConvertCurrency_thenGetNewAmount()
            throws Exception {
        int amount = 11;
        CurrencyApiResponse currencyApiResponse = new CurrencyApiResponse(new BigDecimal(amount));
        given(currencyService.convertAmount(any(BigDecimal.class), anyString(), anyString())).willReturn(Mono.just(currencyApiResponse));

        MvcResult mvcResult = mvc.perform(get("/api/currency/convert").param("amount", "10")
                .param("source-currency", "EUR").param("target-currency", "USD"))
                .andExpect(status().isOk()).andReturn();
        CurrencyApiResponse response = (CurrencyApiResponse) mvcResult.getAsyncResult();
        assertTrue(response.getAmount().intValue() == amount);
    }

    @Test
    public void givenMissingSource_whenConvertCurrency_thenExpectBadRequest()
            throws Exception {
        mvc.perform(get("/api/currency/convert").param("amount", "10")
                .param("target-currency", "USD"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenMissingTarget_whenConvertCurrency_thenExpectBadRequest()
            throws Exception {
        mvc.perform(get("/api/currency/convert").param("amount", "10")
                .param("source-currency", "EUR"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidAmount_whenConvertCurrency_thenExpectBadRequest()
            throws Exception {
        mvc.perform(get("/api/currency/convert").param("amount", "a")
                .param("source-currency", "EUR").param("target-currency", "USD"))
                .andExpect(status().isBadRequest());
    }

    @After
    public void after() {
        embeddedRedisServer.stopRedis();
    }
}