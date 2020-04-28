package task.demo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import task.demo.model.Currencies;
import task.demo.model.Currency;
import task.demo.model.Rate;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceTest {

    @Mock
    RestTemplateBuilder builder;

    @Mock
    RestTemplate restTemplate;

    CurrencyService service;

    @Before
    public void setUp() {
        when(builder.build()).thenReturn(restTemplate);
        service = new CurrencyService(builder);
    }

    @Test
    public void shouldNotGetCurrencyOfBitcoinToUsdWhenResponseIsIncorrect() {
        // given
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Currency.class))).thenReturn(responseEntity);
        // when
        Currency result = service.getCurrency("BTC", "USD");
        //then
        assertNull(result);
    }

    @Test
    public void shouldGetCurrencyOfBitcoinToUsd() {
        // given
        double rate = 7900;
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        Currency currency = mock(Currency.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.getBody()).thenReturn(currency);
        when(currency.getRate()).thenReturn(rate);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Currency.class))).thenReturn(responseEntity);
        // when
        Currency result = service.getCurrency("BTC", "USD");
        //then
        assertNotNull(result);
        assertEquals((int) rate, (int) result.getRate());
    }

    @Test
    public void shouldGetAllCurrenciesForBitcoin() {
        // given
        double rateUsd = 7900;
        double rateEth = 3;

        Rate rate1 = new Rate();
        rate1.setAsset_id_quote("USD");
        rate1.setRate(rateUsd);
        Rate rate2 = new Rate();
        rate2.setAsset_id_quote("ETH");
        rate2.setRate(rateEth);

        Currencies currencies = new Currencies();
        currencies.setAsset_id_base("BTC");
        currencies.setRates(new Rate[]{rate1, rate2});

        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.getBody()).thenReturn(currencies);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Currencies.class))).thenReturn(responseEntity);
        // when
        Currencies result = service.getCurrencies("BTC");
        // then
        assertNotNull(result);
        assertEquals((int) rate1.getRate(), (int) result.getRates()[0].getRate());
        assertEquals((int) rate2.getRate(), (int) result.getRates()[1].getRate());
    }

    @Test
    public void shouldNotGetAllCurrenciesForBitcoinWhenResponseIsIncorrect() {
        // given
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Currencies.class))).thenReturn(responseEntity);
        // when
        Currencies result = service.getCurrencies("BTC");
        // then
        assertNull(result);
    }
}