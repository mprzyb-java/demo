package task.demo.model;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ExchangeResult implements Serializable {

    public ExchangeResult(String from) {
        this.from = from;
        this.exchanges = new HashMap<>();
    }

    private final String from;
    private final Map<String, ExchangeFee> exchanges;
}
