package task.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import task.demo.model.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {

    @Mock
    CurrencyService service;

    @InjectMocks
    Controller controller;

    @Test
    public void shouldReturnAllCurrenciesForBitcoin() {
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

        when(service.getCurrencies("BTC")).thenReturn(currencies);
        // when
        CurrencyResult result = controller.getCurrencies("BTC", null);
        // then
        verify(service, times(1)).getCurrencies("BTC");
        verify(service, times(0)).getCurrency(eq("BTC"), anyString());
        assertNotNull(result);
        assertFalse(result.getRates().isEmpty());
        assertEquals(2, result.getRates().size());
        assertEquals((int) rateUsd, result.getRates().get("USD").intValue());
        assertEquals((int) rateEth, result.getRates().get("ETH").intValue());
    }

    @Test
    public void shouldReturnUsdCurrencyForBitcoinWhenUsdIsInFilter() {
        // given
        double rateUsd = 7900;

        Currency currency = new Currency();
        currency.setAsset_id_base("BTC");
        currency.setAsset_id_quote("USD");
        currency.setRate(rateUsd);

        when(service.getCurrency("BTC", "USD")).thenReturn(currency);
        // when
        CurrencyResult result = controller.getCurrencies("BTC", new String[]{"USD"});
        // then
        verify(service, times(0)).getCurrencies("BTC");
        verify(service, times(1)).getCurrency("BTC", "USD");
        assertNotNull(result);
        assertFalse(result.getRates().isEmpty());
        assertEquals(1, result.getRates().size());
        assertEquals((int) rateUsd, result.getRates().get("USD").intValue());
    }

    @Test
    public void shouldReturnUsdAndEthCurrenciesForBitcoinWhenUsdAndEthAreInFilter() {
        // given
        double rateUsd = 7900;
        double rateEth = 3;

        Currency currencyUsd = new Currency();
        currencyUsd.setAsset_id_base("BTC");
        currencyUsd.setAsset_id_quote("USD");
        currencyUsd.setRate(rateUsd);
        Currency currencyEth = new Currency();
        currencyEth.setAsset_id_base("BTC");
        currencyEth.setAsset_id_quote("ETH");
        currencyEth.setRate(rateEth);

        when(service.getCurrency("BTC", "USD")).thenReturn(currencyUsd);
        when(service.getCurrency("BTC", "ETH")).thenReturn(currencyEth);
        // when
        CurrencyResult result = controller.getCurrencies("BTC", new String[]{"USD", "ETH"});
        // then
        verify(service, times(0)).getCurrencies("BTC");
        verify(service, times(2)).getCurrency(eq("BTC"), anyString());
        assertNotNull(result);
        assertFalse(result.getRates().isEmpty());
        assertEquals(2, result.getRates().size());
        assertEquals((int) rateUsd, result.getRates().get("USD").intValue());
        assertEquals((int) rateEth, result.getRates().get("ETH").intValue());
    }

    @Test
    public void shouldGetCurrencyBeStringCaseIndependent() {
        // given
        Currencies currencies = mock(Currencies.class);
        when(currencies.getRates()).thenReturn(new Rate[]{});
        when(service.getCurrencies("BTC")).thenReturn(currencies);
        when(service.getCurrency("BTC", "USD")).thenReturn(mock(Currency.class));
        // when
        controller.getCurrencies("BTC", null);
        controller.getCurrencies("btc", new String[]{});
        controller.getCurrencies("BTC", new String[]{"USD"});
        controller.getCurrencies("btc", new String[]{"USD"});
        controller.getCurrencies("btc", new String[]{"usd"});
        controller.getCurrencies("BTC", new String[]{"usd"});
        // then
        verify(service, times(2)).getCurrencies("BTC");
        verify(service, times(4)).getCurrency("BTC", "USD");
    }

    @Test
    public void shouldGetExchangeForBitcoinForUsdAndEth() {
        // given
        int amount = 50;
        double rateUsd = 7900;
        double rateEth = 3;

        Exchange exchange = new Exchange();
        exchange.setFrom("BTC");
        exchange.setTo(new String[]{"USD", "ETH"});
        exchange.setAmount(amount);

        Currency currencyUsd = new Currency();
        currencyUsd.setAsset_id_base("BTC");
        currencyUsd.setAsset_id_quote("USD");
        currencyUsd.setRate(rateUsd);
        Currency currencyEth = new Currency();
        currencyEth.setAsset_id_base("BTC");
        currencyEth.setAsset_id_quote("ETH");
        currencyEth.setRate(rateEth);

        when(service.getCurrency("BTC", "USD")).thenReturn(currencyUsd);
        when(service.getCurrency("BTC", "ETH")).thenReturn(currencyEth);
        // when
        ExchangeResult result = controller.getExchange(exchange);
        // then
        verify(service, times(2)).getCurrency(eq("BTC"), anyString());
        assertNotNull(result);
        assertFalse(result.getExchanges().isEmpty());
        assertEquals(2, result.getExchanges().size());
        double feeUsd = rateUsd / 100;
        assertEquals(feeUsd, result.getExchanges().get("USD").getFee(), 0.1);
        assertEquals(amount * (rateUsd + feeUsd), result.getExchanges().get("USD").getResult(), 0.1);
        double feeEth = rateEth / 100;
        assertEquals(feeEth, result.getExchanges().get("ETH").getFee(), 0.01);
        assertEquals(amount * (rateEth + feeEth), result.getExchanges().get("ETH").getResult(), 0.01);
    }
}