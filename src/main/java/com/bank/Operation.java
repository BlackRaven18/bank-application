package com.bank;

import java.time.LocalDateTime;

public class Operation {

    private LocalDateTime operationLocalDate;
    private OperationType operationType;
    private String operationName;

    public Operation(){this(LocalDateTime.now(), null, "");}

    public Operation(LocalDateTime operationLocalDate, OperationType operationType, String operationName) {
        this.operationLocalDate = operationLocalDate;
        this.operationType = operationType;
        this.operationName = operationName;
    }

    public Operation(OperationType operationType){
        this.operationLocalDate = LocalDateTime.now();
        this.operationType = operationType;

        switch (this.operationType){
            case SENDING_MONEY_TRANSFER -> this.operationName = "Wysłanie przelewu";
            case RECEIVING_MONEY_TRANSFER -> this.operationName = "Otrzymanie przelewu";
            case CURRENCY_EXCHANGE -> this.operationName = "Wymiana waluty";
            case CREDIT_APPLICATION -> this.operationName = "Złożenie wniosku o kredyt";
            case CREDIT_GRANTED -> this.operationName = "Przyznanie kredytu";
        }
    }

    public LocalDateTime getOperationLocalDate() {
        return operationLocalDate;
    }

    public void setOperationLocalDate(LocalDateTime operationLocalDate) {
        this.operationLocalDate = operationLocalDate;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
