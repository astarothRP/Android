package com.astaroth.listacompra.beans;

import android.content.ContentValues;

import com.astaroth.listacompra.datos.DBAdapter;

import java.io.Serializable;

/**
 * Created by rperez on 12/02/2015.
 */
public class Usuario implements Serializable {
    private int id;
    private String login;
    private String password;
    private String mail;
    private int mantenerConectado;

    public Usuario() {
    }

    public Usuario(String login, String password, String mail, int mantenerConectado) {
        this.login = login;
        this.password = password;
        this.mail = mail;
        this.mantenerConectado = mantenerConectado;
    }

    public Usuario(int id, String login, String password, String mail, int mantenerConectado) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.mail = mail;
        this.mantenerConectado = mantenerConectado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getMantenerConectado() {
        return mantenerConectado;
    }

    public void setMantenerConectado(int mantenerConectado) {
        this.mantenerConectado = mantenerConectado;
    }

    public ContentValues getContentValuesToDB(){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DBAdapter.DB_MAIL, getMail());
        initialValues.put(DBAdapter.DB_USUARIO, getLogin());
        initialValues.put(DBAdapter.DB_CONTRASENA, getPassword());
        initialValues.put(DBAdapter.DB_MANTIENE_CONTRASENA, getMantenerConectado());
        return initialValues;
    }
}
