package com.art.bookbrowser.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnectionChangeListener extends BroadcastReceiver {
    private Callback callback;

    public interface Callback{
        void onInternetAvailable();
        void onInternetNotAvailable();
    }

    public NetworkConnectionChangeListener(Callback callback){
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        isNetworkAvailable(context);
    }

    private void isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()){
            callback.onInternetAvailable();
        }else{
            callback.onInternetNotAvailable();
        }
    }
}
