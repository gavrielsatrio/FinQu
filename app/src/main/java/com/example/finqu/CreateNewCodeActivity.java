package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finqu.Controller.AccessCodeController;

import java.util.Timer;
import java.util.TimerTask;

public class CreateNewCodeActivity extends AppCompatActivity {

    EditText txtCode1;
    EditText txtCode2;
    EditText txtCode3;
    EditText txtCode4;
    EditText txtCode5;
    EditText txtCode6;
    Button btnCreate;

    EditText currentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_code);

        btnCreate = findViewById(R.id.createNewCodeBtnCreate);
        txtCode1 = findViewById(R.id.createNewCodeTxtCode1);
        txtCode2 = findViewById(R.id.createNewCodeTxtCode2);
        txtCode3 = findViewById(R.id.createNewCodeTxtCode3);
        txtCode4 = findViewById(R.id.createNewCodeTxtCode4);
        txtCode5 = findViewById(R.id.createNewCodeTxtCode5);
        txtCode6 = findViewById(R.id.createNewCodeTxtCode6);

        currentTxt = txtCode1;

        EditText[] txtCodes = new EditText[]
                {
                        txtCode1,
                        txtCode2,
                        txtCode3,
                        txtCode4,
                        txtCode5,
                        txtCode6
                };

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isCodeValid;
                String inputtedCode = "";
                for (int i = 0; i < txtCodes.length; i++) {
                    inputtedCode += txtCodes[i].getText().toString();
                }

                isCodeValid = inputtedCode.length() == 6;

                if(isCodeValid) {
                    AccessCodeController.setCode(inputtedCode, CreateNewCodeActivity.this);

                    Intent intent = new Intent(CreateNewCodeActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                } else {
                    Toast.makeText(CreateNewCodeActivity.this, "Code is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextWatcher txtCodeTextWather = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                View theFollowingTxt;

                if(!currentTxt.getText().toString().equals("")) {
                    theFollowingTxt = currentTxt.focusSearch(View.FOCUS_RIGHT);
                } else {
                    theFollowingTxt = currentTxt.focusSearch(View.FOCUS_LEFT);
                }

                if(theFollowingTxt != null) {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            CreateNewCodeActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    currentTxt = (EditText) theFollowingTxt;
                                    theFollowingTxt.requestFocus();
                                }
                            });
                        }
                    }, 100);
                }
            }
        };

        txtCode1.addTextChangedListener(txtCodeTextWather);
        txtCode2.addTextChangedListener(txtCodeTextWather);
        txtCode3.addTextChangedListener(txtCodeTextWather);
        txtCode4.addTextChangedListener(txtCodeTextWather);
        txtCode5.addTextChangedListener(txtCodeTextWather);
        txtCode6.addTextChangedListener(txtCodeTextWather);
    }
}