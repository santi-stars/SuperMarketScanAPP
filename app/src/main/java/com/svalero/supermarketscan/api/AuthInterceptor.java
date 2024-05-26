package com.svalero.supermarketscan.api;

import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;

public class AuthInterceptor implements Interceptor {
    private SharedPreferences sharedPreferences;

    public AuthInterceptor(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        okhttp3.Request originalRequest = chain.request();

        String token = sharedPreferences.getString("auth_token", null);

        if (token == null) {
            return chain.proceed(originalRequest);
        }

        okhttp3.Request.Builder builder = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token);

        okhttp3.Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
