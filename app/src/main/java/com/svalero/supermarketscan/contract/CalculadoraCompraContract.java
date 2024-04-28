package com.svalero.supermarketscan.contract;

import android.content.Context;

import com.svalero.supermarketscan.domain.ProductoVistaBase;

import java.util.List;

public interface CalculadoraCompraContract {
    interface Model {

        void startDb(Context context);

        List<ProductoVistaBase> loadAllProductosVB();

        List<ProductoVistaBase> loadProductoByQuery(String query);

        void deleteProduct(ProductoVistaBase producto);

        void deleteAllProduct();
    }

    interface View {
        void listProductosVB(List<ProductoVistaBase> productosList);

        void showMessage(int message);
    }

    interface Presenter {

        void loadAllProductosVB();

        void loadProductoByQuery(String query);

        void deleteProduct(ProductoVistaBase producto);

        void deleteAllProduct();
    }
}
