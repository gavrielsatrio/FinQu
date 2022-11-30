package com.example.finqu.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
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
        return new ViewHolder(LayoutInflater.from(viewReportActivity).inflate(R.layout.animation_skeleton_transaction_summary_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionSummarySkeletonLoadAdapter.ViewHolder holder, int position) {
        View viewInflate = holder.itemView;

        ImageView shimmerImgLeft = viewInflate.findViewById(R.id.skeletonTransactionSummaryItemSkeletonShimmerTransactionTypeLeft);
        ImageView shimmerImgRight = viewInflate.findViewById(R.id.skeletonTransactionSummaryItemSkeletonShimmerTransactionTypeRight);
        ImageView shimmerNameLeft = viewInflate.findViewById(R.id.skeletonTransactionSummaryItemSkeletonShimmerNameLeft);
        ImageView shimmerNameRight = viewInflate.findViewById(R.id.skeletonTransactionSummaryItemSkeletonShimmerNameRight);
        ImageView shimmerAmountLeft = viewInflate.findViewById(R.id.skeletonTransactionSummaryItemSkeletonShimmerAmountLeft);
        ImageView shimmerAmountRight = viewInflate.findViewById(R.id.skeletonTransactionSummaryItemSkeletonShimmerAmountRight);

        CardView imgTransactionType = viewInflate.findViewById(R.id.skeletonTransactionSummaryItemSkeletonTransactionTypeContainer);
        TextView lblName = viewInflate.findViewById(R.id.skeletonTransactionSummaryItemLblName);
        TextView lblAmount = viewInflate.findViewById(R.id.skeletonTransactionSummaryItemLblAmount);

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
