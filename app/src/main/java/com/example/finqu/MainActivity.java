package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finqu.Adapter.TransactionSkeletonLoadAdapter;
import com.example.finqu.Controller.TransactionController;
import com.example.finqu.Data.GlobalData;
import com.example.finqu.Dialog.LoadingDialog;
import com.example.finqu.Helper.DateHelper;
import com.example.finqu.Helper.NumberHelper;
import com.example.finqu.Model.Transaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    TextView lblTodayExpense;
    LinearLayout linearLayoutTransaction;
    FloatingActionButton btnAdd;
    TextView lblViewReport;
    ImageView imgViewReportArrow;
    EditText txtStartDate;
    EditText txtEndDate;
    ImageView btnSetting;
    FloatingActionButton btnRefresh;
    RecyclerView recViewSkeleton;

    private String dateState = "start";
    public boolean isFetchingNewData = false;

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
        btnRefresh = findViewById(R.id.mainBtnRefresh);
        recViewSkeleton = findViewById(R.id.mainRecViewSkeleton);

        txtStartDate.setKeyListener(null);
        txtEndDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance();
        txtStartDate.setText(DateHelper.convertToShortStringFromDate(calendar.getTime()));
        txtEndDate.setText(DateHelper.convertToShortStringFromDate(calendar.getTime()));

        LoadTransactionList();

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

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefreshData();
            }
        });
    }

    String currentDateOnList = "";
    Integer totalTodayExpense = 0;
    Integer totalSelectedRangeExpense = 0;

    private void RefreshData() {
        if(!isFetchingNewData) {
            isFetchingNewData = true;
            LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
            loadingDialog.ShowDialog("Fetching new data ...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    GlobalData.FetchNewData();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isFetchingNewData = false;
                            loadingDialog.DismissDialog();

                            LoadTransactionList();
                        }
                    });
                }
            }).start();
        }
    }

    private void LoadTransactionList() {
        totalSelectedRangeExpense = 0;
        totalTodayExpense = 0;
        currentDateOnList = "";

        linearLayoutTransaction.removeAllViews();

        Date startDate = DateHelper.convertToDateFromShortString(txtStartDate.getText().toString());
        Date endDate = DateHelper.convertToDateFromShortString(txtEndDate.getText().toString());

        linearLayoutTransaction.setVisibility(View.INVISIBLE);
        recViewSkeleton.setVisibility(View.VISIBLE);

        recViewSkeleton.setLayoutManager(new GridLayoutManager(MainActivity.this, 1, RecyclerView.VERTICAL, false));
        recViewSkeleton.setAdapter(new TransactionSkeletonLoadAdapter(MainActivity.this, 5));

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Transaction> query = GlobalData.transactionList.stream().filter(x -> (x.Date.after(startDate) && x.Date.before(endDate)) || x.Date.equals(startDate) || x.Date.equals(endDate)).collect(Collectors.toList());
                query.forEach(x ->
                {
                    if(!currentDateOnList.equals(DateHelper.convertToStringFromDate(x.Date))) {
                        currentDateOnList = DateHelper.convertToStringFromDate(x.Date);

                        View transactionDateView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_layout_transaction_date, null, false);

                        if(x.Date.equals(query.get(0).Date)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    FloatingActionButton btnRefreshOnItemDate = transactionDateView.findViewById(R.id.transactionDateBtnRefresh);
                                    btnRefreshOnItemDate.setVisibility(View.VISIBLE);
                                    btnRefreshOnItemDate.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            RefreshData();
                                        }
                                    });
                                }
                            });
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView)transactionDateView.findViewById(R.id.transactionDateLbl)).setText(currentDateOnList);
                                linearLayoutTransaction.addView(transactionDateView);
                            }
                        });
                    }

                    int calculatedAmount = x.IsOut ? x.Amount : x.Amount * -1;

                    totalSelectedRangeExpense += calculatedAmount;
                    if(x.Date.equals(DateHelper.getDateNow())) {
                        totalTodayExpense += calculatedAmount;
                    }

                    TransactionController transactionItem = new TransactionController(MainActivity.this, x);
                    transactionItem.setDataAndEventsToView();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            linearLayoutTransaction.addView(transactionItem.getView());
                        }
                    });
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lblTodayExpense.setText(NumberHelper.convertToRpFormat(totalTodayExpense));
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(linearLayoutTransaction.getChildCount() == 0) {
                            btnRefresh.setVisibility(View.VISIBLE);
                        } else {
                            btnRefresh.setVisibility(View.INVISIBLE);

                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(0, 8, 0, 8);

                            TextView lblSelectedRangeExpense = new TextView(MainActivity.this);
                            lblSelectedRangeExpense.setText("Selected range total expenses : " + NumberHelper.convertToRpFormat(totalSelectedRangeExpense));
                            lblSelectedRangeExpense.setLayoutParams(layoutParams);
                            lblSelectedRangeExpense.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            lblSelectedRangeExpense.setTypeface(MainActivity.this.getResources().getFont(R.font.sf_pro_display_medium));

                            linearLayoutTransaction.addView(lblSelectedRangeExpense);
                        }
                    }
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutTransaction.setVisibility(View.VISIBLE);
                        recViewSkeleton.setAdapter(null);
                        recViewSkeleton.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }
}