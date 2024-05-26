package com.svalero.supermarketscan.api;

import com.svalero.supermarketscan.domain.ProductoVistaBase;
import com.svalero.supermarketscan.domain.login.LoginRequest;
import com.svalero.supermarketscan.domain.login.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SmScanApiInterface {
    // Productos
    @GET("Productos/base")
    Call<List<ProductoVistaBase>> getProductosBase();

    @GET("master/{codigoBarras}.json")
    Call<ProductoVistaBase> getProductoGitHub(@Path("codigoBarras") String codigoBarras);

    @GET("Productos/base")
    Call<ProductoVistaBase> getProductoBase(@Query("codigoBarras") String codigoBarras);

    @POST("Authentication/validar")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
