package com.example.finqu.Controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.finqu.MainActivity;
import com.example.finqu.Model.Transaction;
import com.example.finqu.R;

public class TransactionItemController {
    public Boolean isEditDeleteMenuShown = false;
    private final View viewInflate;
    private MainActivity mainActivity;
    private Transaction currentTransaction;

    private TextView lblName;
    private TextView lblPaymentType;
    private ImageView imgTransactionType;
    private TextView lblAmount;
    private CardView cardViewContainer;
    private ConstraintLayout constLayoutInfo;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnDetail;

    public TransactionItemController(MainActivity mainActivityParam, Transaction transactionParam) {
        this.mainActivity = mainActivityParam;
        this.currentTransaction = transactionParam;
        this.viewInflate = LayoutInflater.from(mainActivityParam).inflate(R.layout.item_layout_transaction, null, false);

        this.lblName = viewInflate.findViewById(R.id.transactionLblName);
        this.lblPaymentType = viewInflate.findViewById(R.id.transactionLblPaymentType);
        this.imgTransactionType = viewInflate.findViewById(R.id.transactionImgTransactionType);
        this.lblAmount = viewInflate.findViewById(R.id.transactionLblAmount);
        this.cardViewContainer = viewInflate.findViewById(R.id.transactionCardView);
        this.constLayoutInfo = viewInflate.findViewById(R.id.transactionConstLayoutInfo);
        this.btnEdit = viewInflate.findViewById(R.id.transactionBtnEdit);
        this.btnDelete = viewInflate.findViewById(R.id.transactionBtnDelete);
        this.btnDetail = viewInflate.findViewById(R.id.transactionBtnDetail);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setDataAndEventsToView() {
        lblName.setText(currentTransaction.Name);
        lblPaymentType.setText(currentTransaction.PaymentType);
        imgTransactionType.setImageDrawable(TransactionTypeHelper.getInstance(mainActivity).getTransactionTypeIcon(currentTransaction.TransactionType));

        Integer amountView = currentTransaction.Amount;

        if(currentTransaction.IsIn) {
            lblAmount.setTextColor(mainActivity.getResources().getColor(R.color.green_type_in, mainActivity.getTheme()));
        } else {
            if(amountView > 0) {
                lblAmount.setTextColor(mainActivity.getResources().getColor(R.color.red_type_out, mainActivity.getTheme()));
                amountView *= -1;
            }
        }
        lblAmount.setText(NumberHelper.convertToRpFormat(amountView));

        constLayoutInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        cardViewContainer.animate().scaleX(0.97f).scaleY(0.97f).setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator());
                        break;
                    case MotionEvent.ACTION_UP:
                        cardViewContainer.animate().scaleX(1).scaleY(1).setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator());

                        if(!mainActivity.isFetchingNewData) {
                            if(isEditDeleteMenuShown) {
                                constLayoutInfo.animate().translationX(0).setDuration(400).setInterpolator(new AccelerateDecelerateInterpolator());
                            } else {
                                constLayoutInfo.animate().translationX(-160).setDuration(400).setInterpolator(new AccelerateDecelerateInterpolator());
                            }

                            isEditDeleteMenuShown = !isEditDeleteMenuShown;
                        }

                        break;
                    case MotionEvent.ACTION_CANCEL:
                        cardViewContainer.animate().scaleX(1).scaleY(1).setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator());
                        break;
                }

                return true;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

                View viewDialog = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_layout_transaction_detail, null, false);
                builder.setView(viewDialog);

                ((TextView)viewDialog.findViewById(R.id.transactionDetailLblName)).setText(currentTransaction.Name);
                ((TextView)viewDialog.findViewById(R.id.transactionDetailLblPaidBy)).setText("Paid By : " + currentTransaction.PaidBy);
                ((ImageView)viewDialog.findViewById(R.id.transactionDetailImgPaymentType)).setImageDrawable(PaymentTypeHelper.getInstance(mainActivity).getPaymentIcon(currentTransaction.PaymentType));

                CardView cardViewStatus = viewDialog.findViewById(R.id.transactionDetailCardViewStatus);
                ImageView imgStatus = viewDialog.findViewById(R.id.transactionDetailImgStatus);
                if(currentTransaction.IsCheck) {
                    cardViewStatus.setCardBackgroundColor(mainActivity.getResources().getColor(R.color.green_type_in, mainActivity.getTheme()));
                    imgStatus.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.ic_round_check_24, mainActivity.getTheme()));
                } else {
                    cardViewStatus.setCardBackgroundColor(mainActivity.getResources().getColor(R.color.red_type_out, mainActivity.getTheme()));
                    imgStatus.setImageDrawable(mainActivity.getResources().getDrawable(R.drawable.ic_baseline_hourglass_bottom_24, mainActivity.getTheme()));
                }


                TextView lblAmount = viewDialog.findViewById(R.id.transactionDetailLblAmount);
                Integer amountView = currentTransaction.Amount;
                if(currentTransaction.IsIn) {
                    lblAmount.setTextColor(mainActivity.getResources().getColor(R.color.green_type_in, mainActivity.getTheme()));
                } else {
                    if(amountView > 0) {
                        lblAmount.setTextColor(mainActivity.getResources().getColor(R.color.red_type_out, mainActivity.getTheme()));
                        amountView *= -1;
                    }
                }
                lblAmount.setText(NumberHelper.convertToRpFormat(amountView));

                AlertDialog dialog = builder.create();
                dialog.show();

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        constLayoutInfo.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis() + 100, MotionEvent.ACTION_UP, 0f, 0f, 0));
                    }
                });
            }
        });
    }

    public View getView() {
        return viewInflate;
    }
}
