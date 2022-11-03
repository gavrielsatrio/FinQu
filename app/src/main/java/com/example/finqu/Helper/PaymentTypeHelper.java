package com.example.finqu.Helper;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.finqu.R;

import java.util.Dictionary;
import java.util.Hashtable;

public class PaymentTypeHelper {
    private static PaymentTypeHelper instance;
    private Context context;
    private Dictionary<String, Drawable> paymentTypeList = new Hashtable<>();

    private PaymentTypeHelper(Context contextParam) {
        this.context = contextParam;

        paymentTypeList.put("BCA Mobile", this.context.getResources().getDrawable(R.drawable.bca_mobile, this.context.getTheme()));
        paymentTypeList.put("Cash", this.context.getResources().getDrawable(R.drawable.cash, this.context.getTheme()));
        paymentTypeList.put("Debit BCA", this.context.getResources().getDrawable(R.drawable.debit_bca, this.context.getTheme()));
        paymentTypeList.put("Flazz", this.context.getResources().getDrawable(R.drawable.flazz, this.context.getTheme()));
        paymentTypeList.put("GoPay", this.context.getResources().getDrawable(R.drawable.gopay, this.context.getTheme()));
        paymentTypeList.put("JakCard", this.context.getResources().getDrawable(R.drawable.jakcard, this.context.getTheme()));
        paymentTypeList.put("OVO", this.context.getResources().getDrawable(R.drawable.ovo, this.context.getTheme()));
        paymentTypeList.put("QRIS", this.context.getResources().getDrawable(R.drawable.qris, this.context.getTheme()));
    }

    public static PaymentTypeHelper getInstance(Context contextParam) {
        if(PaymentTypeHelper.instance == null) {
            PaymentTypeHelper.instance = new PaymentTypeHelper(contextParam);
        }

        return PaymentTypeHelper.instance;
    }

    public Drawable getPaymentIcon(String paymentType) {
        return paymentTypeList.get(paymentType);
    }
}
