package com.svalero.supermarketscan.contract;

import android.content.Context;

import com.svalero.supermarketscan.domain.ProductoVistaBase;

public interface AddProductoContract {
    interface Model {
        interface OnLoadProductListener {
            void onLoadProductSuccess(ProductoVistaBase producto);

            void onLoadProductError(int message);
        }

        void startDb(Context context);

        void loadProductByQuery(OnLoadProductListener listener, String query);

        void insertProduct(ProductoVistaBase producto);

        void updateProduct(ProductoVistaBase producto);
    }

    interface View {
        void showProduct(ProductoVistaBase product);

        void showMessage(int message);
    }

    interface Presenter {
        void loadProductByQuery(String query);

        void insertProduct(ProductoVistaBase producto);

        void updateProduct(ProductoVistaBase producto);
    }
}
