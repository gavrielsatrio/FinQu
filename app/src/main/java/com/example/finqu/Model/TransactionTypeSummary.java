package com.example.finqu.Model;

public class TransactionTypeSummary {
    public String TransactionType;
    public Integer SumAmount;

    public TransactionTypeSummary(String transactionTypeParam, Integer sumAmountParam) {
        this.TransactionType = transactionTypeParam;
        this.SumAmount = sumAmountParam;
    }
}
