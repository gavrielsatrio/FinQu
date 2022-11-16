package com.example.finqu.Fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finqu.Adapter.ComboBoxAdapter;
import com.example.finqu.Data.GlobalData;
import com.example.finqu.Helper.DateHelper;
import com.example.finqu.Helper.NumberHelper;
import com.example.finqu.Model.Transaction;
import com.example.finqu.R;
import com.example.finqu.ViewReportActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReportChartFragment extends Fragment {
    ViewReportActivity viewReportActivity;

    public ReportChartFragment(ViewReportActivity viewReportActivityParam) {
        this.viewReportActivity = viewReportActivityParam;
    }

    Spinner comboBoxMonth;
    ImageView imgChart;
    TextView lblTotal;

    Date selectedMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_report_chart, container, false);

        comboBoxMonth = viewInflate.findViewById(R.id.reportChartComboMonth);
        imgChart = viewInflate.findViewById(R.id.reportChartImgChart);
        lblTotal = viewInflate.findViewById(R.id.reportChartLblTotalExpense);

        imgChart.setImageBitmap(null);

        LoadComboMonth();

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

        return viewInflate;
    }

    private void LoadComboMonth() {
        comboBoxMonth.setAdapter(new ComboBoxAdapter(viewReportActivity, GlobalData.sheetList));

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
        int barChartWidth = 70;
        float halfBarChartWidth = barChartWidth / 2f;

        int dataXDistance = barChartWidth + 80;

        int xAxisLabelHeight = 90;
        int xAxisLabelMarginBottom = 35;
        float xAxisLabelFontSize = 28f;

        int bitmapHeight = 1000;
        int bitmapTopBottomPadding = 20;
        int chartHeight = bitmapHeight - xAxisLabelHeight - bitmapTopBottomPadding;


        Bitmap bitmap = Bitmap.createBitmap(dayCount * dataXDistance, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(viewReportActivity.getResources().getColor(R.color.lightgray, viewReportActivity.getTheme()));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, bitmapHeight - xAxisLabelHeight, dayCount * dataXDistance, bitmapHeight, paint);

        List<Date> transactionDateList = new ArrayList<>(GlobalData.transactionListGroupByDate.keySet().stream().filter(x -> x.getMonth() == selectedMonth.getMonth()).collect(Collectors.toList()));
        Collections.sort(transactionDateList);

        double maxYData = 0;
        for (int i = 0; i < dayCount; i++) {
            Date transactionDate = transactionDateList.get(i);
            List<Transaction> transactionList = GlobalData.transactionListGroupByDate.get(transactionDate);

            paint.setColor(viewReportActivity.getResources().getColor(R.color.darkgray, viewReportActivity.getTheme()));
            paint.setTextSize(xAxisLabelFontSize);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setStyle(Paint.Style.FILL);

            canvas.drawText(DateHelper.convertToSuperShortStringFromDate(transactionDate), dataXDistance * (i + 0.5f), bitmapHeight - xAxisLabelMarginBottom, paint);

            double perDayOutAmount = 0;
            double perDayInAmount = 0;
            for (int j = 0; j < transactionList.size(); j++) {
                Transaction x = transactionList.get(j);

                if(x.IsOut) {
                    perDayOutAmount += x.Amount;
                } else {
                    perDayInAmount += x.Amount;
                }
            }

            if(perDayOutAmount > perDayInAmount) {
                if(perDayOutAmount > maxYData) {
                    maxYData = perDayOutAmount;
                }
            } else {
                if(perDayInAmount > maxYData) {
                    maxYData = perDayInAmount;
                }
            }

//            int perDayCumulative = perDayOutAmount >= perDayInAmount ? perDayOutAmount - perDayInAmount : perDayInAmount - perDayOutAmount;
//            if(perDayCumulative >= maxYData) {
//                maxYData = perDayCumulative;
//            }
        }

        for (int i = 0; i < dayCount; i++) {
            Date transactionDate = transactionDateList.get(i);
            List<Transaction> transactionList = GlobalData.transactionListGroupByDate.get(transactionDate);

            double perDayOutAmount = 0;
            double perDayInAmount = 0;
            for (int j = 0; j < transactionList.size(); j++) {
                Transaction x = transactionList.get(j);

                if(x.IsOut) {
                    perDayOutAmount += x.Amount;
                } else {
                    perDayInAmount += x.Amount;
                }
            }

            float startXChartCanvas = dataXDistance * (i + 0.5f) - halfBarChartWidth;
            float endXChartCanvas = dataXDistance * (i + 0.5f) + halfBarChartWidth;

            float outBarChartHeight = ((float)perDayOutAmount / (float)maxYData) * chartHeight;
            float inBarChartHeight = ((float)perDayInAmount / (float)maxYData) * chartHeight;

            paint.setColor(viewReportActivity.getResources().getColor(R.color.red_type_out, viewReportActivity.getTheme()));
            canvas.drawRoundRect(startXChartCanvas, chartHeight - outBarChartHeight, endXChartCanvas - halfBarChartWidth, chartHeight, 8, 8, paint);

            paint.setColor(viewReportActivity.getResources().getColor(R.color.green_type_in, viewReportActivity.getTheme()));
            canvas.drawRoundRect(startXChartCanvas + halfBarChartWidth, chartHeight - inBarChartHeight, endXChartCanvas, chartHeight, 8, 8, paint);

//            int perDayCumulative = 0;
//            if(perDayOutAmount > perDayInAmount) {
//                perDayCumulative = perDayOutAmount - perDayInAmount;
//                paint.setColor(viewReportActivity.getResources().getColor(R.color.red_type_out, viewReportActivity.getTheme()));
//            } else {
//                perDayCumulative = perDayInAmount - perDayOutAmount;
//                paint.setColor(viewReportActivity.getResources().getColor(R.color.green_type_in, viewReportActivity.getTheme()));
//            }
//
//            canvas.drawRoundRect(dataXDistance * (i + 0.5f) - (barChartWidth / 2f), bitmapHeight - xAxisLabelHeight - bitmapTopBottomPadding - ((perDayCumulative / (float)maxYData) * (bitmapHeight - xAxisLabelHeight - bitmapTopBottomPadding)), dataXDistance * (i + 0.5f) + (barChartWidth / 2f), bitmapHeight - xAxisLabelHeight - bitmapTopBottomPadding, 8, 8, paint);
        }

        imgChart.setImageBitmap(bitmap);
    }
}
