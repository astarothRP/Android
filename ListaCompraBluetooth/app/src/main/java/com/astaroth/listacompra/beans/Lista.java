package com.astaroth.listacompra.beans;

/**
 * Created by rperez on 12/02/2015.
 */
public class Lista {
    private int id;
    private String descripcion;
    private int fkIdUsuario;
    private int listaActual;

    /*public Lista(ListaSerializable lista) {
        this.id = lista.getId();
        this.descripcion = lista.getDescripcion();
        this.fkIdUsuario = lista.getFkIdUsuario();
        this.listaActual = lista.getListaActual();
    }*/

    public Lista(int id, String descripcion, int fkIdUsuario, int listaActual) {
        this.id = id;
        this.descripcion = descripcion;
        this.fkIdUsuario = fkIdUsuario;
        this.listaActual = listaActual;
    }

    public Lista(String descripcion, int fkIdUsuario, int listaActual) {
        this.descripcion = descripcion;
        this.fkIdUsuario = fkIdUsuario;
        this.listaActual = listaActual;
    }

    public Lista() {
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
}
