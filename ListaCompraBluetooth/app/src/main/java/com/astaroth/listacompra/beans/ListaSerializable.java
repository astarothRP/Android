package com.astaroth.listacompra.beans;

import android.content.ContentValues;

import com.astaroth.listacompra.datos.DBAdapter;

import java.io.Serializable;

/**
 * Created by rperez on 12/02/2015.
 */
public class ListaSerializable extends Lista implements Serializable {
    public ListaSerializable(int id, String descripcion, int fkIdUsuario, int listaActual) {
        super(id, descripcion, fkIdUsuario, listaActual);
    }

    public ListaSerializable(String descripcion, int fkIdUsuario, int listaActual) {
        super(descripcion, fkIdUsuario, listaActual);
    }

    public ListaSerializable() {
        super();
    }

    public ContentValues getContentValuesToDB(){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DBAdapter.DB_DESCRIPCION, getDescripcion());
        initialValues.put(DBAdapter.DB_FKIDUSUARIO, getFkIdUsuario());
        initialValues.put(DBAdapter.DB_LISTAACTUAL, getListaActual());
        return initialValues;
    }
}
