package com.currency.converter.model;

import java.math.BigDecimal;

public class CurrencyCacheObject {

    private String id;
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal sourceTargetValue;

    public CurrencyCacheObject() {
    }

    public CurrencyCacheObject(String id, String sourceCurrency, String targetCurrency, BigDecimal sourceTargetValue) {
        this.id = id;
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.sourceTargetValue = sourceTargetValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getSourceTargetValue() {
        return sourceTargetValue;
    }

    public void setSourceTargetValue(BigDecimal sourceTargetValue) {
        this.sourceTargetValue = sourceTargetValue;
    }
}
