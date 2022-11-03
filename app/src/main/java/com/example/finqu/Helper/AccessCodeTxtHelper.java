package com.example.finqu.Helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AccessCodeTxtHelper {
    private EditText[] txtCodes;

    private int currentTxtIndex = 0;
    private EditText currentTxt;

    private TextWatcher txtCodeTextWather = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            MoveToNextOrPrevTxt();
        }
    };

    private View.OnFocusChangeListener txtCodeFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean isFocused) {
            if(isFocused) {
                currentTxtIndex = Arrays.stream(txtCodes).collect(Collectors.toList()).indexOf((EditText)view);
                currentTxt = txtCodes[currentTxtIndex];
            }
        }
    };

    private View.OnKeyListener txtCodeKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if(keyCode == 67 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                MoveToNextOrPrevTxt();
            }

            return false;
        }
    };

    private void MoveToNextOrPrevTxt() {
        if(!currentTxt.getText().toString().equals("")) {
            if(currentTxtIndex == txtCodes.length - 1) {
                return;
            }

            currentTxtIndex++;
        } else {
            if(currentTxtIndex == 0) {
                return;
            }

            currentTxtIndex--;
        }

        EditText theFollowingTxt = txtCodes[currentTxtIndex];
        theFollowingTxt.requestFocus();
    }

    public void BindAllListener(EditText[] txtCodesParam) {
        this.txtCodes = txtCodesParam;
        this.currentTxtIndex = 0;
        this.currentTxt = txtCodes[currentTxtIndex];

        for (int i = 0; i < txtCodes.length; i++) {
            EditText txt = txtCodes[i];

            txt.addTextChangedListener(txtCodeTextWather);
            txt.setOnFocusChangeListener(txtCodeFocusListener);
            txt.setOnKeyListener(txtCodeKeyListener);
        }
    }

    public String GetJoinedAccessCode(EditText[] txtCodesParam) {
        String inputtedCode = "";
        for (int i = 0; i < txtCodesParam.length; i++) {
            inputtedCode += txtCodesParam[i].getText().toString();
        }

        return inputtedCode;
    }
}
