package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.finqu.Fragment.ReportChartFragment;
import com.example.finqu.Fragment.ReportTransactionOfTheMonthFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Hashtable;

public class ViewReportActivity extends AppCompatActivity {

    ImageView btnBack;
    TabLayout tabLayout;

    Hashtable<String, Fragment> reportFragmentList = new Hashtable<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        btnBack = findViewById(R.id.viewReportBtnBack);
        tabLayout = findViewById(R.id.viewReportTabLayout);

        reportFragmentList.put("Chart", new ReportChartFragment(ViewReportActivity.this));
        reportFragmentList.put("Transaction Of The Month", new ReportTransactionOfTheMonthFragment(ViewReportActivity.this));

        LoadReportFragment("Chart");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewReportActivity.super.onBackPressed();
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LoadReportFragment(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                LoadReportFragment(tab.getText().toString());
            }
        });
    }

    private void LoadReportFragment(String reportType) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction trx = fragmentManager.beginTransaction();
        trx.replace(R.id.viewReportFrameLayout, reportFragmentList.get(reportType))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}