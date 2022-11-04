package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.finqu.Adapter.ComboBoxAdapter;
import com.example.finqu.Data.GlobalData;
import com.example.finqu.Helper.DateHelper;
import com.example.finqu.Helper.NumberHelper;
import com.example.finqu.Model.Transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ViewReportActivity extends AppCompatActivity {

    ImageView btnBack;
    Spinner comboBoxMonth;
    ImageView imgChart;
    TextView lblTotal;

    HorizontalScrollView scrollView;

    Date selectedMonth;
    int bitmapChartMinWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        btnBack = findViewById(R.id.viewReportBtnBack);
        comboBoxMonth = findViewById(R.id.viewReportComboMonth);
        imgChart = findViewById(R.id.viewReportImgChart);
        lblTotal = findViewById(R.id.viewReportLblTotalExpense);
        scrollView = findViewById(R.id.viewReportScrollViewChart);

        imgChart.setImageBitmap(null);

        LoadComboMonth();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewReportActivity.super.onBackPressed();
            }
        });

        comboBoxMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMonth = DateHelper.convertToMonthAndYearFromString(GlobalData.sheetList.get(comboBoxMonth.getSelectedItemPosition()));

                LoadTotalExpenses();
                LoadChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void LoadComboMonth() {
        comboBoxMonth.setAdapter(new ComboBoxAdapter(ViewReportActivity.this, GlobalData.sheetList));

        comboBoxMonth.setSelection(GlobalData.sheetList.indexOf(DateHelper.getMonthAndYearFromDate(DateHelper.getDateNow())));
    }

    private int totalMonthExpense = 0;
    private void LoadTotalExpenses() {
        totalMonthExpense = 0;

        GlobalData.transactionList.stream().filter(x -> x.Date.getMonth() == selectedMonth.getMonth()).forEach(x ->
        {
            if(x.IsOut) {
                totalMonthExpense += x.Amount;
            } else {
                if(!x.TransactionType.equals("Income")) {
                    totalMonthExpense -= x.Amount;
                }
            }
        });

        lblTotal.setText("Total : " + NumberHelper.convertToRpFormat(totalMonthExpense));
    }

    private void LoadChart() {
        int dayCount = (int) GlobalData.transactionListGroupByDate.keySet().stream().filter(x -> x.getMonth() == selectedMonth.getMonth()).count();
        int dataXDistance = 130;
        int barChartWidth = 50;
        Integer bitmapHeight = 1000;
        Integer xAxisLabelHeight = 90;

        Bitmap bitmap = Bitmap.createBitmap(dayCount * dataXDistance, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(ViewReportActivity.this.getResources().getColor(R.color.lightgray, ViewReportActivity.this.getTheme()));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, bitmapHeight - xAxisLabelHeight, dayCount * dataXDistance, bitmapHeight, paint);

        List<Date> transactionDateList = new ArrayList<>(GlobalData.transactionListGroupByDate.keySet().stream().filter(x -> x.getMonth() == selectedMonth.getMonth()).collect(Collectors.toList()));
        Collections.sort(transactionDateList);

        int maxYData = 0;
        for (int i = 0; i < dayCount; i++) {
            Date transactionDate = transactionDateList.get(i);
            ArrayList<Transaction> transactionList = GlobalData.transactionListGroupByDate.get(transactionDate);

            paint.setColor(ViewReportActivity.this.getResources().getColor(R.color.darkgray, ViewReportActivity.this.getTheme()));
            paint.setTextSize(28f);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawText(DateHelper.convertToSuperShortStringFromDate(transactionDate), dataXDistance * (i + 0.5f), bitmapHeight - 35, paint);

            int perDayOutAmount = 0;
            int perDayInAmount = 0;
            for (int j = 0; j < transactionList.size(); j++) {
                Transaction x = transactionList.get(j);

                if(x.IsOut) {
                    perDayOutAmount += x.Amount;
                } else {
                    perDayInAmount += x.Amount;
                }
            }

            int perDayCumulative = perDayOutAmount >= perDayInAmount ? perDayOutAmount - perDayInAmount : perDayInAmount - perDayOutAmount;
            if(perDayCumulative >= maxYData) {
                maxYData = perDayCumulative;
            }
        }

        for (int i = 0; i < dayCount; i++) {
            Date transactionDate = transactionDateList.get(i);
            ArrayList<Transaction> transactionList = GlobalData.transactionListGroupByDate.get(transactionDate);

            int perDayOutAmount = 0;
            int perDayInAmount = 0;
            for (int j = 0; j < transactionList.size(); j++) {
                Transaction x = transactionList.get(j);

                if(x.IsOut) {
                    perDayOutAmount += x.Amount;
                } else {
                    perDayInAmount += x.Amount;
                }
            }

            int perDayCumulative = 0;
            if(perDayOutAmount > perDayInAmount) {
                perDayCumulative = perDayOutAmount - perDayInAmount;
                paint.setColor(ViewReportActivity.this.getResources().getColor(R.color.red_type_out, ViewReportActivity.this.getTheme()));
            } else {
                perDayCumulative = perDayInAmount - perDayOutAmount;
                paint.setColor(ViewReportActivity.this.getResources().getColor(R.color.green_type_in, ViewReportActivity.this.getTheme()));
            }

            canvas.drawRoundRect(dataXDistance * (i + 0.5f) - (barChartWidth / 2f), bitmapHeight - xAxisLabelHeight - 20 - ((perDayCumulative / (float)maxYData) * (bitmapHeight - xAxisLabelHeight - 20)), dataXDistance * (i + 0.5f) + (barChartWidth / 2f), bitmapHeight - xAxisLabelHeight - 20, 8, 8, paint);
        }

        imgChart.setImageBitmap(bitmap);
    }
}