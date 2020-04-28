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

    private static final String BASE_URL_FORMAT = "https://rest.coinapi.io/v1/exchangerate/";

    private final RestTemplate restTemplate;

    public CurrencyService(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public Currency getCurrency(String from, String to) {
        String url = BASE_URL_FORMAT + from + "/" + to;

        ResponseEntity<Currency> response = restTemplate.exchange(url, HttpMethod.GET, setupRequest(), Currency.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public Currencies getCurrencies(String from) {
        String url = BASE_URL_FORMAT + from;

        ResponseEntity<Currencies> response = restTemplate.exchange(url, HttpMethod.GET, setupRequest(), Currencies.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    private HttpEntity setupRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("X-CoinAPI-Key", "6C6F1D19-2D41-444D-BC75-2CBC43B3297F");
        return new HttpEntity(headers);
    }
}
