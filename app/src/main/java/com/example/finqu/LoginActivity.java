package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finqu.Controller.AccessCodeController;
import com.example.finqu.Helper.AccessCodeTxtHelper;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class LoginActivity extends AppCompatActivity {

    EditText txtCode1;
    EditText txtCode2;
    EditText txtCode3;
    EditText txtCode4;
    EditText txtCode5;
    EditText txtCode6;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.loginBtnLogin);
        txtCode1 = findViewById(R.id.loginTxtCode1);
        txtCode2 = findViewById(R.id.loginTxtCode2);
        txtCode3 = findViewById(R.id.loginTxtCode3);
        txtCode4 = findViewById(R.id.loginTxtCode4);
        txtCode5 = findViewById(R.id.loginTxtCode5);
        txtCode6 = findViewById(R.id.loginTxtCode6);

        EditText[] txtCodes = new EditText[]
        {
            txtCode1,
            txtCode2,
            txtCode3,
            txtCode4,
            txtCode5,
            txtCode6
        };

        AccessCodeTxtHelper helperTxtCodes = new AccessCodeTxtHelper();
        helperTxtCodes.BindAllListener(txtCodes);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(helperTxtCodes.GetJoinedAccessCode(txtCodes).equals(AccessCodeController.getCode(LoginActivity.this))) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Code is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}