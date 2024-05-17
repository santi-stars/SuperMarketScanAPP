package com.svalero.supermarketscan.model;

import android.content.Context;

import androidx.room.Room;

import com.svalero.supermarketscan.contract.FavListContract;
import com.svalero.supermarketscan.database.AppDatabase;
import com.svalero.supermarketscan.domain.FavListaTotal;

import java.util.List;

public class FavListModel implements FavListContract.Model {

    private AppDatabase db;

    @Override
    public void startDb(Context context) {
        db = Room.databaseBuilder(context,
                        AppDatabase.class, "ProductoVistaBase").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
    }

    @Override
    public List<FavListaTotal> loadFavListsWithTotal() {
        List<FavListaTotal> favListaTotals = db.productoVBDao().getFavListsWithTotal();
        return favListaTotals;
    }

    @Override
    public void renameList(String oldNameList, String newNameList) {
        db.productoVBDao().updateNameList(oldNameList, newNameList);
    }

    @Override
    public void deleteList(String nameList) {
        db.productoVBDao().deleteAllByNameList(nameList);
    }
}
