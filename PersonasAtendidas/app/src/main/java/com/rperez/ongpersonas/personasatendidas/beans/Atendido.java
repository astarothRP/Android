package com.rperez.ongpersonas.personasatendidas.beans;

import com.rperez.ongpersonas.personasatendidas.activities.PrincipalActivity;
import com.rperez.ongpersonas.personasatendidas.utils.CSVUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rperez on 09/02/2015.
 */
public class Atendido {
    private int nacionalHombre;
    private int nacionalMujer;
    private int extranjeroHombre;
    private int extranjeroMujer;
    private int totalHombre;
    private int totalMujer;
    private int total;
    private long fecha;
    private int tipo;
    private int exportado;
    private String mail;

    public Atendido() {
    }

    public Atendido(int nacionalHombre, int nacionalMujer, int extranjeroHombre, int extranjeroMujer, int totalHombre, int totalMujer, int total, long fecha, int tipo, String mail, int exportado) {
        this.nacionalHombre = nacionalHombre;
        this.nacionalMujer = nacionalMujer;
        this.extranjeroHombre = extranjeroHombre;
        this.extranjeroMujer = extranjeroMujer;
        this.totalHombre = totalHombre;
        this.totalMujer = totalMujer;
        this.total = total;
        this.fecha = fecha;
        this.tipo = tipo;
        this.mail = mail;
        this.exportado = exportado;
    }

    public int getExportado() {
        return exportado;
    }

    public void setExportado(int exportado) {
        this.exportado = exportado;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getNacionalHombre() {
        return nacionalHombre;
    }

    public void setNacionalHombre(int nacionalHombre) {
        this.nacionalHombre = nacionalHombre;
    }

    public int getNacionalMujer() {
        return nacionalMujer;
    }

    public void setNacionalMujer(int nacionalMujer) {
        this.nacionalMujer = nacionalMujer;
    }

    public int getExtranjeroHombre() {
        return extranjeroHombre;
    }

    public void setExtranjeroHombre(int extranjeroHombre) {
        this.extranjeroHombre = extranjeroHombre;
    }

    public int getExtranjeroMujer() {
        return extranjeroMujer;
    }

    public void setExtranjeroMujer(int extranjeroMujer) {
        this.extranjeroMujer = extranjeroMujer;
    }

    public int getTotalHombre() {
        return totalHombre;
    }

    public void setTotalHombre(int totalHombre) {
        this.totalHombre = totalHombre;
    }

    public int getTotalMujer() {
        return totalMujer;
    }

    public void setTotalMujer(int totalMujer) {
        this.totalMujer = totalMujer;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    // {"Hombre Nacional", "Mujer Nacional", "Hombre Extranjero", "Mujer Extranjero", "Total hombre", "Total mujer", "Total", "Fecha", "Atendido"}
    public String[] getDataToCSV(SimpleDateFormat formato){
        String[] a_rets = new String[CSVUtils.columns.length];
        a_rets[0] = String.valueOf(this.getNacionalHombre());
        a_rets[1] = String.valueOf(this.getNacionalMujer());
        a_rets[2] = String.valueOf(this.getExtranjeroHombre());
        a_rets[3] = String.valueOf(this.getExtranjeroMujer());
        a_rets[4] = String.valueOf(this.getTotalHombre());
        a_rets[5] = String.valueOf(this.getTotalMujer());
        a_rets[6] = String.valueOf(this.getTotal());
        a_rets[7] = formato.format(this.getFecha());
        a_rets[8] = this.getTipo()== PrincipalActivity.TIPO_ATENDIDO ? "SI" : "NO";
        return a_rets;
    }
}
