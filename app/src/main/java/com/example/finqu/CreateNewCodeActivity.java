package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finqu.Controller.AccessCodeController;
import com.example.finqu.Helper.AccessCodeTxtHelper;

public class CreateNewCodeActivity extends AppCompatActivity {

    EditText txtCode1;
    EditText txtCode2;
    EditText txtCode3;
    EditText txtCode4;
    EditText txtCode5;
    EditText txtCode6;
    Button btnCreate;

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

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(helperTxtCodes.GetJoinedAccessCode(txtCodes).length() == 6) {
                    AccessCodeController.setCode(helperTxtCodes.GetJoinedAccessCode(txtCodes), CreateNewCodeActivity.this);

                    Intent intent = new Intent(CreateNewCodeActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                } else {
                    Toast.makeText(CreateNewCodeActivity.this, "Code must be 6 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}