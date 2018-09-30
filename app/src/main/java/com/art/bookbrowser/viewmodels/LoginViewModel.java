package com.art.bookbrowser.viewmodels;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.art.bookbrowser.R;
import com.art.bookbrowser.base.BaseViewModel;
import com.art.bookbrowser.helpers.RestApiFactory;
import com.art.bookbrowser.helpers.RxUtils;
import com.art.bookbrowser.interfaces.Repository;

import javax.inject.Inject;

import lombok.Getter;

public class LoginViewModel extends BaseViewModel {
    private Repository repository;
    private RxUtils rxUtils;

    @Getter
    private MutableLiveData<String> login = new MutableLiveData();

    @Inject
    LoginViewModel(Application app,
                   Repository repository,
                   RxUtils rxUtils
    ){
        super(app);
        this.repository = repository;
        this.rxUtils = rxUtils;
    }

    // temporary solution
    // TODO use proper login API method with implemented token authentication mechanism or at least password encryption
    public void loginToServer(String user, String password) {
        if(!user.isEmpty() && !password.isEmpty()){
            RestApiFactory.updateCredentials(user, password);
            disposables.add(rxUtils.baseCall(repository.getBooks())
                    .subscribe(
                            response -> {
                                logMessage(resources.getString(R.string.login_success));
                                login.postValue("success");
                                // TODO token authentication handling/password encryption
                                //RestApiFactory.updateCredentials(user, password);
                            }, throwable -> {
                                passMessage(resources.getString(R.string.login_failure));
                                login.postValue("success");
                            }));
        }else{
            passMessage(resources.getString(R.string.login_failure));
        }
    }
}
