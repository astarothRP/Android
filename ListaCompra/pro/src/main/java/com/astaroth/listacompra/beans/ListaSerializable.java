package com.astaroth.listacompra.beans;

import android.content.ContentValues;

import com.astaroth.listacompra.datos.DBAdapter;

import java.io.Serializable;

/**
 * Created by rperez on 12/02/2015.
 */
public class ListaSerializable implements Serializable {
    private int id;
    private String descripcion;
    private int fkIdUsuario;
    private int listaActual;

    public ListaSerializable(int id, String descripcion, int fkIdUsuario, int listaActual) {
        this.id = id;
        this.descripcion = descripcion;
        this.fkIdUsuario = fkIdUsuario;
        this.listaActual = listaActual;
    }

    public ListaSerializable(String descripcion, int fkIdUsuario, int listaActual) {
        this.descripcion = descripcion;
        this.fkIdUsuario = fkIdUsuario;
        this.listaActual = listaActual;
    }

    public ListaSerializable() {
    }

    public int getListaActual() {
        return listaActual;
    }

    public void setListaActual(int listaActual) {
        this.listaActual = listaActual;
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

    public int getFkIdUsuario() {
        return fkIdUsuario;
    }

    public void setFkIdUsuario(int fkIdUsuario) {
        this.fkIdUsuario = fkIdUsuario;
    }

    public ContentValues getContentValuesToDB(){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DBAdapter.DB_DESCRIPCION, getDescripcion());
        initialValues.put(DBAdapter.DB_FKIDUSUARIO, getFkIdUsuario());
        initialValues.put(DBAdapter.DB_LISTAACTUAL, getListaActual());
        return initialValues;
    }
    public Lista getLista(){
        return new Lista(id, descripcion, fkIdUsuario, listaActual);
    }
}
