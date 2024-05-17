package com.svalero.supermarketscan.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.svalero.supermarketscan.dao.ProductoVBDao;
import com.svalero.supermarketscan.domain.ProductoVistaBase;

@Database(entities = {ProductoVistaBase.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ProductoVBDao productoVBDao();

}
