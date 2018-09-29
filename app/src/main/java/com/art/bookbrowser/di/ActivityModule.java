package com.art.bookbrowser.di;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.art.bookbrowser.helpers.IntentManager;
import com.art.bookbrowser.helpers.KeyboardManager;

import java.util.HashSet;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.INPUT_METHOD_SERVICE;

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    public Context providesContext() {
        return activity;
    }

    @Provides
    public InputMethodManager providesInputMethodManager() {
        return (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
    }

    @Provides
    public KeyboardManager providesKeyboardManager() {
        return new KeyboardManager(providesInputMethodManager());
    }

    @Provides
    public IntentManager providesIntentManaager() {
        return new IntentManager(new Bundle(), new HashSet());
    }
}