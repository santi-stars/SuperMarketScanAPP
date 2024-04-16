package com.svalero.supermarketscan.api;

import static com.svalero.supermarketscan.api.Constants.BASE_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SmScanApi {
    public static SmScanApiInterface buildInstance() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(SmScanApiInterface.class);
    }
}
