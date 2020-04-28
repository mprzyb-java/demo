package task.demo.model;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ExchangeFee implements Serializable {

    public ExchangeFee(double rate, int amount) {
        this.rate = rate;
        this.amount = amount;
        this.fee = rate / 100;
        this.result = amount * (rate + fee);
    }

    private final double rate;
    private final int amount;
    private final double result;
    private final double fee;
}
