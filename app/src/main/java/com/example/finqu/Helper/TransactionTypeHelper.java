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

        transactionTypeList.put("All", this.context.getResources().getDrawable(R.drawable.filter, this.context.getTheme()));
        transactionTypeList.put("Bank Admin Fee", this.context.getResources().getDrawable(R.drawable.invoice, this.context.getTheme()));
        transactionTypeList.put("Bank Transfer", this.context.getResources().getDrawable(R.drawable.bank_transfer, this.context.getTheme()));
        transactionTypeList.put("Beverage", this.context.getResources().getDrawable(R.drawable.beverages, this.context.getTheme()));
        transactionTypeList.put("Boarding Fee", this.context.getResources().getDrawable(R.drawable.rent, this.context.getTheme()));
        transactionTypeList.put("Cake", this.context.getResources().getDrawable(R.drawable.cheesecake, this.context.getTheme()));
        transactionTypeList.put("Desert", this.context.getResources().getDrawable(R.drawable.dessert_2, this.context.getTheme()));
        transactionTypeList.put("Electricity Token", this.context.getResources().getDrawable(R.drawable.flash, this.context.getTheme()));
        transactionTypeList.put("Food", this.context.getResources().getDrawable(R.drawable.food, this.context.getTheme()));
        transactionTypeList.put("Income", this.context.getResources().getDrawable(R.drawable.money_bag, this.context.getTheme()));
        transactionTypeList.put("Laundry", this.context.getResources().getDrawable(R.drawable.laundry2, this.context.getTheme()));
        transactionTypeList.put("Medicine", this.context.getResources().getDrawable(R.drawable.medicine, this.context.getTheme()));
        transactionTypeList.put("Shopping", this.context.getResources().getDrawable(R.drawable.shop_cart, this.context.getTheme()));
        transactionTypeList.put("Snack", this.context.getResources().getDrawable(R.drawable.popcorn, this.context.getTheme()));
        transactionTypeList.put("Phone Credit Quota", this.context.getResources().getDrawable(R.drawable.sim, this.context.getTheme()));
        transactionTypeList.put("Transportation", this.context.getResources().getDrawable(R.drawable.transportation, this.context.getTheme()));
        transactionTypeList.put("Travel Ticket Fee", this.context.getResources().getDrawable(R.drawable.ticket2, this.context.getTheme()));
    }

    public static TransactionTypeHelper getInstance(Context contextParam) {
        if(TransactionTypeHelper.instance == null) {
            TransactionTypeHelper.instance = new TransactionTypeHelper(contextParam);
        }

        return TransactionTypeHelper.instance;
    }

    public Drawable getTransactionTypeIcon(String transactionType) {
        return transactionTypeList.get(transactionType);
    }
}
