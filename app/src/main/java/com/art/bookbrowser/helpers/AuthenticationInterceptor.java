package com.art.bookbrowser.helpers;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    private String credentials;

    public void setCredentials(String login, String password) {
        this.credentials = Credentials.basic(login, password);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain
                .request()
                .newBuilder()
                .addHeader("Authorization", credentials)
                .build());
    }
}
