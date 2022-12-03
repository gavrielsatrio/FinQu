package com.example.finqu.Dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.finqu.R;

import androidx.appcompat.app.AlertDialog;

public class LoadingDialog {
    Context context;
    private AlertDialog dialog;

    public LoadingDialog(Context contextParam) {
        this.context = contextParam;
    }

    public void ShowDialog(String descText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_layout_loading, null, false);
        builder.setView(viewDialog);
        builder.setCancelable(false);

        dialog = builder.create();

        ((TextView)viewDialog.findViewById(R.id.loadingLblDesc)).setText(descText);

        dialog.show();
    }

    public void DismissDialog() {
        dialog.dismiss();
    }
}
