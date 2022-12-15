package com.example.finqu.Model;

import java.util.Date;

public class Transaction {
    public Date Date;
    public String TransactionType;
    public String Name;
    public Boolean IsIn;
    public Boolean IsOut;
    public String PaidBy;
    public String PaymentType;
    public Boolean IsCheck;
    public Integer Amount;
    public Integer RowNoInExcel;

    public Transaction(Date dateParam, String transactionTypeParam, String nameParam, Boolean isInParam, Boolean isOutParam, String paidByParam, String paymentTypeParam, Boolean isCheckParam, Integer amountParam, Integer rowNoInExcel) {
        this.Date = dateParam;
        this.TransactionType = transactionTypeParam;
        this.Name = nameParam;
        this.IsIn = isInParam;
        this.IsOut = isOutParam;
        this.PaidBy = paidByParam;
        this.PaymentType = paymentTypeParam;
        this.IsCheck = isCheckParam;
        this.Amount = amountParam;
        this.RowNoInExcel = rowNoInExcel;
    }
}
