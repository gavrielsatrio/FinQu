package com.example.finqu.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finqu.Data.GlobalData;
import com.example.finqu.Dialog.LoadingDialog;
import com.example.finqu.Fragment.ReportSummaryFragment;
import com.example.finqu.Helper.TransactionTypeHelper;
import com.example.finqu.R;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TransactionTypePillController {
   private ReportSummaryFragment reportSummaryFragment;
   private View viewInflate;

   public String transactionType;
   public boolean isSelected = false;

   private CardView cardViewTransactionType;
   private ImageView imgTransactionType;
   private TextView lblTransactionType;

   public TransactionTypePillController(ReportSummaryFragment reportSummaryFragmentParam, String transactionTypeParam) {
      this.reportSummaryFragment = reportSummaryFragmentParam;
      this.viewInflate = LayoutInflater.from(reportSummaryFragment.getContext()).inflate(R.layout.transaction_type_item_pill_layout, null, false);
      this.transactionType = transactionTypeParam;

      this.cardViewTransactionType = viewInflate.findViewById(R.id.transactionTypeItemPillCardView);
      this.imgTransactionType = viewInflate.findViewById(R.id.transactionTypeItemPillImg);
      this.lblTransactionType = viewInflate.findViewById(R.id.transactionTypeItemPillLblName);
   }

   public void setDataAndEventsToView() {
      cardViewTransactionType.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            isSelected = !isSelected;

            changeColorState();
            changeSelectedState();

            reportSummaryFragment.LoadData();
         }
      });

      imgTransactionType.setImageDrawable(TransactionTypeHelper.getInstance(reportSummaryFragment.getContext()).getTransactionTypeIcon(transactionType));
      lblTransactionType.setText(transactionType);
   }

   public void clickPill() {
      cardViewTransactionType.performClick();
   }

   private void changeColorState() {
      if(isSelected) {
         cardViewTransactionType.setCardBackgroundColor(reportSummaryFragment.getResources().getColor(R.color.master_card_2, reportSummaryFragment.getContext().getTheme()));
         lblTransactionType.setTextColor(reportSummaryFragment.getResources().getColor(R.color.white, reportSummaryFragment.getContext().getTheme()));
      } else {
         cardViewTransactionType.setCardBackgroundColor(reportSummaryFragment.getResources().getColor(R.color.white, reportSummaryFragment.getContext().getTheme()));
         lblTransactionType.setTextColor(reportSummaryFragment.getResources().getColor(R.color.black, reportSummaryFragment.getContext().getTheme()));
      }
   }

   private void changeSelectedState() {
      if(isSelected) {
         if(transactionType.equals("All")) {
            reportSummaryFragment.selectedTransactionTypeList = reportSummaryFragment.shownTransactionTypeList.stream().filter(x -> !x.equals("All")).collect(Collectors.toList());

            reportSummaryFragment.transactionTypePillList.stream().filter(x -> !x.transactionType.equals("All")).forEach(x ->
            {
               x.isSelected = true;
               x.changeColorState();
            });
         } else {
            reportSummaryFragment.selectedTransactionTypeList.add(transactionType);
         }
      } else {
         if(transactionType.equals("All")) {
            reportSummaryFragment.selectedTransactionTypeList = new ArrayList<>();

            reportSummaryFragment.transactionTypePillList.stream().filter(x -> !x.transactionType.equals("All")).forEach(x ->
            {
               x.isSelected = false;
               x.changeColorState();
            });
         } else {
            reportSummaryFragment.selectedTransactionTypeList.remove(transactionType);
         }
      }
   }

   public View getView() {
      return viewInflate;
   }
}
