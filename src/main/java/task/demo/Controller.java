package task.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import task.demo.model.*;

/**
 * Controller which listen to requests from clients and returns results of operations on cryptocurrency.
 */
@RestController
@RequestMapping(path = "/currencies/", produces = MediaType.APPLICATION_JSON_VALUE)
public class Controller {

    @Autowired
    CurrencyService currencyService;

    @GetMapping("{from}")
    public CurrencyResult getCurrencies(@PathVariable String from, @RequestParam(required = false) String[] filter) {
        from = from.toUpperCase();
        CurrencyResult result = new CurrencyResult(from);
        if (filter == null || filter.length == 0) {
            Currencies currencies = currencyService.getCurrencies(from);
            for (Rate rate : currencies.getRates()) {
                result.getRates().put(rate.getAsset_id_quote(), rate.getRate());
            }
            return result;
        }
        for (String to : filter) {
            to = to.toUpperCase();
            Currency currency = currencyService.getCurrency(from, to);
            result.getRates().put(to, currency.getRate());
        }
        return result;
    }

    @PostMapping("exchange")
    public ExchangeResult getExchange(@RequestBody Exchange exchange) {
        ExchangeResult result = new ExchangeResult(exchange.getFrom().toUpperCase());
        for (String to : exchange.getTo()) {
            to = to.toUpperCase();
            Currency currency = currencyService.getCurrency(result.getFrom().toUpperCase(), to);
            result.getExchanges().put(to, new ExchangeFee(currency.getRate(), exchange.getAmount()));
        }
        return result;
    }
}
