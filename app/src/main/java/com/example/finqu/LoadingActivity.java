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
        GlobalData.FetchNewData();

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