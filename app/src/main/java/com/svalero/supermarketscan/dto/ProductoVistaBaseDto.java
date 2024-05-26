package com.svalero.supermarketscan.dto;

public class ProductoVistaBaseDto {

    private int id;
    private String codigoBarras;
    private byte[] imagen;
    private String nombre;
    private String descripcion;
    private double precio;
    private double precioPorKg;
    private int cantidad;
    private String nombreLista;

    public ProductoVistaBaseDto() {
    }
}
