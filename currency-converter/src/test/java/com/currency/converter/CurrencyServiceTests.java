package com.currency.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.currency.converter.dto.Constants;
import com.currency.converter.dto.currency.CurrencyApiResponse;
import com.currency.converter.model.CurrencyCacheObject;
import com.currency.converter.repo.CurrencyRepoRedisImpl;
import com.currency.converter.restclient.CurrencyRestClient;
import com.currency.converter.service.CurrencyServiceImpl;

import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = CurrencyServiceImpl.class)
public class CurrencyServiceTests {

    @Mock
    private CurrencyRestClient currencyRestClient;

    @Mock
    private CurrencyRepoRedisImpl currencyRepo;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    @Test
    public void givenCurrencyObjectCached_thenExpectMonoCurrencyResponse() {
        CurrencyCacheObject currencyCacheObject = new CurrencyCacheObject("EUR_USD", "EUR", "USD", BigDecimal.valueOf(1.1));
        doReturn(Mono.just(currencyCacheObject))
                .when(currencyRepo).findBySourceCurrencyAndTargetCurrency(anyString(), anyString());
        doReturn(Mono.empty()).when(currencyRestClient).getResponse(anyString(), anyString());
        CurrencyApiResponse currencyApiResponse = currencyService.convertAmount(BigDecimal.valueOf(10), "EUR", "USD").block();
        assertNotNull(currencyApiResponse);
        assertEquals(BigDecimal.valueOf(11), currencyApiResponse.getAmount());
    }

    @Test
    public void givenNonCachedCurrencyObject_thenExpectMonoCurrencyResponse() {

        doReturn(Mono.empty())
                .when(currencyRepo).findBySourceCurrencyAndTargetCurrency(anyString(), anyString());
        CurrencyCacheObject currencyCacheObject = new CurrencyCacheObject("EUR_USD", "EUR", "USD", BigDecimal.valueOf(1.1));
        doReturn(Mono.just(currencyCacheObject)).when(currencyRepo).save(any());
        Map<String, String> map = new HashMap<>();
        map.put("EUR_USD", "1.1");
        doReturn(Mono.just(map)).when(currencyRestClient).getResponse(anyString(), anyString());
        CurrencyApiResponse currencyApiResponse = currencyService.convertAmount(BigDecimal.valueOf(10), "EUR", "USD").block();
        assertNotNull(currencyApiResponse);
        assertEquals(BigDecimal.valueOf(11), currencyApiResponse.getAmount());
    }

    @Test
    public void givenNonCachedCurrencyObject_WhenCurrencyUndefined_thenExpectProcessingException() {

        doReturn(Mono.empty())
                .when(currencyRepo).findBySourceCurrencyAndTargetCurrency(anyString(), anyString());
        Map<String, String> map = new HashMap<>();
        map.put("X_Y", "0");
        doReturn(Mono.just(map)).when(currencyRestClient).getResponse(anyString(), anyString());
        boolean catched = false;
        try {
            currencyService.convertAmount(BigDecimal.valueOf(10), "EUR", "USD").block();
        } catch (Exception ex) {
            catched = true;
            assert (ex.getMessage().contains(Constants.UNDEFINED_CURR.getValue()));
        }
        assertTrue(catched);
    }
}