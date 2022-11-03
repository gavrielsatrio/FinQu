package com.example.finqu.Helper;

import java.text.NumberFormat;
import java.util.Currency;

public class NumberHelper {
    public static String convertToRpFormat(Integer number) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(0);
        formatter.setCurrency(Currency.getInstance("IDR"));

        return formatter.format(number).replace("IDR", "Rp").replace(",", ".");
    }
}
