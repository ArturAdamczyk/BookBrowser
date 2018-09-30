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

public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity implements UiInteraction, ActivityContract {
    private final String CLASS_TAG = this.getClass().getName();

    private ProgressDialog progressDialog;
    @Inject
    protected Messenger messenger;
    @Inject
    protected IntentManager intentManager;
    @Inject
    protected ViewModelFactory baseViewModelFactory;
    protected T viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        injectDependencies();
        initViewModel();
        viewModel.getShowMessage().observe(this, it -> showMessage(it));
        viewModel.getLogMessage().observe(this, it -> logMessage(it));
        viewModel.getPassMessage().observe(this, it -> passMessage(it));
        viewModel.getShowProgress().observe(this, it -> showProgress(it));
        viewModel.getHideProgress().observe(this, it -> hideProgress());
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
    public void openActivity(Class targetActivity) {
        startActivity(intentManager.feedIntent(new Intent(this, targetActivity)));
    }
}
