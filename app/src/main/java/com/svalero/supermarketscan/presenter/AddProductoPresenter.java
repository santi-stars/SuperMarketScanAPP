package com.svalero.supermarketscan.presenter;

import com.svalero.supermarketscan.contract.AddProductoContract;
import com.svalero.supermarketscan.domain.ProductoVistaBase;
import com.svalero.supermarketscan.model.AddProductoModel;
import com.svalero.supermarketscan.view.AddProductoView;

public class AddProductoPresenter implements AddProductoContract.Presenter, AddProductoContract.Model.OnLoadProductListener {

    private AddProductoModel model;
    private AddProductoView view;

    public AddProductoPresenter(AddProductoView view) {
        model = new AddProductoModel();
        model.startDb(view.getApplicationContext());

        this.view = view;
    }

    @Override
    public void loadProductByQuery(String query) {
        model.loadProductByQuery(this, query);
    }

    @Override
    public void insertProduct(ProductoVistaBase producto) {
        model.startDb(view.getApplicationContext());
        model.insertProduct(producto);
    }

    @Override
    public void updateProduct(ProductoVistaBase producto) {

    }

    @Override
    public void onLoadProductSuccess(ProductoVistaBase producto) {
        System.out.println("10+++ " + producto.toString());
        view.showProduct(producto);
    }

    @Override
    public void onLoadProductError(int message) {
        view.showMessage(message);
    }
}
