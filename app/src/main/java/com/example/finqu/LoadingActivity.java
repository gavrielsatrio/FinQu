package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.finqu.Controller.AccessCodeController;
import com.example.finqu.Data.GlobalData;
import com.example.finqu.Helper.DateHelper;
import com.example.finqu.Model.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Thread(new Runnable() {
            @Override
            public void run() {
                LoadData();
            }
        }).start();
    }

    private void LoadData() {
        GlobalData.sheetList = new ArrayList<>();
        GlobalData.transactionList = new ArrayList<>();
        GlobalData.transactionListGroupByDate = new Hashtable<>();

        HttpRequest requestSheets = new HttpRequest("https://sheets.googleapis.com/v4/spreadsheets/1_85IA1IJ2PH4Kb-95rfB5Eqa4I1vffdwtq2FaAShB0A/?key=AIzaSyBc2POX2kNNnxaeuB0Az6DZcD6D27aAIUM", "GET");
        requestSheets.setRequestProperty("Content-Type", "application/json");
        requestSheets.execute();

        try {
            JSONArray sheetJSONArray = new JSONObject(requestSheets.get()).getJSONArray("sheets");
            for (int i = 2; i < sheetJSONArray.length(); i++) {
                String sheetName = sheetJSONArray.getJSONObject(i).getJSONObject("properties").getString("title");
                GlobalData.sheetList.add(sheetName);

                HttpRequest request = new HttpRequest("https://sheets.googleapis.com/v4/spreadsheets/1_85IA1IJ2PH4Kb-95rfB5Eqa4I1vffdwtq2FaAShB0A/values/" + sheetName + "!A1:I200?key=AIzaSyBc2POX2kNNnxaeuB0Az6DZcD6D27aAIUM", "GET");
                request.setRequestProperty("Content-Type", "application/json");
                request.execute();

                JSONArray result = new JSONObject(request.get()).getJSONArray("values");
                int currentIndex = 4;

                String currentDate = "";

                while(!result.getJSONArray(currentIndex).getString(1).trim().equals("")) {
                    String date = result.getJSONArray(currentIndex).getString(0).trim();
                    String transactionType = result.getJSONArray(currentIndex).getString(1).trim();
                    String name = result.getJSONArray(currentIndex).getString(2).trim();
                    Boolean isIn = result.getJSONArray(currentIndex).getBoolean(3);
                    Boolean isOut = result.getJSONArray(currentIndex).getBoolean(4);
                    String paidBy = result.getJSONArray(currentIndex).getString(5);
                    String paymentType = result.getJSONArray(currentIndex).getString(6).trim();
                    Boolean isCheck = result.getJSONArray(currentIndex).getBoolean(7);
                    Integer amount = result.getJSONArray(currentIndex).getInt(8);

                    if(!currentDate.equals(date)) {
                        if(!date.equals("")) {
                            currentDate = date;
                        }
                    }

                    if(!GlobalData.transactionListGroupByDate.containsKey(DateHelper.convertToDateFromString(currentDate))) {
                        ArrayList<Transaction> newDailyTransactionList = new ArrayList<>();
                        newDailyTransactionList.add(0, new Transaction(DateHelper.convertToDateFromString(currentDate), transactionType, name, isIn, isOut, paidBy, paymentType, isCheck, amount));

                        GlobalData.transactionListGroupByDate.put(DateHelper.convertToDateFromString(currentDate), newDailyTransactionList);
                    } else {
                        GlobalData.transactionListGroupByDate.get(DateHelper.convertToDateFromString(currentDate)).add(0, new Transaction(DateHelper.convertToDateFromString(currentDate), transactionType, name, isIn, isOut, paidBy, paymentType, isCheck, amount));
                    }

                    GlobalData.transactionList.add(0, new Transaction(DateHelper.convertToDateFromString(currentDate), transactionType, name, isIn, isOut, paidBy, paymentType, isCheck, amount));

                    currentIndex++;
                }
            }

            isWriteExternalPermitted();
            isReadExternalPermitted();

            if(AccessCodeController.getCode(LoadingActivity.this) != null) {
                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            } else {
                Intent intent = new Intent(LoadingActivity.this, CreateNewCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private boolean isWriteExternalPermitted() {
        if(ActivityCompat.checkSelfPermission(LoadingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(LoadingActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 100);
            return false;
        }
    }

    private boolean isReadExternalPermitted() {
        if(ActivityCompat.checkSelfPermission(LoadingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(LoadingActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 200);
            return false;
        }
    }
}