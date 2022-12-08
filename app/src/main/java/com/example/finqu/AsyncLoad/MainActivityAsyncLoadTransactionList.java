package com.example.finqu.AsyncLoad;

import android.os.*;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finqu.Controller.TransactionController;
import com.example.finqu.Helper.DateHelper;
import com.example.finqu.Helper.NumberHelper;
import com.example.finqu.MainActivity;
import com.example.finqu.Model.Transaction;
import com.example.finqu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivityAsyncLoadTransactionList extends AsyncTask<Void, Void, Void>{
    MainActivity mainActivity;
    List<Transaction> transactionList;
    LinearLayout linearLayoutTransaction;

    public MainActivityAsyncLoadTransactionList(MainActivity mainActivityParam, List<Transaction> transactionListParam, LinearLayout linearLayoutTransactionParam) {
        this.mainActivity = mainActivityParam;
        this.transactionList = transactionListParam;
        this.linearLayoutTransaction = linearLayoutTransactionParam;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        transactionList.forEach(x ->
        {
            if(!mainActivity.currentDateOnList.equals(DateHelper.convertToStringFromDate(x.Date))) {
                mainActivity.currentDateOnList = DateHelper.convertToStringFromDate(x.Date);

                View transactionDateView = LayoutInflater.from(mainActivity).inflate(R.layout.item_layout_transaction_date, null, false);

                if(x.Date.equals(transactionList.get(0).Date)) {
                    FloatingActionButton btnRefreshOnItemDate = transactionDateView.findViewById(R.id.transactionDateBtnRefresh);
                    btnRefreshOnItemDate.setVisibility(View.VISIBLE);
                    btnRefreshOnItemDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mainActivity.RefreshData();
                        }
                    });
                }

                ((TextView)transactionDateView.findViewById(R.id.transactionDateLbl)).setText(mainActivity.currentDateOnList);

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutTransaction.addView(transactionDateView);
                    }
                });
            }

            int calculatedAmount = !x.TransactionType.equals("Income") ? (x.IsOut ? x.Amount : x.Amount * -1) : 0;

            mainActivity.totalSelectedRangeExpense += calculatedAmount;

            TransactionController transactionItem = new TransactionController(mainActivity, x);
            transactionItem.setDataAndEventsToView();

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayoutTransaction.addView(transactionItem.getView());
                }
            });
        });

        if(transactionList.size() > 0) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 8, 0, 8);

            TextView lblSelectedRangeExpense = new TextView(mainActivity);
            lblSelectedRangeExpense.setText("Selected range total expenses : " + NumberHelper.convertToRpFormat(mainActivity.totalSelectedRangeExpense));
            lblSelectedRangeExpense.setLayoutParams(layoutParams);
            lblSelectedRangeExpense.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            lblSelectedRangeExpense.setTypeface(mainActivity.getResources().getFont(R.font.sf_pro_display_medium));

            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayoutTransaction.addView(lblSelectedRangeExpense);
                }
            });
        }

        return null;
    }
}
