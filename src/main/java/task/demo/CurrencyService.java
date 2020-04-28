package task.demo;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import task.demo.model.Currencies;
import task.demo.model.Currency;

import java.util.Collections;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;

    public CurrencyService(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public Currency getCurrency(String from, String to) {
        String url = String.format("https://rest.coinapi.io/v1/exchangerate/%s/%s", from, to);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-CoinAPI-Key", "6C6F1D19-2D41-444D-BC75-2CBC43B3297F");
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Currency> response = restTemplate.exchange(url, HttpMethod.GET, request, Currency.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public Currencies getCurrencies(String from) {
        String url = String.format("https://rest.coinapi.io/v1/exchangerate/%s", from);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-CoinAPI-Key", "6C6F1D19-2D41-444D-BC75-2CBC43B3297F");
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<Currencies> response = restTemplate.exchange(url, HttpMethod.GET, request, Currencies.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }
}
