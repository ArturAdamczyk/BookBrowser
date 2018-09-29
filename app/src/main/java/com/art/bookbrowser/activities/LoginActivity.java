package com.art.bookbrowser.activities;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.EditText;

import com.art.bookbrowser.App;
import com.art.bookbrowser.R;
import com.art.bookbrowser.base.BaseActivity;
import com.art.bookbrowser.di.ActivityModule;
import com.art.bookbrowser.helpers.KeyboardManager;
import com.art.bookbrowser.helpers.RestApiFactory;
import com.art.bookbrowser.interfaces.Repository;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.loginEditTextUser)
    EditText loginEditTextLogin;
    @BindView(R.id.loginEditTextPassword)
    EditText loginEditTextPassword;
    @BindView(R.id.loginButtonSign)
    Button loginButtonSign;
    @BindView(R.id.loginParentLayout)
    ConstraintLayout loginParentLayout;

    @Inject
    protected Repository repository;
    @Inject
    protected KeyboardManager keyboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginButtonSign)
    public void onViewClicked() {
        loginToServer();
    }

    // temporary solution
    // TODO use proper login API method with implemented token authentication mechanism or at least password encryption
    private void loginToServer() {
        String user = String.valueOf(loginEditTextLogin.getText());
        String password = String.valueOf(loginEditTextPassword.getText());
        if(!user.isEmpty() && !password.isEmpty()){
            RestApiFactory.updateCredentials(user, password);
            keyboardManager.hideKeyboard(loginParentLayout);
                    disposables.add(repository.getBooks()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                response -> {
                                    logMessage(getResources().getString(R.string.login_success));
                                    // TODO token authentication handling/password encryption
                                    //RestApiFactory.updateCredentials(user, password);
                                    goToNextActivity();
                            }, throwable -> {
                                passMessage(throwable.getLocalizedMessage(),
                                        getResources().getString(R.string.login_failure));
                                goToNextActivity();
                            }));
        }else{
            passMessage(getResources().getString(R.string.login_failure));
        }
    }

    private void goToNextActivity() {
        goToNextActivity(BookBrowserActivity.class);
        finish();
    }

    @Override
    public void injectDependencies(){
        App.appComponent
                .getActivityComponent(new ActivityModule(this))
                .inject(this);
    }
}
