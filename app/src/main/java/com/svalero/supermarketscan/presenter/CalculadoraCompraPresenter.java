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

    public void loadAllProductosByNameList(String nameList) {
        productosVBList.clear();

        try {
            productosVBList = model.loadAllProductosByNameList(nameList);
            view.listProductosVB(productosVBList);
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage(R.string.load_products_error);
        }
    }

    @Override
    public void loadProductoByQueryAndNameList(String query, String nameList) {
        productosVBList.clear();
        query = "%" + query + "%";

        try {
            System.out.println("DIBUG loadProductoByQuery: " + query);
            productosVBList = model.loadProductosByQueryAndNameList(query, nameList);
            System.out.println("DIBUG loadProductoByQuery productosVBList: " + productosVBList);
            view.listProductosVB(productosVBList);
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage(R.string.load_products_error);
        }
    }

    @Override
    public void updateProduct(ProductoVistaBase producto) {
        model.startDb(view.getApplicationContext());
        model.updateProduct(producto);
    }

    @Override
    public void deleteProduct(ProductoVistaBase producto) {

        try {
            model.deleteProduct(producto);
            showMessage(R.string.delete_product_success);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(R.string.delete_product_error);
        }
    }

    @Override
    public void deleteAllProductsByNameList(String nameList) {

            try {
                model.deleteAllProductsByNameList(nameList);
                showMessage(R.string.delete_all_products_success);
            } catch (Exception e) {
                e.printStackTrace();
                showMessage(R.string.delete_all_products_error);
            }
    }

    private void showMessage(int message) {
        view.showMessage(message);
    }
}
