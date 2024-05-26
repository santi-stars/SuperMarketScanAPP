package com.svalero.supermarketscan.presenter;

import com.svalero.supermarketscan.R;
import com.svalero.supermarketscan.contract.FavListContract;
import com.svalero.supermarketscan.domain.FavListaTotal;
import com.svalero.supermarketscan.model.FavListModel;
import com.svalero.supermarketscan.view.FavListView;

import java.util.ArrayList;
import java.util.List;

public class FavListPresenter implements FavListContract.Presenter {

    private FavListModel model;
    private FavListView view;
    private List<FavListaTotal> favList;


    public FavListPresenter(FavListView view) {
        favList = new ArrayList<>();
        model = new FavListModel();
        model.startDb(view.getApplicationContext());

        this.view = view;
    }

    @Override
    public void loadFavList() {
        favList.clear();

        try {
            favList = model.loadFavListsWithTotal();
            view.loadFavList(favList);
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage(R.string.load_lists_error);
        }
    }

    @Override
    public void renameList(String oldNameList, String newNameList) {
        try {
            model.renameList(oldNameList, newNameList);
            showMessage(R.string.rename_list_success);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage(R.string.rename_list_error);
        }
    }

    @Override
    public void deleteList(String nameList) {

        try {
            model.deleteList(nameList);
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
