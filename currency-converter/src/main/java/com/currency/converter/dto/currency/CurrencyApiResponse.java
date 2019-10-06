package com.currency.converter.dto.currency;

import java.math.BigDecimal;

public class CurrencyApiResponse {

    private BigDecimal amount;

    public CurrencyApiResponse(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
