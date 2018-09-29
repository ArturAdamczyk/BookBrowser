package com.art.bookbrowser;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.art.bookbrowser.di.AppComponent;
import com.art.bookbrowser.di.AppModule;
import com.art.bookbrowser.di.DaggerAppComponent;
import com.art.bookbrowser.models.InternetConnectionEvent;
import com.art.bookbrowser.services.NetworkConnectionChangeListener;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import timber.log.Timber;

public class App extends Application {
    public static AppComponent appComponent;

    @Inject
    protected Timber.DebugTree debugTree;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
        Timber.plant(debugTree);
        initInternetConnectionBroadcastReceiver();
    }

    private void initInternetConnectionBroadcastReceiver(){
        registerReceiver(new NetworkConnectionChangeListener(new NetworkConnectionChangeListener.Callback() {
            @Override
            public void onInternetAvailable() {
                EventBus.getDefault().post(new InternetConnectionEvent(true));
            }

            @Override
            public void onInternetNotAvailable() {
                EventBus.getDefault().post(new InternetConnectionEvent(false));
            }
        }), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}