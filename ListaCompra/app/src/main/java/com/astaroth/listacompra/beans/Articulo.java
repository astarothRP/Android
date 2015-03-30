package com.astaroth.listacompra.beans;

import android.content.ContentValues;

import com.astaroth.listacompra.datos.DBAdapter;

import java.io.Serializable;

/**
 * Created by rperez on 13/02/2015.
 */
public class Articulo {
    private int id;
    private String descripcion;
    private String cantidad;
    private int fkIdLista;

    public Articulo() {
    }

    public Articulo(int id, String descripcion, String cantidad, int fkIdLista) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fkIdLista = fkIdLista;
    }

    public Articulo(String descripcion, String cantidad, int fkIdLista) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fkIdLista = fkIdLista;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public int getFkIdLista() {
        return fkIdLista;
    }

    public void setFkIdLista(int fkIdLista) {
        this.fkIdLista = fkIdLista;
    }
}
