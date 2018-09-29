package com.art.bookbrowser.interfaces;

import android.view.View;

public interface Message {
    Message logMessage(String classTag, String logMsg);

    Message showMessage(String appMsg, View view);
}

