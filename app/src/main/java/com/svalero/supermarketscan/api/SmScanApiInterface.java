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

    @GET("Productos/base/{codigoBarras}")
    Call<List<ProductoVistaBase>> getProductoBase(@Path("codigoBarras") String codigoBarras);

}
