package com.svalero.supermarketscan.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.svalero.supermarketscan.domain.FavListaTotal;
import com.svalero.supermarketscan.domain.ProductoVistaBase;

import java.util.List;

@Dao
public interface ProductoVBDao {

    @Query("SELECT * FROM ProductoVistaBase")
    List<ProductoVistaBase> getAll();

    @Query("SELECT * FROM ProductoVistaBase WHERE nombreLista LIKE :nameList")
    List<ProductoVistaBase> getAllByNameList(String nameList);

    @Query("SELECT * FROM ProductoVistaBase WHERE (codigoBarras LIKE :query OR nombre LIKE :query)" +
            " AND (nombreLista LIKE :nameList)")
    List<ProductoVistaBase> getAllByQueryAndNameList(String query, String nameList);

    @Query("SELECT nombreLista, SUM(precio * cantidad) AS total FROM ProductoVistaBase" +
            " WHERE nombreLista <> '' GROUP BY nombreLista")
    List<FavListaTotal> getFavListsWithTotal();

    @Query("SELECT * FROM ProductoVistaBase WHERE codigoBarras LIKE :query")
    ProductoVistaBase getProductoByQuery(String query);

    @Query("SELECT * FROM ProductoVistaBase WHERE codigoBarras LIKE :query AND nombreLista LIKE :nameList")
    ProductoVistaBase getProductoByQueryAndNameList(String query, String nameList);

    @Insert
    void insert(ProductoVistaBase ProductoVistaBase);

    @Update
    void update(ProductoVistaBase ProductoVistaBase);

    @Delete
    void deleteProduct(ProductoVistaBase ProductoVistaBase);

    @Query("DELETE FROM ProductoVistaBase WHERE nombreLista LIKE :nameList")
    void deleteAllByNameList(String nameList);

    @Query("UPDATE ProductoVistaBase SET nombreLista = :newNameList WHERE nombreLista = :oldNameList")
    void updateNameList(String oldNameList, String newNameList);
}
