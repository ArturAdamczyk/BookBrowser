package com.art.bookbrowser.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.art.bookbrowser.helpers.IntentManager;
import com.art.bookbrowser.helpers.Messenger;
import com.art.bookbrowser.interfaces.ActivityContract;
import com.art.bookbrowser.interfaces.UiInteraction;

import java.util.Optional;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity implements UiInteraction, ActivityContract {
    private final String CLASS_TAG = this.getClass().getName();
    protected CompositeDisposable disposables = new CompositeDisposable();
    @Inject
    protected Messenger messenger;
    @Inject
    protected IntentManager intentManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        intentManager.clear();
    }

    public final String getName() {
        return this.getClass().getName();
    }

    public void logMessage(String msg){
        messenger.logMessage(CLASS_TAG, msg);
    }

    public void showMessage(String msg){
        messenger.showMessage(msg, getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public void passMessage(String logMsg, String userCommunicate){
        messenger
                .logMessage(CLASS_TAG, logMsg)
                .showMessage(userCommunicate, getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public void passMessage(String msg){
        messenger
                .logMessage(CLASS_TAG, msg)
                .showMessage(msg, getWindow().getDecorView().findViewById(android.R.id.content));
    }

    @Override
    public void showProgress(String msg){
        Optional.ofNullable(progressDialog).ifPresent(o ->{
            o.setCancelable(false);
            o.setMessage(msg);
            if (!o.isShowing()) {
                o.show();
            }
        });
    }

    @Override
    public void hideProgress(){
        Optional.ofNullable(progressDialog).ifPresent(o ->{
            if (o.isShowing()) {
                o.dismiss();
            }
        });
    }

    public void initProgressDialog(){
        this.progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }

    public void goToNextActivity(Class targetActivity){
        startActivity(intentManager.feedIntent(new Intent(this, targetActivity)));
    }
}
