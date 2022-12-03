package com.example.finqu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finqu.Helper.SkeletonLoadingHelper;
import com.example.finqu.MainActivity;
import com.example.finqu.R;
import com.example.finqu.ViewReportActivity;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionSkeletonLoadAdapter extends RecyclerView.Adapter<TransactionSkeletonLoadAdapter.ViewHolder> {
    private MainActivity mainActivity;
    private int skeletonLoadItemCount;

    public TransactionSkeletonLoadAdapter(MainActivity mainActivityParam, int skeletonLoadItemCountParam) {
        this.mainActivity = mainActivityParam;
        this.skeletonLoadItemCount = skeletonLoadItemCountParam;
    }

    @NonNull
    @Override
    public TransactionSkeletonLoadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mainActivity).inflate(R.layout.skeleton_item_layout_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionSkeletonLoadAdapter.ViewHolder holder, int position) {
        View viewInflate = holder.itemView;

        ImageView shimmerImgLeft = viewInflate.findViewById(R.id.skeletonTransactionSkeletonShimmerTransactionTypeLeft);
        ImageView shimmerImgRight = viewInflate.findViewById(R.id.skeletonTransactionSkeletonShimmerTransactionTypeRight);
        ImageView shimmerNameLeft = viewInflate.findViewById(R.id.skeletonTransactionSkeletonShimmerNameLeft);
        ImageView shimmerNameRight = viewInflate.findViewById(R.id.skeletonTransactionSkeletonShimmerNameRight);
        ImageView shimmerPaymentTypeLeft = viewInflate.findViewById(R.id.skeletonTransactionSkeletonShimmerPaymentTypeLeft);
        ImageView shimmerPaymentTypeRight = viewInflate.findViewById(R.id.skeletonTransactionSkeletonShimmerPaymentTypeRight);
        ImageView shimmerAmountLeft = viewInflate.findViewById(R.id.skeletonTransactionSkeletonShimmerAmountLeft);
        ImageView shimmerAmountRight = viewInflate.findViewById(R.id.skeletonTransactionSkeletonShimmerAmountRight);

        CardView imgTransactionType = viewInflate.findViewById(R.id.skeletonTransactionSkeletonTransactionTypeContainer);
        TextView lblName = viewInflate.findViewById(R.id.skeletonTransactionLblName);
        TextView lblPaymentType = viewInflate.findViewById(R.id.skeletonTransactionLblPaymentType);
        TextView lblAmount = viewInflate.findViewById(R.id.skeletonTransactionLblAmount);

        SkeletonLoadingHelper.StartSkeletonLoadingAnimation(imgTransactionType, shimmerImgLeft, shimmerImgRight, 1200);
        SkeletonLoadingHelper.StartSkeletonLoadingAnimation(lblName, shimmerNameLeft, shimmerNameRight, 1200);
        SkeletonLoadingHelper.StartSkeletonLoadingAnimation(lblPaymentType, shimmerPaymentTypeLeft, shimmerPaymentTypeRight, 1200);
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
