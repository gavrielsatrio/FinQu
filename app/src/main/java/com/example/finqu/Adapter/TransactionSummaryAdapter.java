package com.example.finqu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finqu.Helper.NumberHelper;
import com.example.finqu.Helper.TransactionTypeHelper;
import com.example.finqu.Model.TransactionTypeSummary;
import com.example.finqu.R;
import com.example.finqu.ViewReportActivity;

import java.util.List;

public class TransactionSummaryAdapter extends RecyclerView.Adapter<TransactionSummaryAdapter.ViewHolder> {
    public ViewReportActivity viewReportActivity;
    public List<TransactionTypeSummary> transactionTypeSummaryList;

    public TransactionSummaryAdapter(ViewReportActivity viewReportActivityParam, List<TransactionTypeSummary> transactionTypeSummaryListParam) {
        this.viewReportActivity = viewReportActivityParam;
        this.transactionTypeSummaryList = transactionTypeSummaryListParam;
    }

    @NonNull
    @Override
    public TransactionSummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewReportActivity).inflate(R.layout.item_layout_transaction_summary, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionSummaryAdapter.ViewHolder holder, int position) {
        View viewInflate = holder.itemView;

        TransactionTypeSummary transactionTypeSummaryItem = transactionTypeSummaryList.get(position);

        ((TextView)viewInflate.findViewById(R.id.transactionSummaryLblName)).setText(transactionTypeSummaryItem.TransactionType);
        ((TextView)viewInflate.findViewById(R.id.transactionSummaryLblAmount)).setText(NumberHelper.convertToRpFormat(transactionTypeSummaryItem.SumAmount));
        ((ImageView)viewInflate.findViewById(R.id.transactionSummaryImgTransactionType)).setImageDrawable(TransactionTypeHelper.getInstance(viewReportActivity).getTransactionTypeIcon(transactionTypeSummaryItem.TransactionType));;
    }

    @Override
    public int getItemCount() {
        return transactionTypeSummaryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
