package com.art.bookbrowser.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.EditText;

import com.art.bookbrowser.App;
import com.art.bookbrowser.R;
import com.art.bookbrowser.base.BaseActivity;
import com.art.bookbrowser.di.ActivityModule;
import com.art.bookbrowser.helpers.KeyboardManager;
import com.art.bookbrowser.viewmodels.LoginViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginViewModel> {

    @BindView(R.id.loginEditTextUser)
    EditText loginEditTextLogin;
    @BindView(R.id.loginEditTextPassword)
    EditText loginEditTextPassword;
    @BindView(R.id.loginButtonSign)
    Button loginButtonSign;
    @BindView(R.id.loginParentLayout)
    ConstraintLayout loginParentLayout;

    @Inject
    protected KeyboardManager keyboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.loginButtonSign)
    public void onViewClicked() {
        loginToServer();
    }

    private void loginToServer(){
        keyboardManager.hideKeyboard(loginParentLayout);
        viewModel.loginToServer(
                String.valueOf(loginEditTextLogin.getText()),
                String.valueOf(loginEditTextPassword.getText()));
    }

    private void goToNextActivity() {
        openActivity(BookBrowserActivity.class);
        finish();
    }

    @Override
    public void injectDependencies(){
        App.appComponent
                .getActivityComponent(new ActivityModule(this))
                .inject(this);
    }

    @Override
    public void initViewModel(){
        viewModel = ViewModelProviders
                .of(this, baseViewModelFactory)
                .get(LoginViewModel.class);
        viewModel.getLogin().observe(this, message -> goToNextActivity());
    }
}
