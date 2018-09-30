package com.art.bookbrowser.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;

import io.reactivex.disposables.CompositeDisposable;
import lombok.Getter;

public class BaseViewModel extends AndroidViewModel {
    protected CompositeDisposable disposables = new CompositeDisposable();
    protected Resources resources;
    @Getter
    private MutableLiveData<String> showMessage = new MutableLiveData();
    @Getter
    private MutableLiveData<String> passMessage = new MutableLiveData();
    @Getter
    private MutableLiveData<String> logMessage = new MutableLiveData();
    @Getter
    private MutableLiveData<String> showErrorMessage = new MutableLiveData();
    @Getter
    private MutableLiveData<String> showProgress = new MutableLiveData();
    @Getter
    private MutableLiveData hideProgress = new MutableLiveData();

    public BaseViewModel(Application app) {
        super(app);
        this.resources = app.getResources();
    }

    public void passMessage(String msg){
        passMessage.postValue(msg);
    }

    public void showMessage(String msg){
        showMessage.postValue(msg);
    }

    public void logMessage(String msg){
        logMessage.postValue(msg);
    }

    public void showProgress(String msg){
        showProgress.postValue(msg);
    }

    public void hideProgress(){
        hideProgress.postValue(null);
    }

    @Override
    public void onCleared() {
        disposables.dispose();
        super.onCleared();
    }
}
