package com.svalero.supermarketscan.domain;

public class FavListaTotal {
    private String nombreLista;
    private double total;

    public FavListaTotal(String nombreLista, double total) {
        this.nombreLista = nombreLista;
        this.total = total;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
