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
    private double importe;
    private int marcado;

    public ArticuloSerializable() {
    }

    public ArticuloSerializable(int id, String descripcion, String cantidad, int fkIdLista, double importe, int marcado) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fkIdLista = fkIdLista;
        this.importe = importe;
        this.marcado = marcado;
    }

    public ArticuloSerializable(String descripcion, String cantidad, int fkIdLista, double importe, int marcado) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fkIdLista = fkIdLista;
        this.importe = importe;
        this.marcado = marcado;
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

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public int getMarcado() {
        return marcado;
    }

    public void setMarcado(int marcado) {
        this.marcado = marcado;
    }

    public ContentValues getContentValuesToDB(){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DBAdapter.DB_DESCRIPCION, getDescripcion());
        initialValues.put(DBAdapter.DB_CANTIDAD, getCantidad());
        initialValues.put(DBAdapter.DB_FKIDLISTA, getFkIdLista());
        initialValues.put(DBAdapter.DB_IMPORTE, getImporte());
        initialValues.put(DBAdapter.DB_MARCADO, getMarcado());
        return initialValues;
    }

    public Articulo getArticulo(){
        return new Articulo(id, descripcion, cantidad, fkIdLista, importe, marcado);
    }
}
