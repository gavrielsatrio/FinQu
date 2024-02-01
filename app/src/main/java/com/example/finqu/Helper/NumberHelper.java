package com.example.finqu.Helper;

import java.text.NumberFormat;
import java.util.Currency;

public class NumberHelper {
    public static String convertToRpFormat(double number) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(0);
        formatter.setCurrency(Currency.getInstance("IDR"));

        return formatter.format(number).replace("IDR", "Rp").replace(",", ".");
    }

    public static String convertToNumberFormat(double amount, int fractionDigits) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMinimumFractionDigits(fractionDigits);
        formatter.setMaximumFractionDigits(fractionDigits);

        return formatter.format(amount).replace(",", ".");
    }
}
