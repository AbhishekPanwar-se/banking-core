package com.personal.banking_core.transaction.dto;

public enum TransactionSortField {
	TRANSACTION_DATE_TIME("transactionDateTime"),
    AMOUNT("amount");

    private final String field;

    TransactionSortField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
