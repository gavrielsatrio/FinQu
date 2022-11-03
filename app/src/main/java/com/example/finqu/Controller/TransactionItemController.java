package com.example.finqu.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.finqu.Helper.NumberHelper;
import com.example.finqu.Helper.PaymentTypeHelper;
import com.example.finqu.Helper.TransactionTypeHelper;
import com.example.finqu.Model.Transaction;
import com.example.finqu.R;

public class TransactionItemController {
    public Boolean isEditDeleteMenuShown = false;
    private final View viewInflate;
    private Context context;
    private Transaction currentTransaction;

    private TextView lblName;
    private TextView lblPaymentType;
    private ImageView imgTransactionType;
    private TextView lblAmount;
    private ConstraintLayout constLayoutInfo;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnDetail;

    public TransactionItemController(Context contextParam, Transaction transactionParam) {
        this.context = contextParam;
        this.currentTransaction = transactionParam;
        this.viewInflate = LayoutInflater.from(contextParam).inflate(R.layout.transaction_item_layout, null, false);

        this.lblName = viewInflate.findViewById(R.id.transactionItemLblName);
        this.lblPaymentType = viewInflate.findViewById(R.id.transactionItemLblPaymentType);
        this.imgTransactionType = viewInflate.findViewById(R.id.transactionItemImgTransactionType);
        this.lblAmount = viewInflate.findViewById(R.id.transactionItemLblAmount);
        this.constLayoutInfo = viewInflate.findViewById(R.id.transactionItemConstLayoutInfo);
        this.btnEdit = viewInflate.findViewById(R.id.transactionItemBtnEdit);
        this.btnDelete = viewInflate.findViewById(R.id.transactionItemBtnDelete);
        this.btnDetail = viewInflate.findViewById(R.id.transactionItemBtnDetail);
    }

    public void setDataAndEventsToView() {
        lblName.setText(currentTransaction.Name);
        lblPaymentType.setText(currentTransaction.PaymentType);
        imgTransactionType.setImageDrawable(TransactionTypeHelper.getInstance(context).getPaymentIcon(currentTransaction.TransactionType));

        Integer amountView = currentTransaction.Amount;

        if(currentTransaction.IsIn) {
            lblAmount.setTextColor(context.getResources().getColor(R.color.green_type_in, context.getTheme()));
        } else {
            if(amountView > 0) {
                lblAmount.setTextColor(context.getResources().getColor(R.color.red_type_out, context.getTheme()));
                amountView *= -1;
            }
        }
        lblAmount.setText(NumberHelper.convertToRpFormat(amountView));

        constLayoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEditDeleteMenuShown) {
                    constLayoutInfo.animate().translationX(0).setDuration(400).setInterpolator(new AccelerateDecelerateInterpolator());
                } else {
                    constLayoutInfo.animate().translationX(-160).setDuration(400).setInterpolator(new AccelerateDecelerateInterpolator());
                }

                isEditDeleteMenuShown = !isEditDeleteMenuShown;
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View viewDialog = LayoutInflater.from(context).inflate(R.layout.transaction_item_detail_layout, null, false);
                builder.setView(viewDialog);

                ((TextView)viewDialog.findViewById(R.id.transactionItemDetailLblName)).setText(currentTransaction.Name);
                ((TextView)viewDialog.findViewById(R.id.transactionItemDetailLblPaidBy)).setText("Paid By : " + currentTransaction.PaidBy);
                ((ImageView)viewDialog.findViewById(R.id.transactionItemDetailImgPaymentType)).setImageDrawable(PaymentTypeHelper.getInstance(context).getPaymentIcon(currentTransaction.PaymentType));

                CardView cardViewStatus = viewDialog.findViewById(R.id.transactionItemDetailCardViewStatus);
                ImageView imgStatus = viewDialog.findViewById(R.id.transactionItemDetailImgStatus);
                if(currentTransaction.IsCheck) {
                    cardViewStatus.setCardBackgroundColor(context.getResources().getColor(R.color.green_type_in, context.getTheme()));
                    imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_round_check_24, context.getTheme()));
                } else {
                    cardViewStatus.setCardBackgroundColor(context.getResources().getColor(R.color.red_type_out, context.getTheme()));
                    imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_hourglass_bottom_24, context.getTheme()));
                }


                TextView lblAmount = viewDialog.findViewById(R.id.transactionItemDetailLblAmount);
                Integer amountView = currentTransaction.Amount;
                if(currentTransaction.IsIn) {
                    lblAmount.setTextColor(context.getResources().getColor(R.color.green_type_in, context.getTheme()));
                } else {
                    if(amountView > 0) {
                        lblAmount.setTextColor(context.getResources().getColor(R.color.red_type_out, context.getTheme()));
                        amountView *= -1;
                    }
                }
                lblAmount.setText(NumberHelper.convertToRpFormat(amountView));

                AlertDialog dialog = builder.create();
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        constLayoutInfo.performClick();
                    }
                });
            }
        });
    }

    public View getView() {
        return viewInflate;
    }
}
