package com.astaroth.listacompra.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rperez on 17/02/2015.
 */
public class ListaArticulo {
    private ListaSerializable lista;
    private List<Articulo> articulos;

    public ListaArticulo(ListaSerializable lista, List<Articulo> articulos) {
        this.lista = lista;
        this.articulos = articulos;
    }

    public ListaSerializable getLista() {
        return lista;
    }

    public void setLista(ListaSerializable lista) {
        this.lista = lista;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    public Map<String, Map<String, String>> getMap(){
        Map<String, String> listaM = new HashMap<String, String>();
        listaM.put("lista", lista.getDescripcion());

        for (Articulo articulo: articulos) {
            Map<String, String> articuloM = new HashMap<String, String>();
            listaM.put("articulo", articulo.getDescripcion());
            listaM.put("cantidad", articulo.getCantidad());
        }
        return null;
    }
}
