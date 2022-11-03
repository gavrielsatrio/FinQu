package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finqu.Controller.TransactionItemController;
import com.example.finqu.Data.GlobalData;
import com.example.finqu.Helper.DateHelper;
import com.example.finqu.Helper.NumberHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView lblTodayExpense;
    LinearLayout linearLayoutTransaction;
    FloatingActionButton btnAdd;
    TextView lblViewReport;
    ImageView imgViewReportArrow;
    EditText txtStartDate;
    EditText txtEndDate;
    ImageView btnSetting;

    private String dateState = "start";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblTodayExpense = findViewById(R.id.mainLblTodayExpenseValue);
        linearLayoutTransaction = findViewById(R.id.mainLinearLayoutTransactionItem);
        btnAdd = findViewById(R.id.mainBtnAdd);
        lblViewReport = findViewById(R.id.mainLblViewReport);
        imgViewReportArrow = findViewById(R.id.mainImgViewReportArrow);
        txtStartDate = findViewById(R.id.mainTxtStartDate);
        txtEndDate = findViewById(R.id.mainTxtEndDate);
        btnSetting = findViewById(R.id.mainBtnSetting);

        txtStartDate.setKeyListener(null);
        txtEndDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance();
        txtStartDate.setText(DateHelper.convertToShortStringFromDate(calendar.getTime()));
        txtEndDate.setText(DateHelper.convertToShortStringFromDate(calendar.getTime()));

        LoadTransactionList();
        LoadTodayExpenses();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                String selectedDate = DateHelper.convertToShortStringFromDate(calendar.getTime());

                if(dateState.equals("start")) {
                    txtStartDate.setText(selectedDate);

                    Date startDate = DateHelper.convertToDateFromShortString(txtStartDate.getText().toString());
                    Date endDate = DateHelper.convertToDateFromShortString(txtEndDate.getText().toString());
                    if(startDate.after(endDate)) {
                        txtEndDate.setText(DateHelper.convertToShortStringFromDate(startDate));
                    }
                } else {
                    txtEndDate.setText(selectedDate);

                    Date startDate = DateHelper.convertToDateFromShortString(txtStartDate.getText().toString());
                    Date endDate = DateHelper.convertToDateFromShortString(txtEndDate.getText().toString());
                    if(endDate.before(startDate)) {
                        txtStartDate.setText(DateHelper.convertToShortStringFromDate(endDate));
                    }
                }

                LoadTransactionList();
            }
        };

        View.OnClickListener viewReportClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ViewReportActivity.class);
                startActivity(intent);
            }
        };

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateState = "start";
                new DatePickerDialog(MainActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateState = "end";
                new DatePickerDialog(MainActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        lblViewReport.setOnClickListener(viewReportClick);
        imgViewReportArrow.setOnClickListener(viewReportClick);

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    String currentDateOnList = "";
    Integer totalTodayExpense = 0;

    private void LoadTransactionList() {
        currentDateOnList = "";

        linearLayoutTransaction.removeAllViews();

        Date startDate = DateHelper.convertToDateFromShortString(txtStartDate.getText().toString());
        Date endDate = DateHelper.convertToDateFromShortString(txtEndDate.getText().toString());

        GlobalData.transactionList.stream().filter(x -> (x.Date.after(startDate) && x.Date.before(endDate)) || x.Date.equals(startDate) || x.Date.equals(endDate)).forEach(x ->
        {
            if(!currentDateOnList.equals(DateHelper.convertToStringFromDate(x.Date))) {
                currentDateOnList = DateHelper.convertToStringFromDate(x.Date);

                View transactionDateView = LayoutInflater.from(MainActivity.this).inflate(R.layout.transaction_item_date_layout, null, false);
                ((TextView)transactionDateView.findViewById(R.id.transactionItemDateLbl)).setText(currentDateOnList);
                linearLayoutTransaction.addView(transactionDateView);
            }

            TransactionItemController itemLayout = new TransactionItemController(MainActivity.this, x);
            itemLayout.setDataAndEventsToView();

            linearLayoutTransaction.addView(itemLayout.getView());
        });
    }

    private void LoadTodayExpenses() {
        totalTodayExpense = 0;

        GlobalData.transactionList.stream().filter(x -> x.Date.equals(DateHelper.getDateNow())).forEach(x ->
        {
            if(x.IsOut) {
                totalTodayExpense += x.Amount;
            } else {
                totalTodayExpense -= x.Amount;
            }
        });

        lblTodayExpense.setText(NumberHelper.convertToRpFormat(totalTodayExpense));
    }
}