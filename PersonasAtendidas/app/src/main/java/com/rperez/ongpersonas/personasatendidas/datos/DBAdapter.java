package com.rperez.ongpersonas.personasatendidas.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rperez.ongpersonas.personasatendidas.activities.PrincipalActivity;
import com.rperez.ongpersonas.personasatendidas.beans.Atendido;
import com.rperez.ongpersonas.personasatendidas.beans.Coordinador;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rperez on 06/02/2015.
 */
public class DBAdapter {
    //constantes para campos
    public static final String DB_MAIL="mail";


    //constantes para BD y tablas
    private static final String DATABASE_NAME="PersonasAtendidas";
    private static final int DATABASE_VERSION=5;
    private static final String DATABASE_TABLE_COORDINADORES="coordinadores";
    private static final String DATABASE_TABLE_ATENDIDOS="atendidos";
    //instrucciones SQL
    private static final String DATABASE_CREATE_COORDINADORES=
            "create table "+DATABASE_TABLE_COORDINADORES+" (_id integer primary key autoincrement, "+
                    DB_MAIL+" text not null)";
    private static final String DATABASE_DELETE_COORDINADORES=
            "drop table if exists "+DATABASE_TABLE_COORDINADORES;

    public static String DB_NACIONAL_NOMBRE = "nacionalHombre";
    public static String DB_NACIONAL_MUJER = "nacionalMujer";
    public static String DB_EXTRANJERO_NOMBRE = "extranjeroHombre";
    public static String DB_EXTRANJERO_MUJER = "extranjeroMujer";
    public static String DB_TOTAL_NOMBRE = "totalHombre";
    public static String DB_TOTAL_MUJER = "totalMujer";
    public static String DB_TOTAL = "total";
    public static String DB_FECHA = "fecha";
    public static String DB_TIPO = "tipo";
    public static String DB_EXPORTADO = "exportado";

    private static final String DATABASE_CREATE_ATENDIDOS=
            "create table "+DATABASE_TABLE_ATENDIDOS+" (_id integer primary key autoincrement, "+
                    DB_NACIONAL_NOMBRE+" integer not null default 0, "+
                    DB_NACIONAL_MUJER+" integer not null default 0, "+
                    DB_EXTRANJERO_NOMBRE+" integer not null default 0, "+
                    DB_EXTRANJERO_MUJER+" integer not null default 0, "+
                    DB_TOTAL_NOMBRE+" integer not null default 0, "+
                    DB_TOTAL_MUJER+" integer not null default 0, "+
                    DB_TOTAL+" integer not null default 0, "+
                    DB_FECHA+" integer not null default 0, "+
                    DB_TIPO+" integer not null default 1, "+
                    DB_EXPORTADO+" integer not null default 0, "+
                    DB_MAIL+" text not null "+
                    ")";

    private static final String DATABASE_DELETE_ATENDIDOS=
            "drop table if exists "+DATABASE_TABLE_ATENDIDOS;

    //Atributos
    private SQLiteDatabase db=null;
    private DatabaseHelper dbhelper=null;

    //Contexto
    Context context;

    public DBAdapter(Context ctx){
        this.context=ctx;
        dbhelper=new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME,null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            deleteTables(db);
            createTables(db);
        }

        private void createTables(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_COORDINADORES);
            db.execSQL(DATABASE_CREATE_ATENDIDOS);

        }

        private void deleteTables(SQLiteDatabase db) {
            db.execSQL(DATABASE_DELETE_COORDINADORES);
            db.execSQL(DATABASE_DELETE_ATENDIDOS);
        }
    }
    //metodos expuestos por el modelo
    public void open() throws SQLException {
        db=dbhelper.getWritableDatabase();

    }
    public void close(){
        dbhelper.close();
    }


    //metodos de Coordinador
    public long insertCoordinador(String mail){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DB_MAIL, mail);
        //devuelve id del nuevo registro
        return db.insert(DATABASE_TABLE_COORDINADORES, null, initialValues);
    }

    public Cursor getAllCoordinadoresCursor(){
        //al parecer, es obligatorio recuperar tambien el campo _id
        //si no da una excepcion que dice que no existe campo _id
        return db.query(DATABASE_TABLE_COORDINADORES, new String[]{"_id",DB_MAIL},null, null, null,null,null);
    }

    public String[] getAllMailCoordinadores(){
        Cursor c=getAllCoordinadoresCursor();
        String[] mails = new String[c.getCount()];
        int i = 0;
        while(c.moveToNext()){
            mails[i]=c.getString(1);
            i++;
        }
        return mails;
    }

    public ArrayList<Coordinador> getAllCoordinadores(){
        ArrayList<Coordinador> coordinadores=new ArrayList<Coordinador>();
        Cursor c=getAllCoordinadoresCursor();
        while(c.moveToNext()){
            coordinadores.add(new Coordinador(c.getString(1)));
        }
        return coordinadores;
    }

    //metodos de Coordinador
    public void insertAtendido(Atendido atendido){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DB_NACIONAL_NOMBRE, atendido.getNacionalHombre());
        initialValues.put(DB_NACIONAL_MUJER, atendido.getNacionalMujer());
        initialValues.put(DB_EXTRANJERO_NOMBRE, atendido.getExtranjeroHombre());
        initialValues.put(DB_EXTRANJERO_MUJER, atendido.getExtranjeroMujer());
        initialValues.put(DB_TOTAL_NOMBRE, atendido.getTotalHombre());
        initialValues.put(DB_TOTAL_MUJER, atendido.getTotalMujer());
        initialValues.put(DB_TOTAL, atendido.getTotal());
        initialValues.put(DB_FECHA, atendido.getFecha());
        initialValues.put(DB_TIPO, atendido.getTipo());
        initialValues.put(DB_MAIL, atendido.getMail());
        initialValues.put(DB_EXPORTADO, atendido.getExportado());
        Atendido atendidoDB = getAtendidoPorFecha(atendido.getFecha(), atendido.getTipo(), atendido.getMail());
        if (atendidoDB==null)db.insert(DATABASE_TABLE_ATENDIDOS, null, initialValues);
        else db.update(DATABASE_TABLE_ATENDIDOS, initialValues, DB_FECHA + "=? AND " + DB_TIPO + "=? AND " + DB_MAIL + "=?", new String[]{String.valueOf(atendido.getFecha()), String.valueOf(atendido.getTipo()), atendido.getMail()});
    }
    public void setExportado(List<Atendido> atendidos){
        for (Atendido atendido : atendidos){
            atendido.setExportado(1);
            insertAtendido(atendido);
        }
    }
    public ArrayList<Atendido> findAtendidoPorFechas(long fechaDesde, long fechaHasta, String mail){
        Cursor c = db.query(DATABASE_TABLE_ATENDIDOS, new String[]{"_id",DB_NACIONAL_NOMBRE, DB_NACIONAL_MUJER
                , DB_EXTRANJERO_NOMBRE, DB_EXTRANJERO_MUJER
                , DB_TOTAL_NOMBRE, DB_TOTAL_MUJER
                , DB_TOTAL, DB_FECHA, DB_TIPO, DB_MAIL, DB_EXPORTADO}
                , DB_FECHA + ">=? AND " + DB_FECHA + "<=? AND " + DB_MAIL + "=?"
                , new String[]{String.valueOf(fechaDesde), String.valueOf(fechaHasta), mail}, null,null,null);
        ArrayList<Atendido> atendidos = new ArrayList<Atendido>();
        while(c.moveToNext()){
            atendidos.add(new Atendido(c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6)
                    , c.getInt(7), c.getLong(8), c.getInt(9), c.getString(10), c.getInt(11)));
        }
        return atendidos;
    }
    public ArrayList<Atendido> findAllAtendidoNoExportado(String mail){
        Cursor c = db.query(DATABASE_TABLE_ATENDIDOS, new String[]{"_id",DB_NACIONAL_NOMBRE, DB_NACIONAL_MUJER
                , DB_EXTRANJERO_NOMBRE, DB_EXTRANJERO_MUJER
                , DB_TOTAL_NOMBRE, DB_TOTAL_MUJER
                , DB_TOTAL, DB_FECHA, DB_TIPO, DB_MAIL, DB_EXPORTADO}
                , DB_EXPORTADO + "=? AND " + DB_MAIL + "=?"
                , new String[]{String.valueOf(0), mail}, null,null,null);
        ArrayList<Atendido> atendidos = new ArrayList<Atendido>();
        while(c.moveToNext()){
            atendidos.add(new Atendido(c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6)
                    , c.getInt(7), c.getLong(8), c.getInt(9), c.getString(10), c.getInt(11)));
        }
        return atendidos;
    }
    public Atendido getAtendidoPorFecha(long fecha, int tipo, String mail){
        Cursor c = db.query(DATABASE_TABLE_ATENDIDOS, new String[]{"_id",DB_NACIONAL_NOMBRE, DB_NACIONAL_MUJER
                    , DB_EXTRANJERO_NOMBRE, DB_EXTRANJERO_MUJER
                    , DB_TOTAL_NOMBRE, DB_TOTAL_MUJER
                    , DB_TOTAL, DB_FECHA, DB_TIPO, DB_MAIL, DB_EXPORTADO}
                , DB_FECHA + "=? AND " + DB_TIPO + "=? AND " + DB_MAIL + "=?"
                , new String[]{String.valueOf(fecha), String.valueOf(tipo), mail}, null,null,null);
        Atendido atendido = null;
        if (c.getCount()==1) {
            c.moveToNext();
            atendido = new Atendido(c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6)
                    , c.getInt(7), c.getLong(8), c.getInt(9), c.getString(10), c.getInt(11));
        }
        return atendido;
    }
}
