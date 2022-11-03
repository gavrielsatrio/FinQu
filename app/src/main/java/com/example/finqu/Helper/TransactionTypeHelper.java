package com.example.finqu.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.finqu.R;

import java.util.Dictionary;
import java.util.Hashtable;

public class TransactionTypeHelper {
    private static TransactionTypeHelper instance;
    private Context context;
    private Dictionary<String, Drawable> transactionTypeList = new Hashtable<>();

    private TransactionTypeHelper(Context contextParam) {
        this.context = contextParam;

        transactionTypeList.put("Beverage", this.context.getResources().getDrawable(R.drawable.beverages, this.context.getTheme()));
        transactionTypeList.put("Boarding Fee", this.context.getResources().getDrawable(R.drawable.boarding_fee, this.context.getTheme()));
        transactionTypeList.put("Desert", this.context.getResources().getDrawable(R.drawable.dessert_2, this.context.getTheme()));
        transactionTypeList.put("Electricity Token", this.context.getResources().getDrawable(R.drawable.electricity_token, this.context.getTheme()));
        transactionTypeList.put("Food", this.context.getResources().getDrawable(R.drawable.food, this.context.getTheme()));
        transactionTypeList.put("Laundry", this.context.getResources().getDrawable(R.drawable.laundry2, this.context.getTheme()));
        transactionTypeList.put("Medicine", this.context.getResources().getDrawable(R.drawable.medicine, this.context.getTheme()));
        transactionTypeList.put("Shopping", this.context.getResources().getDrawable(R.drawable.shopping, this.context.getTheme()));
        transactionTypeList.put("Transportation", this.context.getResources().getDrawable(R.drawable.transportation, this.context.getTheme()));
        transactionTypeList.put("Travel Ticket Fee", this.context.getResources().getDrawable(R.drawable.ticket2, this.context.getTheme()));
    }

    public static TransactionTypeHelper getInstance(Context contextParam) {
        if(TransactionTypeHelper.instance == null) {
            TransactionTypeHelper.instance = new TransactionTypeHelper(contextParam);
        }

        return TransactionTypeHelper.instance;
    }

    public Drawable getPaymentIcon(String paymentType) {
        return transactionTypeList.get(paymentType);
    }
}
