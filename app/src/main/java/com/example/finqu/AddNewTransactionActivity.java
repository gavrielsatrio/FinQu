package com.example.finqu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.finqu.Adapter.ComboBoxAdapter;
import com.example.finqu.Data.GlobalData;
import com.example.finqu.Dialog.LoadingDialog;
import com.example.finqu.Helper.DateHelper;
import com.example.finqu.Helper.NumberHelper;
import com.example.finqu.Model.Transaction;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class AddNewTransactionActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText txtTitle;
    EditText txtDate;
    Spinner comboTransactionType;
    RadioButton radioIn;
    RadioButton radioOut;
    EditText txtPaidBy;
    Spinner comboPaymentType;
    EditText txtAmount;
    CheckBox checkPaymentStatusDone;
    Button btnSave;
    Button btnCancel;

    private boolean isAddingNewData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_transaction);

        btnBack = findViewById(R.id.addNewTransactionBtnBack);
        txtTitle = findViewById(R.id.addNewTransactionTxtTitle);
        txtDate = findViewById(R.id.addNewTransactionTxtDate);
        comboTransactionType = findViewById(R.id.addNewTransactionComboTransactionType);
        radioIn = findViewById(R.id.addNewTransactionRadioIn);
        radioOut = findViewById(R.id.addNewTransactionRadioOut);
        txtPaidBy = findViewById(R.id.addNewTransactionTxtPaidBy);
        comboPaymentType = findViewById(R.id.addNewTransactionComboPaymentType);
        txtAmount = findViewById(R.id.addNewTransactionTxtAmount);
        checkPaymentStatusDone = findViewById(R.id.addNewTransactionCheckPaymentStatusDone);
        btnSave = findViewById(R.id.addNewTransactionBtnSave);
        btnCancel = findViewById(R.id.addNewTransactionBtnCancel);

        Calendar calendarDate = Calendar.getInstance();

        txtDate.setKeyListener(null);
        txtDate.setText(DateHelper.convertDateToString(calendarDate.getTime(), "EEEE, dd MMM yyyy"));
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddNewTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        calendarDate.set(Calendar.YEAR, year);
                        calendarDate.set(Calendar.MONTH, month);
                        calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        txtDate.setText(DateHelper.convertDateToString(calendarDate.getTime(), "EEEE, dd MMM yyyy"));
                    }
                },
                        calendarDate.get(Calendar.YEAR),
                        calendarDate.get(Calendar.MONTH),
                        calendarDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtAmount.addTextChangedListener(txtAmount_TextChanged);

        LoadComboTransactionType();
        LoadComboPaymentType();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTransactionActivity.super.onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtTitle.getText().toString().trim().equals("")) {
                    Snackbar.make(view, "Title is required", Snackbar.LENGTH_SHORT)
                            .setTextColor(AddNewTransactionActivity.this.getResources().getColor(R.color.white, AddNewTransactionActivity.this.getTheme()))
                            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();

                    return;
                }

                if(txtDate.getText().toString().trim().equals("")) {
                    Snackbar.make(view, "Date is required", Snackbar.LENGTH_SHORT)
                            .setTextColor(AddNewTransactionActivity.this.getResources().getColor(R.color.white, AddNewTransactionActivity.this.getTheme()))
                            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();

                    return;
                }

                if(txtPaidBy.getText().toString().trim().equals("")) {
                    Snackbar.make(view, "Paid by is required", Snackbar.LENGTH_SHORT)
                            .setTextColor(AddNewTransactionActivity.this.getResources().getColor(R.color.white, AddNewTransactionActivity.this.getTheme()))
                            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();

                    return;
                }

                if(txtAmount.getText().toString().trim().equals("")) {
                    Snackbar.make(view, "Amount is required", Snackbar.LENGTH_SHORT)
                            .setTextColor(AddNewTransactionActivity.this.getResources().getColor(R.color.white, AddNewTransactionActivity.this.getTheme()))
                            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show();

                    return;
                }

                String selectedDateMonthName = DateHelper.convertDateToString(calendarDate.getTime(), "MMMM yyyy");
                List<Transaction> transactionOnTheSelectedMonth = GlobalData.transactionList.stream().filter(x -> DateHelper.convertDateToString(x.Date, "MMMM yyyy").equals(selectedDateMonthName)).collect(Collectors.toList());
                List<Transaction> transactionOnTheSelectedDate = GlobalData.transactionList.stream().filter(x -> DateHelper.convertDateToString(x.Date, "MM-dd-yyyy").equals(DateHelper.convertDateToString(calendarDate.getTime(), "MM-dd-yyyy"))).collect(Collectors.toList());
                List<Transaction> transactionBeforeTheSelectedDate = GlobalData.transactionList.stream().filter(x -> x.Date.before(calendarDate.getTime()) && DateHelper.convertDateToString(x.Date, "MMMM yyyy").equals(selectedDateMonthName)).collect(Collectors.toList());
                List<Transaction> transactionAfterTheSelectedDate = GlobalData.transactionList.stream().filter(x -> x.Date.after(calendarDate.getTime()) && DateHelper.convertDateToString(x.Date, "MMMM yyyy").equals(selectedDateMonthName)).collect(Collectors.toList());

                int currentRowNo = 0;
                String insertPosition = "";
                if(transactionOnTheSelectedMonth.size() == 0) {
                    // First transaction of the month
                    currentRowNo = 5;
                    insertPosition = "onThisRow";
                } else {
                    if(transactionOnTheSelectedDate.size() > 0) {
                        currentRowNo = transactionOnTheSelectedDate.get(0).RowNoInExcel;
                        insertPosition = "afterThisRow";
                    } else {
                        if(transactionBeforeTheSelectedDate.size() > 0) {
                            currentRowNo = transactionBeforeTheSelectedDate.get(0).RowNoInExcel;
                            insertPosition = "afterThisRow";
                        } else {
                            currentRowNo = transactionAfterTheSelectedDate.get(transactionAfterTheSelectedDate.size() - 1).RowNoInExcel;
                            insertPosition = "beforeThisRow";
                        }
                    }
                }

                String postURL = "https://script.google.com/macros/s/AKfycbzAfyt3aN4ddAObcbG5yIE1ujsjTqUvt9EPmoI0YsjPvs78g-H98-OReYfJDa1YivQWlQ/exec?action=addNewDataToSheet&sheetName=" + selectedDateMonthName.replace(" ", "%20") + "&rowNo=" + currentRowNo + "&insertPosition=" + insertPosition;

                if(!isAddingNewData) {
                    isAddingNewData = true;
                    LoadingDialog loadingDialog = new LoadingDialog(AddNewTransactionActivity.this);
                    loadingDialog.ShowDialog("Adding new data ...");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new HttpRequest(postURL, "POST")
                                        .setRequestProperty("Content-Type", "application/json")
                                        .setBody(new JSONObject()
                                                .put("Date", DateHelper.convertDateToString(calendarDate.getTime(), "MM-dd-yyyy"))
                                                .put("DisplayDate", txtDate.getText().toString())
                                                .put("TransactionType", GlobalData.transactionTypeList.get(comboTransactionType.getSelectedItemPosition() + 1))
                                                .put("Title", txtTitle.getText().toString().trim())
                                                .put("IsIn", radioIn.isChecked())
                                                .put("IsOut", radioOut.isChecked())
                                                .put("PaidBy", txtPaidBy.getText().toString().trim())
                                                .put("PaymentType", GlobalData.paymentTypeList.get(comboPaymentType.getSelectedItemPosition()))
                                                .put("IsCheck", checkPaymentStatusDone.isChecked())
                                                .put("Amount", Double.valueOf(txtAmount.getText().toString().trim().replace(".", "")))
                                                .toString())
                                        .execute();

                                GlobalData.FetchNewData();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        isAddingNewData = false;
                                        loadingDialog.DismissDialog();

                                        Intent intent = new Intent(AddNewTransactionActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(intent);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTransactionActivity.super.onBackPressed();
            }
        });
    }

    private TextWatcher txtAmount_TextChanged = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            txtAmount.removeTextChangedListener(txtAmount_TextChanged);

            if(txtAmount.getText().toString().equals("")) {
                txtAmount.addTextChangedListener(txtAmount_TextChanged);
                return;
            }

            double amount = Double.parseDouble(txtAmount.getText().toString().replace(".", ""));
            txtAmount.setText(NumberHelper.convertToNumberFormat(amount, 0));
            txtAmount.setSelection(txtAmount.getText().toString().length());

            txtAmount.addTextChangedListener(txtAmount_TextChanged);
        }
    };

    private void LoadComboTransactionType() {
        comboTransactionType.setAdapter(new ComboBoxAdapter(AddNewTransactionActivity.this, GlobalData.transactionTypeList.stream().filter(x -> !x.equals("All")).collect(Collectors.toList())));
    }

    private void LoadComboPaymentType() {
        comboPaymentType.setAdapter(new ComboBoxAdapter(AddNewTransactionActivity.this, GlobalData.paymentTypeList));
    }
}