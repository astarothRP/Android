package com.rperez.ongpersonas.personasatendidas.beans;

/**
 * Created by rperez on 06/02/2015.
 */
public class Coordinador {
    private String mail;

    public Coordinador() {}
    public Coordinador(String mail) {
        this.mail = mail;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
}
