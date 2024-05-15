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
    public List<ProductoVistaBase> loadAllProductosByNameList(String nameList) {
        return db.productoVBDao().getAllByNameList(nameList);
    }

    @Override
    public List<ProductoVistaBase> loadProductosByQueryAndNameList(String query, String nameList) {
        return db.productoVBDao().getAllByQueryAndNameList(query, nameList);
    }

    @Override
    public void updateProduct(ProductoVistaBase producto) {
        db.productoVBDao().update(producto);
    }

    @Override
    public void deleteProduct(ProductoVistaBase producto) {
        db.productoVBDao().deleteProduct(producto);
    }

    public void deleteAllProductsByNameList(String nameList) {
        db.productoVBDao().deleteAllByNameList(nameList);
    }
}
