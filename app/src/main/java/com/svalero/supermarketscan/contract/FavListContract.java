package com.svalero.supermarketscan.contract;

import android.content.Context;

import com.svalero.supermarketscan.domain.FavListaTotal;
import com.svalero.supermarketscan.domain.ProductoVistaBase;

import java.util.List;

public interface FavListContract {
    interface Model {

        void startDb(Context context);

        List<FavListaTotal> loadFavListsWithTotal();

        void renameList(String oldNameList, String newNameList);

        void deleteList(String nameList);
    }

    interface View {
        void loadFavList(List<FavListaTotal> favList);

        void showMessage(int message);
    }

    interface Presenter {

        void loadFavList();

        void renameList(String oldNameList, String newNameList);

        void deleteList(String nameList);
    }
}
