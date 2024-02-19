package com.example.rxjavaapp;

import android.text.Editable;
import android.text.TextWatcher;

public class SearchTextWatcher implements TextWatcher {

    private final OnTextChangedListener onTextChangedListener;

    public SearchTextWatcher(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onTextChangedListener.onTextChanged(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
