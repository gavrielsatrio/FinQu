package com.example.finqu.Fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
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

        comboBoxMonth = viewInflate.findViewById(R.id.viewReportComboMonth);
        imgChart = viewInflate.findViewById(R.id.viewReportImgChart);
        lblTotal = viewInflate.findViewById(R.id.viewReportLblTotalExpense);

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
        int dataXDistance = 130;
        int barChartWidth = 50;
        Integer bitmapHeight = 1000;
        Integer xAxisLabelHeight = 90;

        Bitmap bitmap = Bitmap.createBitmap(dayCount * dataXDistance, bitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(viewReportActivity.getResources().getColor(R.color.lightgray, viewReportActivity.getTheme()));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, bitmapHeight - xAxisLabelHeight, dayCount * dataXDistance, bitmapHeight, paint);

        List<Date> transactionDateList = new ArrayList<>(GlobalData.transactionListGroupByDate.keySet().stream().filter(x -> x.getMonth() == selectedMonth.getMonth()).collect(Collectors.toList()));
        Collections.sort(transactionDateList);

        int maxYData = 0;
        for (int i = 0; i < dayCount; i++) {
            Date transactionDate = transactionDateList.get(i);
            ArrayList<Transaction> transactionList = GlobalData.transactionListGroupByDate.get(transactionDate);

            paint.setColor(viewReportActivity.getResources().getColor(R.color.darkgray, viewReportActivity.getTheme()));
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
                paint.setColor(viewReportActivity.getResources().getColor(R.color.red_type_out, viewReportActivity.getTheme()));
            } else {
                perDayCumulative = perDayInAmount - perDayOutAmount;
                paint.setColor(viewReportActivity.getResources().getColor(R.color.green_type_in, viewReportActivity.getTheme()));
            }

            canvas.drawRoundRect(dataXDistance * (i + 0.5f) - (barChartWidth / 2f), bitmapHeight - xAxisLabelHeight - 20 - ((perDayCumulative / (float)maxYData) * (bitmapHeight - xAxisLabelHeight - 20)), dataXDistance * (i + 0.5f) + (barChartWidth / 2f), bitmapHeight - xAxisLabelHeight - 20, 8, 8, paint);
        }

        imgChart.setImageBitmap(bitmap);
    }
}
