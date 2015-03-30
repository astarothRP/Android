package com.astaroth.listacompra.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rperez on 17/02/2015.
 */
public class ListaArticuloSerializable implements Serializable {
    private ListaSerializable lista;
    private List<ArticuloSerializable> articulos;

    public ListaArticuloSerializable(ListaSerializable lista, List<ArticuloSerializable> articulos) {
        this.lista = lista;
        this.articulos = articulos;
    }

    public ListaSerializable getLista() {
        return lista;
    }

    public void setLista(ListaSerializable lista) {
        this.lista = lista;
    }

    public List<ArticuloSerializable> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<ArticuloSerializable> articulos) {
        this.articulos = articulos;
    }
}
