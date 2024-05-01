package com.svalero.supermarketscan.model;

import android.content.Context;

import androidx.room.Room;

import com.svalero.supermarketscan.contract.CalculadoraCompraContract;
import com.svalero.supermarketscan.database.AppDatabase;
import com.svalero.supermarketscan.domain.ProductoVistaBase;

import java.util.List;

;

public class CalculadoraCompraModel implements CalculadoraCompraContract.Model {

    private AppDatabase db;

    //TODO: Implementar try-catch o Call enqueue
    @Override
    public void startDb(Context context) {
        db = Room.databaseBuilder(context,
                        AppDatabase.class, "ProductoVistaBase").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
    }

    @Override
    public List<ProductoVistaBase> loadAllProductosVB() {
        return db.productoVBDao().getAll();
    }

    @Override
    public List<ProductoVistaBase> loadProductoByQuery(String query) {
        return db.productoVBDao().getProductosByQuery(query);
    }

    @Override
    public void deleteProduct(ProductoVistaBase producto) {
        db.productoVBDao().deleteProduct(producto);
    }

    @Override
    public void deleteAllProduct() {
        db.productoVBDao().deleteAll();
    }
}
