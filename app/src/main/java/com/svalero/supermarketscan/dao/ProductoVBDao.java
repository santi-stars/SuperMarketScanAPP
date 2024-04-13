package com.svalero.supermarketscan.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.supermarketscan.domain.ProductoVistaBase;

import java.util.List;

@Dao
public interface ProductoVBDao {

    @Query("SELECT * FROM ProductoVistaBase")
    List<ProductoVistaBase> getAll();

    @Query("SELECT * FROM ProductoVistaBase WHERE " +
            "codigoBarras = :query OR " +
            "nombre LIKE :query OR " +
            "descripcion LIKE :query")
    List<ProductoVistaBase> getProductoByQuery(String query);

    @Insert
    void insert(ProductoVistaBase ProductoVistaBase);

    @Update
    void update(ProductoVistaBase ProductoVistaBase);

    @Delete
    void delete(ProductoVistaBase ProductoVistaBase);
}
