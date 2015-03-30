package com.astaroth.listacompra.beans;

import android.content.ContentValues;

import com.astaroth.listacompra.datos.DBAdapter;

import java.io.Serializable;

/**
 * Created by rperez on 13/02/2015.
 */
public class ArticuloSerializable implements Serializable {
    private int id;
    private String descripcion;
    private String cantidad;
    private int fkIdLista;

    public ArticuloSerializable() {
    }

    public ArticuloSerializable(int id, String descripcion, String cantidad, int fkIdLista) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fkIdLista = fkIdLista;
    }

    public ArticuloSerializable(String descripcion, String cantidad, int fkIdLista) {
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

    public ContentValues getContentValuesToDB(){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DBAdapter.DB_DESCRIPCION, getDescripcion());
        initialValues.put(DBAdapter.DB_CANTIDAD, getCantidad());
        initialValues.put(DBAdapter.DB_FKIDLISTA, getFkIdLista());
        return initialValues;
    }

    public Articulo getArticulo(){
        return new Articulo(id, descripcion, cantidad, fkIdLista);
    }
}
