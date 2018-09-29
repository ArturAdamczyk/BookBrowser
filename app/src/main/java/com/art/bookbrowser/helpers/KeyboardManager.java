package com.art.bookbrowser.helpers;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardManager {
    private InputMethodManager inputMethodManager;

    public KeyboardManager(InputMethodManager inputMethodManager){
        this.inputMethodManager = inputMethodManager;
    }

    public void hideKeyboard(View view){
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
