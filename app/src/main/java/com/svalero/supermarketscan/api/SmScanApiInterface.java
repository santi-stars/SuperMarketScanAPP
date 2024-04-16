package com.svalero.supermarketscan.api;

import com.svalero.supermarketscan.domain.ProductoVistaBase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SmScanApiInterface {
    // Productos
    @GET("Productos/base")
    Call<List<ProductoVistaBase>> getProductosBase();

    @GET("master/{codigoBarras}.json")
    Call<ProductoVistaBase> getProductoGitHub(@Path("codigoBarras") String codigoBarras);

    @GET("Productos/base/{codigoBarras}")
    Call<ProductoVistaBase> getProductoBase(@Path("codigoBarras") String codigoBarras);

}
