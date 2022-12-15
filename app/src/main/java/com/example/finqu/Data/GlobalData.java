package com.example.finqu.Data;

import android.util.Log;

import com.example.finqu.Helper.DateHelper;
import com.example.finqu.HttpRequest;
import com.example.finqu.Model.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GlobalData {
    public static ArrayList<String> sheetList = new ArrayList<>();
    public static List<Transaction> transactionList = new ArrayList<>();
    public static Hashtable<Date, ArrayList<Transaction>> transactionListGroupByDate = new Hashtable<>();
    public static List<String> transactionTypeList = new ArrayList<>();

    public static void FetchNewData() {
        GlobalData.sheetList = new ArrayList<>();
        GlobalData.transactionList = new ArrayList<>();
        GlobalData.transactionListGroupByDate = new Hashtable<>();
        GlobalData.transactionTypeList = new ArrayList<>();

        HttpRequest requestSheets = new HttpRequest("https://sheets.googleapis.com/v4/spreadsheets/1_85IA1IJ2PH4Kb-95rfB5Eqa4I1vffdwtq2FaAShB0A/?key=AIzaSyBc2POX2kNNnxaeuB0Az6DZcD6D27aAIUM", "GET");
        requestSheets.setRequestProperty("Content-Type", "application/json");
        requestSheets.execute();

        try {
            JSONArray sheetJSONArray = new JSONObject(requestSheets.get()).getJSONArray("sheets");

            HttpRequest requestMasterDataSheet = new HttpRequest("https://sheets.googleapis.com/v4/spreadsheets/1_85IA1IJ2PH4Kb-95rfB5Eqa4I1vffdwtq2FaAShB0A/values/MasterData!A1:I200?key=AIzaSyBc2POX2kNNnxaeuB0Az6DZcD6D27aAIUM", "GET");
            requestMasterDataSheet.setRequestProperty("Content-Type", "application/json");
            requestMasterDataSheet.execute();

            JSONArray resultMasterDataSheet = new JSONObject(requestMasterDataSheet.get()).getJSONArray("values");

            GlobalData.transactionTypeList.add("All");
            for (int i = 1; i < resultMasterDataSheet.length(); i++) {
                GlobalData.transactionTypeList.add(resultMasterDataSheet.getJSONArray(i).getString(2).trim());
            }

            for (int i = 2; i < sheetJSONArray.length(); i++) {
                String sheetName = sheetJSONArray.getJSONObject(i).getJSONObject("properties").getString("title");
                GlobalData.sheetList.add(sheetName);

                HttpRequest requestMonthlySheet = new HttpRequest("https://sheets.googleapis.com/v4/spreadsheets/1_85IA1IJ2PH4Kb-95rfB5Eqa4I1vffdwtq2FaAShB0A/values/" + sheetName + "!A1:I200?key=AIzaSyBc2POX2kNNnxaeuB0Az6DZcD6D27aAIUM", "GET");
                requestMonthlySheet.setRequestProperty("Content-Type", "application/json");
                requestMonthlySheet.execute();

                JSONArray resultMonthlySheet = new JSONObject(requestMonthlySheet.get()).getJSONArray("values");
                int currentIndex = 4;

                String currentDate = "";

                while(!resultMonthlySheet.getJSONArray(currentIndex).getString(1).trim().equals("")) {
                    String date = resultMonthlySheet.getJSONArray(currentIndex).getString(0).trim();
                    String transactionType = resultMonthlySheet.getJSONArray(currentIndex).getString(1).trim();
                    String name = resultMonthlySheet.getJSONArray(currentIndex).getString(2).trim();
                    Boolean isIn = resultMonthlySheet.getJSONArray(currentIndex).getBoolean(3);
                    Boolean isOut = resultMonthlySheet.getJSONArray(currentIndex).getBoolean(4);
                    String paidBy = resultMonthlySheet.getJSONArray(currentIndex).getString(5);
                    String paymentType = resultMonthlySheet.getJSONArray(currentIndex).getString(6).trim();
                    Boolean isCheck = resultMonthlySheet.getJSONArray(currentIndex).getBoolean(7);
                    Integer amount = resultMonthlySheet.getJSONArray(currentIndex).getInt(8);

                    if(!currentDate.equals(date)) {
                        if(!date.equals("")) {
                            currentDate = date;
                        }
                    }

                    if(!GlobalData.transactionListGroupByDate.containsKey(DateHelper.convertToDateFromString(currentDate))) {
                        ArrayList<Transaction> newDailyTransactionList = new ArrayList<>();
                        newDailyTransactionList.add(0, new Transaction(DateHelper.convertToDateFromString(currentDate), transactionType, name, isIn, isOut, paidBy, paymentType, isCheck, amount, currentIndex + 1));

                        GlobalData.transactionListGroupByDate.put(DateHelper.convertToDateFromString(currentDate), newDailyTransactionList);
                    } else {
                        GlobalData.transactionListGroupByDate.get(DateHelper.convertToDateFromString(currentDate)).add(0, new Transaction(DateHelper.convertToDateFromString(currentDate), transactionType, name, isIn, isOut, paidBy, paymentType, isCheck, amount, currentIndex + 1));
                    }

                    GlobalData.transactionList.add(0, new Transaction(DateHelper.convertToDateFromString(currentDate), transactionType, name, isIn, isOut, paidBy, paymentType, isCheck, amount, currentIndex + 1));

                    currentIndex++;
                }
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
