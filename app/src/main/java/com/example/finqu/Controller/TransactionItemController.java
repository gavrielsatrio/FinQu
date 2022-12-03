package com.example.finqu.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        this.viewInflate = LayoutInflater.from(contextParam).inflate(R.layout.item_layout_transaction, null, false);

        this.lblName = viewInflate.findViewById(R.id.transactionLblName);
        this.lblPaymentType = viewInflate.findViewById(R.id.transactionLblPaymentType);
        this.imgTransactionType = viewInflate.findViewById(R.id.transactionImgTransactionType);
        this.lblAmount = viewInflate.findViewById(R.id.transactionLblAmount);
        this.constLayoutInfo = viewInflate.findViewById(R.id.transactionConstLayoutInfo);
        this.btnEdit = viewInflate.findViewById(R.id.transactionBtnEdit);
        this.btnDelete = viewInflate.findViewById(R.id.transactionBtnDelete);
        this.btnDetail = viewInflate.findViewById(R.id.transactionBtnDetail);
    }

    public void setDataAndEventsToView() {
        lblName.setText(currentTransaction.Name);
        lblPaymentType.setText(currentTransaction.PaymentType);
        imgTransactionType.setImageDrawable(TransactionTypeHelper.getInstance(context).getTransactionTypeIcon(currentTransaction.TransactionType));

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

                View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_layout_transaction_detail, null, false);
                builder.setView(viewDialog);

                ((TextView)viewDialog.findViewById(R.id.transactionDetailLblName)).setText(currentTransaction.Name);
                ((TextView)viewDialog.findViewById(R.id.transactionDetailLblPaidBy)).setText("Paid By : " + currentTransaction.PaidBy);
                ((ImageView)viewDialog.findViewById(R.id.transactionDetailImgPaymentType)).setImageDrawable(PaymentTypeHelper.getInstance(context).getPaymentIcon(currentTransaction.PaymentType));

                CardView cardViewStatus = viewDialog.findViewById(R.id.transactionDetailCardViewStatus);
                ImageView imgStatus = viewDialog.findViewById(R.id.transactionDetailImgStatus);
                if(currentTransaction.IsCheck) {
                    cardViewStatus.setCardBackgroundColor(context.getResources().getColor(R.color.green_type_in, context.getTheme()));
                    imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_round_check_24, context.getTheme()));
                } else {
                    cardViewStatus.setCardBackgroundColor(context.getResources().getColor(R.color.red_type_out, context.getTheme()));
                    imgStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_hourglass_bottom_24, context.getTheme()));
                }


                TextView lblAmount = viewDialog.findViewById(R.id.transactionDetailLblAmount);
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
