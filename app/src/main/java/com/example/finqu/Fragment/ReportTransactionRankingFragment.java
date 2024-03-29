package com.example.finqu.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finqu.Data.GlobalData;
import com.example.finqu.Helper.DateHelper;
import com.example.finqu.Helper.TransactionTypeHelper;
import com.example.finqu.Model.Transaction;
import com.example.finqu.Model.TransactionTypeSummary;
import com.example.finqu.ModifiedClass.ModifiedFragment;
import com.example.finqu.R;
import com.example.finqu.ViewReportActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportTransactionRankingFragment extends ModifiedFragment {
    ViewReportActivity viewReportActivity;

    public ReportTransactionRankingFragment(ViewReportActivity viewReportActivityParam) {
        this.viewReportActivity = viewReportActivityParam;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_report_transaction_ranking, container, false);

        Map<String, List<Transaction>> transactionList = GlobalData.transactionList.stream().filter(x -> x.Date.getMonth() == DateHelper.getDateNow().getMonth() && x.Date.getYear() == DateHelper.getDateNow().getYear() && !x.TransactionType.equals("Income") && !x.TransactionType.equals("Bank Transfer"))
                .collect(Collectors.groupingBy(x -> x.TransactionType));

        List<TransactionTypeSummary> transactionTypeSum = new ArrayList<>();
        transactionList.forEach((key, value) ->
        {
            Integer sum = value.stream().collect(Collectors.summingInt(x -> x.IsOut ? x.Amount : x.Amount * -1));
            transactionTypeSum.add(new TransactionTypeSummary(key, sum));
        });

        transactionTypeSum.sort(new Comparator<TransactionTypeSummary>() {
            @Override
            public int compare(TransactionTypeSummary t0, TransactionTypeSummary t1) {
                return t1.SumAmount.compareTo(t0.SumAmount);
            }
        });

        ImageView[] imageViewPodiumList = new ImageView[]
        {
                viewInflate.findViewById(R.id.reportTransactionRankingPodiumFirst),
                viewInflate.findViewById(R.id.reportTransactionRankingPodiumSecond),
                viewInflate.findViewById(R.id.reportTransactionRankingPodiumThird)
        };

        TextView[] lblRankList = new TextView[]
        {
                viewInflate.findViewById(R.id.reportTransactionRankingLblFirst),
                viewInflate.findViewById(R.id.reportTransactionRankingLblSecond),
                viewInflate.findViewById(R.id.reportTransactionRankingLblThird)
        };

        TextView[] lblValueList = new TextView[]
        {
                viewInflate.findViewById(R.id.reportTransactionRankingLblFirstValue),
                viewInflate.findViewById(R.id.reportTransactionRankingLblSecondValue),
                viewInflate.findViewById(R.id.reportTransactionRankingLblThirdValue)
        };

        ImageView[] imageViewTransactionTypeList = new ImageView[]
        {
                viewInflate.findViewById(R.id.reportTransactionRankingImgFirst),
                viewInflate.findViewById(R.id.reportTransactionRankingImgSecond),
                viewInflate.findViewById(R.id.reportTransactionRankingImgThird)
        };

        for (int i = imageViewTransactionTypeList.length - 1; i >= 0; i--) {
            if(i <= transactionTypeSum.size() - 1) {
                String transactionType = transactionTypeSum.get(i).TransactionType;

                imageViewPodiumList[i].animate().setStartDelay(100 * (imageViewTransactionTypeList.length - i)).setDuration(400).translationY(0f).alpha(1).setInterpolator(new AccelerateDecelerateInterpolator());

                lblRankList[i].animate().setStartDelay(100 * (imageViewTransactionTypeList.length - i)).setDuration(400).translationY(0f).alpha(1).setInterpolator(new AccelerateDecelerateInterpolator());

                lblValueList[i].setText(transactionType);
                lblValueList[i].animate().setStartDelay(200 * (imageViewTransactionTypeList.length - i)).setDuration(400).translationY(0f).alpha(1).setInterpolator(new AccelerateDecelerateInterpolator());

                ImageView imgTransactionType = imageViewTransactionTypeList[i];

                imgTransactionType.setImageDrawable(TransactionTypeHelper.getInstance(viewReportActivity).getTransactionTypeIcon(transactionType));
                imgTransactionType.animate().setStartDelay(300 * (imageViewTransactionTypeList.length - i)).setDuration(400).translationY(0f).alpha(1).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        AnimateBouncingImage(imgTransactionType);
                    }
                });
            }
        }

        return viewInflate;
    }

    private void AnimateBouncingImage(ImageView img) {
        img.animate().setStartDelay(0).rotationY(360).translationY(-32).setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                img.animate().setStartDelay(0).rotationY(0).translationY(-32).setDuration(250).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        img.animate().setStartDelay(0).rotationY(0).translationY(0).setDuration(800).setInterpolator(new BounceInterpolator()).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                img.animate().setStartDelay(0).rotationY(0).translationY(0).setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        AnimateBouncingImage(img);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
