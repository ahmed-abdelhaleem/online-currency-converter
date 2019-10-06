package com.currency.converter.dto;

public enum Constants {
    UNDEFINED_CURR("Undefined Currency");


    private String value;

    Constants(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
