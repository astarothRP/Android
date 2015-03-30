package com.astaroth.listacompra.beans;

import android.content.ContentValues;

import com.astaroth.listacompra.datos.DBAdapter;

/**
 * Created by rperez on 13/02/2015.
 */
public class UsuarioSend {
    private int id;
    private int fkIdUsuario;
    private String login;

    public UsuarioSend() {
    }

    public UsuarioSend(int id, int fkIdUsuario, String login) {
        this.id = id;
        this.fkIdUsuario = fkIdUsuario;
        this.login = login;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFkIdUsuario() {
        return fkIdUsuario;
    }

    public void setFkIdUsuario(int fkIdUsuario) {
        this.fkIdUsuario = fkIdUsuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ContentValues getContentValuesToDB(){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DBAdapter.DB_FKIDUSUARIO, getFkIdUsuario());
        initialValues.put(DBAdapter.DB_USUARIO, getLogin());
        return initialValues;
    }
}
