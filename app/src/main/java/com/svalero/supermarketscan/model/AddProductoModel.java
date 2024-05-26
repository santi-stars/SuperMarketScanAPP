package com.svalero.supermarketscan.model;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.api.SmScanApi;
import com.svalero.supermarketscan.api.SmScanApiInterface;
import com.svalero.supermarketscan.contract.AddProductoContract;
import com.svalero.supermarketscan.database.AppDatabase;
import com.svalero.supermarketscan.domain.ProductoVistaBase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductoModel implements AddProductoContract.Model {

    private SmScanApiInterface api;
    private AppDatabase db;
    private ProductoVistaBase producto;

    @Override
    public void startDb(Context context) {
        producto = new ProductoVistaBase();
        api = SmScanApi.buildInstance(context);
        db = Room.databaseBuilder(context,
                        AppDatabase.class, "ProductoVistaBase").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
    }

    @Override
    public void loadProductByQuery(OnLoadProductListener listener, String query) {
        Call<ProductoVistaBase> productoCall = api.getProductoBase(query);

        String requestUrl = productoCall.request().url().toString();
        Log.d("PRODUCT_URL", requestUrl);

        loadProductoCallEnqueue(listener, productoCall);
    }

    @Override
    public void insertProduct(ProductoVistaBase producto) {
        ProductoVistaBase productoDB = db.productoVBDao().getProductoByQueryAndNameList(producto.getCodigoBarras(), producto.getNombreLista());

        if (productoDB == null) {
            db.productoVBDao().insert(producto);
        } else {
            productoDB.setCantidad(productoDB.getCantidad() + producto.getCantidad());
            db.productoVBDao().update(productoDB);
        }
    }

    @Override
    public void updateProduct(ProductoVistaBase producto) {
        db.productoVBDao().update(producto);
    }

    private void loadProductoCallEnqueue(OnLoadProductListener listener, Call<ProductoVistaBase> call) {
        call.enqueue(new Callback<ProductoVistaBase>() {
            @Override
            public void onResponse(Call<ProductoVistaBase> call, Response<ProductoVistaBase> response) {

                producto = response.body();
                listener.onLoadProductSuccess(producto);
            }

            @Override
            public void onFailure(Call<ProductoVistaBase> call, Throwable t) {
                listener.onLoadProductError(R.string.load_product_error);
                t.printStackTrace();
            }
        });
    }
}
