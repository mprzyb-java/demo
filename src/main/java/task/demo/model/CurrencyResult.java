package task.demo.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CurrencyResult implements Serializable {

    public CurrencyResult(String source) {
        this.source = source;
        rates = new HashMap<>();
    }

    private String source;
    private Map<String, Double> rates;
}
