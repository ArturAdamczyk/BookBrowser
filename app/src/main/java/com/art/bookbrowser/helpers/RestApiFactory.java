package com.art.bookbrowser.helpers;

import android.util.Log;

import com.art.bookbrowser.interfaces.rest.RestApi;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RestApiFactory {
    private static final String CLASS_TAG = RestApiFactory.class.getSimpleName();

    private static Retrofit retrofit;
    private static AuthenticationInterceptor authenticationInterceptor;
    private static RestApi restApiClient = null;

    private RestApiFactory(){}

    public static RestApi getService(String webServiceUrl) throws IllegalArgumentException{
        try{
            if (restApiClient == null) {
                initRetrofit(webServiceUrl);
            }
            return restApiClient;
        }catch(IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static void initRetrofit(String webServiceUrl) throws IllegalArgumentException {
        try{
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(authenticationInterceptor = new AuthenticationInterceptor());
            okHttpClientBuilder.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl(webServiceUrl)
                    .client(okHttpClientBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(
                            new GsonBuilder()
                                    .excludeFieldsWithoutExposeAnnotation()
                                    .setLenient()
                                    .create()))
                    .build();
            restApiClient = retrofit.create(RestApi.class);
        }catch(IllegalArgumentException e){
            Log.d(CLASS_TAG, e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static void updateCredentials(String user, String password){
        if(authenticationInterceptor != null){
            authenticationInterceptor.setCredentials(user, password);
        }
    }
}
