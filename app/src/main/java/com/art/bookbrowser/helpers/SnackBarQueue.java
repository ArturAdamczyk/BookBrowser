package com.art.bookbrowser.helpers;

import android.support.design.widget.Snackbar;

import java.util.ArrayList;

public class SnackBarQueue{
    private static final int ONE_ELEMENT = 1;
    protected ArrayList<Snackbar> snackbarList = new ArrayList<>();

    protected Snackbar.Callback callback = new Snackbar.Callback() {
        @Override
        public void onDismissed(Snackbar snackbar, int event) {
            snackbarList.remove(snackbar);
            if (snackbarList.size() > 0){
                display(snackbarList.get(0));
            }
        }
    };

    public void add(Snackbar snackbar){
        snackbar.addCallback(callback);
        snackbarList.add(snackbar);
        if(snackbarList.size() == ONE_ELEMENT){
            display(snackbar);
        }
    }

    public void display(Snackbar snackbar){
        snackbar.show();
    }
}

