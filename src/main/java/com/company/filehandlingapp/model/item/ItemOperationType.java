package com.company.filehandlingapp.model.item;

public enum ItemOperationType {

    SUPPLY("supply"), BUY("buy");

    private final String value;

    ItemOperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
