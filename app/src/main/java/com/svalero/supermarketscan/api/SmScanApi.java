package com.svalero.supermarketscan.api;

import static com.svalero.supermarketscan.api.Constants.BASE_URL;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SmScanApi {

    private static SmScanApiInterface apiService;

    public static SmScanApiInterface buildInstance(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences("SmScanPrefs", Context.MODE_PRIVATE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(sharedPreferences))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(SmScanApiInterface.class);

        return apiService;
    }
}
