package com.astaroth.listacompra.beans;

import android.content.ContentValues;

import com.astaroth.listacompra.datos.DBAdapter;

import java.io.Serializable;

/**
 * Created by rperez on 13/02/2015.
 */
public class ArticuloSerializable extends Articulo implements Serializable {
    public ContentValues getContentValuesToDB(){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DBAdapter.DB_DESCRIPCION, getDescripcion());
        initialValues.put(DBAdapter.DB_CANTIDAD, getCantidad());
        initialValues.put(DBAdapter.DB_FKIDLISTA, getFkIdLista());
        return initialValues;
    }
}
