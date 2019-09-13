package weg.wojciech.recalculate_currency.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class RecalculatedCurrency {

    private BigDecimal amount;
    private Currency currency;

    public RecalculatedCurrency(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecalculatedCurrency that = (RecalculatedCurrency) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
