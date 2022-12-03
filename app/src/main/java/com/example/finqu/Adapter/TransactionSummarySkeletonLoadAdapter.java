package com.example.finqu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finqu.Helper.SkeletonLoadingHelper;
import com.example.finqu.R;
import com.example.finqu.ViewReportActivity;

public class TransactionSummarySkeletonLoadAdapter extends RecyclerView.Adapter<TransactionSummarySkeletonLoadAdapter.ViewHolder> {
    private ViewReportActivity viewReportActivity;
    private int skeletonLoadItemCount;

    public TransactionSummarySkeletonLoadAdapter(ViewReportActivity viewReportActivityParam, int skeletonLoadItemCountParam) {
        this.viewReportActivity = viewReportActivityParam;
        this.skeletonLoadItemCount = skeletonLoadItemCountParam;
    }

    @NonNull
    @Override
    public TransactionSummarySkeletonLoadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewReportActivity).inflate(R.layout.skeleton_item_layout_transaction_summary, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionSummarySkeletonLoadAdapter.ViewHolder holder, int position) {
        View viewInflate = holder.itemView;

        ImageView shimmerImgLeft = viewInflate.findViewById(R.id.skeletonTransactionSummarySkeletonShimmerTransactionTypeLeft);
        ImageView shimmerImgRight = viewInflate.findViewById(R.id.skeletonTransactionSummarySkeletonShimmerTransactionTypeRight);
        ImageView shimmerNameLeft = viewInflate.findViewById(R.id.skeletonTransactionSummarySkeletonShimmerNameLeft);
        ImageView shimmerNameRight = viewInflate.findViewById(R.id.skeletonTransactionSummarySkeletonShimmerNameRight);
        ImageView shimmerAmountLeft = viewInflate.findViewById(R.id.skeletonTransactionSummarySkeletonShimmerAmountLeft);
        ImageView shimmerAmountRight = viewInflate.findViewById(R.id.skeletonTransactionSummarySkeletonShimmerAmountRight);

        CardView imgTransactionType = viewInflate.findViewById(R.id.skeletonTransactionSummarySkeletonTransactionTypeContainer);
        TextView lblName = viewInflate.findViewById(R.id.skeletonTransactionSummaryLblName);
        TextView lblAmount = viewInflate.findViewById(R.id.skeletonTransactionSummaryLblAmount);

        SkeletonLoadingHelper.StartSkeletonLoadingAnimation(imgTransactionType, shimmerImgLeft, shimmerImgRight, 1200);
        SkeletonLoadingHelper.StartSkeletonLoadingAnimation(lblName, shimmerNameLeft, shimmerNameRight, 1200);
        SkeletonLoadingHelper.StartSkeletonLoadingAnimation(lblAmount, shimmerAmountLeft, shimmerAmountRight, 1200);
    }

    @Override
    public int getItemCount() {
        return skeletonLoadItemCount;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
