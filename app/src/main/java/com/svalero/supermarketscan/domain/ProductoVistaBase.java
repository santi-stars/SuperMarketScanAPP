package com.svalero.supermarketscan.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity
public class ProductoVistaBase implements Comparable<ProductoVistaBase> {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String codigoBarras = "";
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] imagen = new byte[0];
    @ColumnInfo
    private String nombre = "";
    @ColumnInfo
    private String descripcion = "";
    @ColumnInfo
    private double precio = 0.0;
    @ColumnInfo
    private double precioPorKg = 0.0;
    @ColumnInfo
    private int cantidad = 0;

    public ProductoVistaBase() {
    }

    @Ignore
    public ProductoVistaBase(ProductoVistaBase producto) {
        this.codigoBarras = producto.getCodigoBarras();
        this.imagen = producto.getImagen();
        this.nombre = producto.getNombre();
        this.descripcion = producto.getDescripcion();
        this.precio = producto.getPrecio();
        this.precioPorKg = producto.getPrecioPorKg();
        this.cantidad = producto.getCantidad();
    }

    @Ignore
    public ProductoVistaBase(String codigoBarras, byte[] imagen, String nombre, String descripcion, double precio, double precioPorKg, int cantidad) {
        this.codigoBarras = codigoBarras;
        this.imagen = imagen;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.precioPorKg = precioPorKg;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPrecioPorKg() {
        return precioPorKg;
    }

    public void setPrecioPorKg(double precioPorKg) {
        this.precioPorKg = precioPorKg;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "ProductoVistaBase{" +
                "id='" + id + '\'' +
                "codigoBarras='" + codigoBarras + '\'' +
                ", imagen=" + Arrays.toString(imagen) +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", precioPorKg=" + precioPorKg +
                ", cantidad=" + cantidad +
                '}';
    }

    @Override
    public int compareTo(ProductoVistaBase o) {
        return codigoBarras.compareTo(o.codigoBarras);
    }
}
