package com.svalero.supermarketscan.contract;

import android.content.Context;

import com.svalero.supermarketscan.domain.ProductoVistaBase;

import java.util.List;

public interface CalculadoraCompraContract {
    interface Model {

        void startDb(Context context);

        List<ProductoVistaBase> loadAllProductosByNameList(String nameList);

        List<ProductoVistaBase> loadProductosByQueryAndNameList(String query, String nameList);

        void updateProduct(ProductoVistaBase producto);

        void deleteProduct(ProductoVistaBase producto);

        void deleteAllProductsByNameList(String nameList);
    }

    interface View {
        void listProductosVB(List<ProductoVistaBase> productosList);

        void showMessage(int message);
    }

    interface Presenter {

        void loadAllProductosByNameList(String nameList);

        void loadProductoByQueryAndNameList(String query, String nameList);

        void updateProduct(ProductoVistaBase producto);

        void deleteProduct(ProductoVistaBase producto);

        void deleteAllProductsByNameList(String nameList);
    }
}
