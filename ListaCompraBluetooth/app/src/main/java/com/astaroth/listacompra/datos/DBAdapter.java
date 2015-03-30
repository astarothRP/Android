package com.astaroth.listacompra.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.astaroth.listacompra.beans.Articulo;
import com.astaroth.listacompra.beans.ListaSerializable;
import com.astaroth.listacompra.beans.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by rperez on 12/02/2015.
 */
public class DBAdapter {
    //constantes para BD y tablas
    private static final String DATABASE_NAME="ListaCompra";
    private static final int DATABASE_VERSION=5;
    private static final String DATABASE_TABLE_USUARIOS="usuarios";
    private static final String DATABASE_TABLE_LISTAS="listas";
    private static final String DATABASE_TABLE_ARTICULOS="articulos";

    // campos comunes
    public static final String DB_ID="_id";
    public static final String DB_DESCRIPCION="descripcion";
    // USUARIOS
    //campos
    public static final String DB_MAIL="mail";
    public static final String DB_USUARIO="user";
    public static final String DB_CONTRASENA="pass";
    public static final String DB_MANTIENE_CONTRASENA="mantiene";
    public static final String[] camposUsuario = new String[]{DB_ID, DB_MAIL, DB_USUARIO, DB_CONTRASENA, DB_MANTIENE_CONTRASENA};
    //instrucciones SQL
    private static final String DATABASE_CREATE_USUARIOS=
            "create table "+DATABASE_TABLE_USUARIOS+" ("+DB_ID+" integer primary key autoincrement, "+
                    DB_MAIL+" text not null," +
                    DB_USUARIO+" text not null," +
                    DB_CONTRASENA+" text not null," +
                    DB_MANTIENE_CONTRASENA+" integer not null default 0 "+
                    ")";
    private static final String DATABASE_DELETE_USUARIOS=
            "drop table if exists "+DATABASE_TABLE_USUARIOS;

    // LISTAS
    //campos
    public static final String DB_FKIDUSUARIO="fkidusuario";
    public static final String DB_LISTAACTUAL="actual";
    public static final String[] camposLista = new String[]{DB_ID, DB_DESCRIPCION, DB_FKIDUSUARIO, DB_LISTAACTUAL};
    //instrucciones SQL
    private static final String DATABASE_CREATE_LISTAS=
            "create table "+DATABASE_TABLE_LISTAS+" ("+DB_ID+" integer primary key autoincrement, "+
                    DB_DESCRIPCION+" text not null," +
                    DB_FKIDUSUARIO+" integer not null, " +
                    DB_LISTAACTUAL+" integer not null " +
                    ")";
    private static final String DATABASE_DELETE_LISTAS=
            "drop table if exists "+DATABASE_TABLE_LISTAS;

    // ARTICULOS
    //campos
    public static final String DB_FKIDLISTA="fkidlista";
    public static final String DB_CANTIDAD="cantidad";
    public static final String[] camposArticulo = new String[]{DB_ID, DB_DESCRIPCION, DB_CANTIDAD, DB_FKIDLISTA};
    //instrucciones SQL
    private static final String DATABASE_CREATE_ARTICULOS=
            "create table "+DATABASE_TABLE_ARTICULOS+" ("+DB_ID+" integer primary key autoincrement, "+
                    DB_DESCRIPCION+" text not null," +
                    DB_CANTIDAD+" text null," +
                    DB_FKIDLISTA+" integer not null" +
                    ")";
    private static final String DATABASE_DELETE_ARTICULOS=
            "drop table if exists "+DATABASE_TABLE_ARTICULOS;

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
            // extraer datos para volver a insertar
            List<Usuario> usuarios = getAllUsuarios(db);
            HashMap<String, List<ListaSerializable>> listas = getAllListas(db, usuarios);
            HashMap<String, List<Articulo>> articulos = getAllArticulos(db, listas);
            deleteTables(db);
            createTables(db);
            createData(db, usuarios, listas, articulos);
            // insertar datos de nuevo en las tablas
        }

        private void createTables(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_USUARIOS);
            db.execSQL(DATABASE_CREATE_LISTAS);
            db.execSQL(DATABASE_CREATE_ARTICULOS);
            //createInitialData(db);
        }
        private void createInitialData(SQLiteDatabase db){
            Usuario usuario = new Usuario("ricardo", "ricardo", "ricardoperezd@gmail.com", 1);
            long idUser = db.insert(DATABASE_TABLE_USUARIOS, null, usuario.getContentValuesToDB());

            ListaSerializable listaActual = new ListaSerializable("Lista actual", (int)idUser, 1);
            long idListaActual = db.insert(DATABASE_TABLE_LISTAS, null, listaActual.getContentValuesToDB());
            db.insert(DATABASE_TABLE_ARTICULOS, null, (new Articulo("Articulo 1", "2", (int)idListaActual)).getContentValuesToDB());
            db.insert(DATABASE_TABLE_ARTICULOS, null, (new Articulo("Articulo 2", "1", (int)idListaActual)).getContentValuesToDB());
            db.insert(DATABASE_TABLE_ARTICULOS, null, (new Articulo("Articulo 3", "1", (int)idListaActual)).getContentValuesToDB());
            db.insert(DATABASE_TABLE_ARTICULOS, null, (new Articulo("Articulo 4", "6", (int)idListaActual)).getContentValuesToDB());

            ListaSerializable otraLista = new ListaSerializable("Lista no actual", (int)idUser, 0);
            long idListaNoActual = db.insert(DATABASE_TABLE_LISTAS, null, otraLista.getContentValuesToDB());
            db.insert(DATABASE_TABLE_ARTICULOS, null, (new Articulo("Articulo 1", "0", (int)idListaNoActual)).getContentValuesToDB());
            db.insert(DATABASE_TABLE_ARTICULOS, null, (new Articulo("Articulo 2", "0", (int)idListaNoActual)).getContentValuesToDB());
            db.insert(DATABASE_TABLE_ARTICULOS, null, (new Articulo("Articulo 3", "10", (int)idListaNoActual)).getContentValuesToDB());
            db.insert(DATABASE_TABLE_ARTICULOS, null, (new Articulo("Articulo 4", "15", (int)idListaNoActual)).getContentValuesToDB());
        }

        private void deleteTables(SQLiteDatabase db) {
            db.execSQL(DATABASE_DELETE_USUARIOS);
            db.execSQL(DATABASE_DELETE_LISTAS);
            db.execSQL(DATABASE_DELETE_ARTICULOS);
        }
        private List<Usuario> getAllUsuarios(SQLiteDatabase db){
            Cursor c = db.query(DATABASE_TABLE_USUARIOS
                    , camposUsuario
                    , null, null, null,null,null);
            List<Usuario> usuarios = new ArrayList<Usuario>();
            while (c.moveToNext()){
                usuarios.add(new Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
            }
            return usuarios;
        }
        private HashMap<String, List<ListaSerializable>> getAllListas(SQLiteDatabase db, List<Usuario> usuarios){
            HashMap<String, List<ListaSerializable>> listas = new HashMap<String, List<ListaSerializable>>();
            for(Usuario usuario:usuarios) {
                Cursor c = db.query(DATABASE_TABLE_LISTAS
                        , camposLista
                        , DB_FKIDUSUARIO + "=?", new String[]{String.valueOf(usuario.getId())}, null, null, null);
                List<ListaSerializable> lista = new ArrayList<ListaSerializable>();
                while (c.moveToNext()) {
                    lista.add(new ListaSerializable(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3)));
                }
                listas.put(String.valueOf(usuario.getId()), lista);
            }
            return listas;
        }
        private HashMap<String, List<Articulo>> getAllArticulos(SQLiteDatabase db, HashMap<String, List<ListaSerializable>> listas){
            HashMap<String, List<Articulo>> articulos = new HashMap<String, List<Articulo>>();

            Set<String> idUsuarios = listas.keySet();
            Articulo a;
            for(String idUsuario:idUsuarios) {
                for (ListaSerializable lista : listas.get(idUsuario)) {
                    Cursor c = db.query(DATABASE_TABLE_ARTICULOS
                            , camposArticulo
                            , DB_FKIDLISTA + "=?", new String[]{String.valueOf(lista.getId())}, null, null, null);
                    List<Articulo> articulo = new ArrayList<Articulo>();
                    while (c.moveToNext()) {
                        a = new Articulo(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3));
                        if ("0".equals(a.getCantidad())) a.setCantidad("");
                        articulo.add(a);
                    }
                    articulos.put(String.valueOf(lista.getId()), articulo);
                }
            }
            return articulos;
        }
        private void createData(SQLiteDatabase db, List<Usuario> usuarios, HashMap<String, List<ListaSerializable>> listasHM, HashMap<String, List<Articulo>> articulosHM){
            long idUserOld, idListaOld, idUserNew, idListaNew;
            for(Usuario usuario:usuarios){
                idUserOld = usuario.getId();
                idUserNew = db.insert(DATABASE_TABLE_USUARIOS, null, usuario.getContentValuesToDB());
                for (ListaSerializable lista: listasHM.get(String.valueOf(idUserOld))){
                    idListaOld = lista.getId();
                    lista.setFkIdUsuario((int)idUserNew);
                    idListaNew = db.insert(DATABASE_TABLE_LISTAS, null, lista.getContentValuesToDB());
                    for (Articulo articulo:articulosHM.get(String.valueOf(idListaOld))){
                        articulo.setFkIdLista((int)idListaNew);
                        db.insert(DATABASE_TABLE_ARTICULOS, null, articulo.getContentValuesToDB());
                    }
                }
            }
        }
    }
    //metodos expuestos por el modelo
    public void open() throws SQLException {
        db=dbhelper.getWritableDatabase();
    }
    public void close(){
        dbhelper.close();
    }

    //metodos de Usuarios
    public long insertUsuario(Usuario usuario){
        //devuelve id del nuevo registro
        return db.insert(DATABASE_TABLE_USUARIOS, null, usuario.getContentValuesToDB());
    }

    public Usuario getUsuario(String login, String password, boolean mantener){
        Usuario usuario = null;
        Cursor c = db.query(DATABASE_TABLE_USUARIOS
                , camposUsuario
                , DB_USUARIO+"=? AND "+DB_CONTRASENA+"=?"
                , new String[]{login, password}, null,null,null);
        if (c.getCount()==1) {
            c.moveToNext();
            usuario = new Usuario(c.getInt(0), c.getString(2), c.getString(3), c.getString(1), c.getInt(4));
            ContentValues initialValues=new ContentValues();
            initialValues.put(DBAdapter.DB_MANTIENE_CONTRASENA, 0);
            db.update(DATABASE_TABLE_USUARIOS, initialValues, null, null);
            if (mantener) {
                initialValues=new ContentValues();
                initialValues.put(DBAdapter.DB_MANTIENE_CONTRASENA, 1);
                db.update(DATABASE_TABLE_USUARIOS, initialValues, DB_ID+"=?", new String[]{String.valueOf(usuario.getId())});

                usuario.setMantenerConectado(1);
            }
        }
        return usuario;
    }

    public Usuario getConectado(){
        Usuario usuario = null;
        Cursor c = db.query(DATABASE_TABLE_USUARIOS
                , camposUsuario
                , DB_MANTIENE_CONTRASENA+"=?"
                , new String[]{"1"}, null,null,null);
        if (c.getCount()==1) {
            c.moveToNext();
            usuario = new Usuario(c.getInt(0), c.getString(2), c.getString(3), c.getString(1), c.getInt(4));
        }
        return usuario;
    }

    public ListaSerializable getActual(int user){
        ListaSerializable lista = null;
        Cursor c = db.query(DATABASE_TABLE_LISTAS
                , camposLista
                , DB_LISTAACTUAL+"=1 AND "+DB_FKIDUSUARIO+"=?"
                , new String[]{String.valueOf(user)}, null,null,null);
        if (c.getCount()==1) {
            c.moveToNext();
            lista = new ListaSerializable(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3));
        }
        return lista;
    }
    public Cursor getCursorArticulosByLista(int lista){
        return db.query(DATABASE_TABLE_ARTICULOS
                , camposArticulo
                , DB_FKIDLISTA+"=?"
                , new String[]{String.valueOf(lista)}, null,null,null);
    }
    public List<Articulo> getArticulosByLista(int lista){
        return  getArticulosByCursor(getCursorArticulosByLista(lista));
    }
    public List<Articulo> getArticulosByCursor(Cursor c){
        List<Articulo> articulos = new ArrayList<Articulo>();
        while (c.moveToNext()){
            articulos.add(new Articulo(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3)));
        }
        return  articulos;
    }
    public List<ListaSerializable> getListasByNameUsuario(int usuario, String desc){
        List<ListaSerializable> listas = new ArrayList<ListaSerializable>();
        Cursor c = db.query(DATABASE_TABLE_LISTAS
                , camposLista
                , DB_FKIDUSUARIO+"=? AND " + DB_DESCRIPCION + "=?"
                , new String[]{String.valueOf(usuario), desc}, null,null,null);
        while (c.moveToNext()){
            listas.add(new ListaSerializable(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3)));
        }
        return listas;
    }
    public Cursor getCursorListasByUsuario(int usuario){
        return db.query(DATABASE_TABLE_LISTAS
                , camposLista
                , DB_FKIDUSUARIO+"=?"
                , new String[]{String.valueOf(usuario)}, null,null,null);
    }
    public List<ListaSerializable> getListasByUsuario(int usuario){
        return  getListasByCursor(getCursorListasByUsuario(usuario));
    }
    public List<ListaSerializable> getListasByCursor(Cursor c){
        List<ListaSerializable> listas = new ArrayList<ListaSerializable>();
        while (c.moveToNext()){
            listas.add(new ListaSerializable(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3)));
        }
        return  listas;
    }
    public long insertLista(ListaSerializable lista){
        //devuelve id del nuevo registro
        if (lista.getListaActual()==1) desactivaActualesByUser(lista.getFkIdUsuario());
        if (lista.getId()==0) return db.insert(DATABASE_TABLE_LISTAS, null, lista.getContentValuesToDB());
        else db.update(DATABASE_TABLE_LISTAS, lista.getContentValuesToDB(), DB_ID+"=?", new String[]{String.valueOf(lista.getId())});
        return  lista.getId();
    }
    public long insertArticulo(Articulo articulo){
        //devuelve id del nuevo registro
        if (articulo.getId()==0) return db.insert(DATABASE_TABLE_ARTICULOS, null, articulo.getContentValuesToDB());
        else db.update(DATABASE_TABLE_ARTICULOS, articulo.getContentValuesToDB(), DB_ID+"=?", new String[]{String.valueOf(articulo.getId())});
        return  articulo.getId();
    }
    private void desactivaActualesByUser(int idUser){
        ContentValues initialValues=new ContentValues();
        initialValues.put(DBAdapter.DB_LISTAACTUAL, 0);
        db.update(DATABASE_TABLE_LISTAS, initialValues, DB_FKIDUSUARIO+"=?", new String[]{String.valueOf(idUser)});
    }

    public void deleteArticulo(int id){
        deleteItem(DATABASE_TABLE_ARTICULOS, id);
    }
    public void deleteUsuario(int id){
        deleteItem(DATABASE_TABLE_USUARIOS, id);
    }
    public void deleteLista(int id){
        deleteArticulosByLista(id);
        deleteItem(DATABASE_TABLE_LISTAS, id);
    }
    private void deleteItem(String tabla, int id){
        db.delete(tabla, DB_ID+"=?", new String[]{String.valueOf(id)});
    }
    public void deleteArticulosByLista(int id){
        db.delete(DATABASE_TABLE_ARTICULOS, DB_FKIDLISTA+"=?", new String[]{String.valueOf(id)});
    }
}
