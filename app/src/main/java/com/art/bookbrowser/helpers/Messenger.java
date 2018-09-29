package com.art.bookbrowser.helpers;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.art.bookbrowser.interfaces.Message;

import timber.log.Timber;

public class Messenger implements Message {
    private SnackBarQueue snackbarQueue;

    public Messenger(SnackBarQueue snackbarQueue){
        this.snackbarQueue = snackbarQueue;
    }

    public Messenger logMessage(String classTag, String logMsg){
        Timber.tag(classTag).e(logMsg);
        return this;
    }

    public Messenger showMessage(String appMsg, View view){
        if(snackbarQueue != null){
            snackbarQueue.add(Snackbar.make(view, appMsg, Snackbar.LENGTH_LONG));
        }else{
            Snackbar.make(view, appMsg, Snackbar.LENGTH_LONG).show();
        }
        return this;
    }
}