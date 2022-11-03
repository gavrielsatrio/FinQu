package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finqu.Controller.AccessCodeController;
import com.example.finqu.Helper.AccessCodeTxtHelper;

public class ChangeAccessCodeActivity extends AppCompatActivity {

    Button btnSave;
    ImageView btnBack;

    EditText txtOldCode1;
    EditText txtOldCode2;
    EditText txtOldCode3;
    EditText txtOldCode4;
    EditText txtOldCode5;
    EditText txtOldCode6;

    EditText txtNewCode1;
    EditText txtNewCode2;
    EditText txtNewCode3;
    EditText txtNewCode4;
    EditText txtNewCode5;
    EditText txtNewCode6;

    EditText txtConfirmCode1;
    EditText txtConfirmCode2;
    EditText txtConfirmCode3;
    EditText txtConfirmCode4;
    EditText txtConfirmCode5;
    EditText txtConfirmCode6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_access_code);

        btnSave = findViewById(R.id.changeAccessCodeBtnSave);
        btnBack = findViewById(R.id.changeAccessCodeBtnBack);

        txtOldCode1 = findViewById(R.id.changeAccessCodeTxtOldCode1);
        txtOldCode2 = findViewById(R.id.changeAccessCodeTxtOldCode2);
        txtOldCode3 = findViewById(R.id.changeAccessCodeTxtOldCode3);
        txtOldCode4 = findViewById(R.id.changeAccessCodeTxtOldCode4);
        txtOldCode5 = findViewById(R.id.changeAccessCodeTxtOldCode5);
        txtOldCode6 = findViewById(R.id.changeAccessCodeTxtOldCode6);

        txtNewCode1 = findViewById(R.id.changeAccessCodeTxtNewCode1);
        txtNewCode2 = findViewById(R.id.changeAccessCodeTxtNewCode2);
        txtNewCode3 = findViewById(R.id.changeAccessCodeTxtNewCode3);
        txtNewCode4 = findViewById(R.id.changeAccessCodeTxtNewCode4);
        txtNewCode5 = findViewById(R.id.changeAccessCodeTxtNewCode5);
        txtNewCode6 = findViewById(R.id.changeAccessCodeTxtNewCode6);

        txtConfirmCode1 = findViewById(R.id.changeAccessCodeTxtConfirmCode1);
        txtConfirmCode2 = findViewById(R.id.changeAccessCodeTxtConfirmCode2);
        txtConfirmCode3 = findViewById(R.id.changeAccessCodeTxtConfirmCode3);
        txtConfirmCode4 = findViewById(R.id.changeAccessCodeTxtConfirmCode4);
        txtConfirmCode5 = findViewById(R.id.changeAccessCodeTxtConfirmCode5);
        txtConfirmCode6 = findViewById(R.id.changeAccessCodeTxtConfirmCode6);

        EditText[] txtOldCodes = new EditText[]
        {
            txtOldCode1,
            txtOldCode2,
            txtOldCode3,
            txtOldCode4,
            txtOldCode5,
            txtOldCode6,
        };

        EditText[] txtNewCodes = new EditText[]
        {
            txtNewCode1,
            txtNewCode2,
            txtNewCode3,
            txtNewCode4,
            txtNewCode5,
            txtNewCode6,
        };

        EditText[] txtConfirmCodes = new EditText[]
        {
            txtConfirmCode1,
            txtConfirmCode2,
            txtConfirmCode3,
            txtConfirmCode4,
            txtConfirmCode5,
            txtConfirmCode6,
        };

        AccessCodeTxtHelper helperOldTxtCodes = new AccessCodeTxtHelper();
        AccessCodeTxtHelper helperNewTxtCodes = new AccessCodeTxtHelper();
        AccessCodeTxtHelper helperConfirmTxtCodes = new AccessCodeTxtHelper();

        helperOldTxtCodes.BindAllListener(txtOldCodes);
        helperNewTxtCodes.BindAllListener(txtNewCodes);
        helperConfirmTxtCodes.BindAllListener(txtConfirmCodes);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeAccessCodeActivity.super.onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldAccessCode = helperOldTxtCodes.GetJoinedAccessCode(txtOldCodes);
                String newAccessCode = helperNewTxtCodes.GetJoinedAccessCode(txtNewCodes);
                String confirmAccessCode = helperConfirmTxtCodes.GetJoinedAccessCode(txtConfirmCodes);

                if(oldAccessCode.equals(AccessCodeController.getCode(ChangeAccessCodeActivity.this))) {
                    if(newAccessCode.length() == 6) {
                        if(confirmAccessCode.equals(newAccessCode)) {
                            AccessCodeController.setCode(newAccessCode, ChangeAccessCodeActivity.this);

                            Toast.makeText(ChangeAccessCodeActivity.this, "Access code successfully changed", Toast.LENGTH_SHORT).show();

                            ChangeAccessCodeActivity.super.onBackPressed();
                        } else {
                            Toast.makeText(ChangeAccessCodeActivity.this, "Confirm access code doesn't match", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ChangeAccessCodeActivity.this, "New access code must be 6 characters", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangeAccessCodeActivity.this, "Old access code is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}