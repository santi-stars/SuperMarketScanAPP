package com.svalero.supermarketscan.presenter;

import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.contract.CalculadoraCompraContract;
import com.svalero.supermarketscan.domain.ProductoVistaBase;
import com.svalero.supermarketscan.model.CalculadoraCompraModel;
import com.svalero.supermarketscan.view.CalculadoraCompraView;

import java.util.ArrayList;
import java.util.List;

public class CalculadoraCompraPresenter implements CalculadoraCompraContract.Presenter {

    private CalculadoraCompraModel model;
    private CalculadoraCompraView view;
    private List<ProductoVistaBase> productosVBList;

    public CalculadoraCompraPresenter(CalculadoraCompraView view) {
        productosVBList = new ArrayList<>();
        model = new CalculadoraCompraModel();
        model.startDb(view.getApplicationContext());

        this.view = view;
    }

    public void loadAllProductosVB() {
        productosVBList.clear();

        try {
            productosVBList = model.loadAllProductosVB();
            view.listProductosVB(productosVBList);
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage(R.string.load_products_error);
        }
    }

    @Override
    public void loadProductoByQuery(String query) {
        productosVBList.clear();

        try {
            productosVBList = model.loadProductoByQuery(query);
            view.listProductosVB(productosVBList);
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage(R.string.load_products_error);
        }
    }

    @Override
    public void deleteClient(ProductoVistaBase producto) {

        try {
            model.deleteProduct(producto);
            showMessage(R.string.delete_product_success);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(R.string.delete_product_error);
        }
    }

    private void showMessage(int message) {
        view.showMessage(message);
    }
}
