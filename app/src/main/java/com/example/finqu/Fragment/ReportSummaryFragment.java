package com.example.finqu.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.finqu.Adapter.TransactionSummaryAdapter;
import com.example.finqu.Controller.TransactionTypePillController;
import com.example.finqu.Data.GlobalData;
import com.example.finqu.Dialog.LoadingDialog;
import com.example.finqu.Helper.DateHelper;
import com.example.finqu.Helper.NumberHelper;
import com.example.finqu.Model.Transaction;
import com.example.finqu.Model.TransactionTypeSummary;
import com.example.finqu.R;
import com.example.finqu.ViewReportActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReportSummaryFragment extends Fragment {
    private ViewReportActivity viewReportActivity;

    public List<String> shownTransactionTypeList = GlobalData.transactionTypeList.stream().filter(x -> !x.equals("Income") && !x.equals("Bank Transfer")).collect(Collectors.toList());
    public List<String> selectedTransactionTypeList = new ArrayList<>();

    private TextView lblTotalExpense;
    private RecyclerView recViewSummary;

    private boolean isLoading = false;

    public ReportSummaryFragment(ViewReportActivity viewReportActivityParam) {
        this.viewReportActivity = viewReportActivityParam;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_report_summary, container, false);

        lblTotalExpense = viewInflate.findViewById(R.id.reportSummaryLblTotalExpense);
        recViewSummary = viewInflate.findViewById(R.id.reportSummaryRecViewSummary);

        LinearLayout linearLayoutFilter = viewInflate.findViewById(R.id.reportSummaryLinearLayoutFilter);
        int transactionTypeCount = shownTransactionTypeList.size();
        int perRowTransactionTypePillCount = 3;

        int totalTransactionTypeRow = (transactionTypeCount / perRowTransactionTypePillCount) + 1;
        int transactionTypeIndex = 0;
        for (int i = 0; i < totalTransactionTypeRow; i++) {
            View viewTransactionTypeContainer = LayoutInflater.from(viewReportActivity).inflate(R.layout.transaction_type_item_container_layout, null, false);
            LinearLayout transactionTypeItemContainer = viewTransactionTypeContainer.findViewById(R.id.transactionTypeItemContainer);

            int j = 0;
            while(j < perRowTransactionTypePillCount && transactionTypeIndex < transactionTypeCount) {
                TransactionTypePillController transactionTypePill = new TransactionTypePillController(this, shownTransactionTypeList.get(transactionTypeIndex));
                transactionTypePill.setDataAndEventsToView();

                if(shownTransactionTypeList.get(transactionTypeIndex).equals("All")) {
                    transactionTypePill.clickPill();
                }

                View viewTransactionTypeItemPill = transactionTypePill.getView();

                transactionTypeItemContainer.addView(viewTransactionTypeItemPill);
                transactionTypeIndex++;
                j++;
            }

            linearLayoutFilter.addView(viewTransactionTypeContainer);
        }

        return viewInflate;
    }

    private int totalExpenses = 0;
    public void LoadData() {
        if(!isLoading) {
            LoadingDialog loadingDialog = new LoadingDialog(viewReportActivity);
            loadingDialog.ShowDialog("Filtering data");
            isLoading = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    totalExpenses = 0;

                    List<Transaction> transactionList = GlobalData.transactionList.stream().filter(x -> x.Date.getMonth() == DateHelper.getDateNow().getMonth() && selectedTransactionTypeList.stream().anyMatch(y -> y.equals(x.TransactionType))).collect(Collectors.toList());
                    transactionList.forEach(x ->
                    {
                        if(x.IsOut) {
                            totalExpenses += x.Amount;
                        } else {
                            totalExpenses -= x.Amount;
                        }
                    });

                    List<TransactionTypeSummary> transactionTypeSummaryList = new ArrayList<>();
                    selectedTransactionTypeList.forEach(x ->
                    {
                        transactionTypeSummaryList.add(new TransactionTypeSummary(x, GlobalData.transactionList.stream().filter(y -> y.Date.getMonth() == DateHelper.getDateNow().getMonth() && y.TransactionType.equals(x)).collect(Collectors.summingInt(y -> y.IsOut ? y.Amount : (-1 * y.Amount)))));
                    });

                    viewReportActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recViewSummary.setLayoutManager(new GridLayoutManager(viewReportActivity, 1, RecyclerView.VERTICAL, false));
                            recViewSummary.setAdapter(new TransactionSummaryAdapter(viewReportActivity, transactionTypeSummaryList));

                            lblTotalExpense.setText("Total : " + NumberHelper.convertToRpFormat(totalExpenses));

                            loadingDialog.DismissDialog();
                            isLoading = false;
                        }
                    });
                }
            }).start();
        }
    }
}
