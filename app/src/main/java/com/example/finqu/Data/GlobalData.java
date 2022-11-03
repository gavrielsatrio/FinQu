package com.example.finqu.Data;

import com.example.finqu.Model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class GlobalData {
    public static ArrayList<String> sheetList = new ArrayList<>();
    public static List<Transaction> transactionList = new ArrayList<>();
    public static Hashtable<Date, ArrayList<Transaction>> transactionListGroupByDate = new Hashtable<>();
}
