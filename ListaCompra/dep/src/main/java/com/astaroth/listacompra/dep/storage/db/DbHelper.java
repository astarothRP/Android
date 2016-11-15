package com.astaroth.listacompra.dep.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.astaroth.listacompra.dep.storage.db.DBConstants.*;

class DbHelper extends SQLiteOpenHelper {

	private static final String CREATE_COLLECTION = "create table " + TABLE_COLLECTIONS + " (" + COLUMN_COLLECTION_ID + " integer primary key autoincrement, " +
									COLUMN_COLLECTION_DESCRIPCION + " text not null, " +
									COLUMN_COLLECTION_TYPE + " integer not null " +
									")";

	private static final String DELETE_COLLECTION = "drop table if exists " + TABLE_COLLECTIONS;
	private static final String CREATE_DETAILS = "create table " + TABLE_DETAILS + " (" + COLUMN_DETAILS_ID + " integer primary key autoincrement, " +
									COLUMN_DETAILS_DESCRIPCION + " text null, " +
									COLUMN_DETAILS_NAME + " text not null, " +
									COLUMN_DETAILS_CANTIDAD + " integer null, " +
									COLUMN_DETAILS_IDCOLLECTION + " integer null, " +
									COLUMN_DETAILS_IMPORTE + " real null, " +
									COLUMN_DETAILS_MARCADO + " integer not null" +
									")";
	private static final String DELETE_DETAILS = "drop table if exists " + TABLE_DETAILS;

	DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//List<Usuario> usuarios = getAllUsuarios(db, oldVersion);
		//HashMap<String, List<ListaSerializable>> listas = getAllListas(db, usuarios, oldVersion);
		//HashMap<String, List<ArticuloSerializable>> articulos = getAllArticulos(db, listas, oldVersion);
		//List<UsuarioSend> userSend = getAllUsuariosSend(db, oldVersion);
		deleteTables(db);
		createTables(db);
		//createData(db, usuarios, listas, articulos, userSend);
	}

	private void createTables(SQLiteDatabase db) {
		db.execSQL(CREATE_COLLECTION);
		db.execSQL(CREATE_DETAILS);
	}

	private void deleteTables(SQLiteDatabase db) {
		db.execSQL(DELETE_COLLECTION);
		db.execSQL(DELETE_DETAILS);
	}
	//private List<UsuarioSend> getAllUsuariosSend(SQLiteDatabase db, int oldVersion) {
	//	List<UsuarioSend> usuarios = new ArrayList<UsuarioSend>();
	//	try {
	//		Cursor c = db.query(DATABASE_TABLE_USUARIOS_SEND, camposUsuarioSend, null, null, null, null, null);
	//		while (c.moveToNext()) {
	//			usuarios.add(new UsuarioSend(c.getInt(0), c.getInt(1), c.getString(2)));
	//		}
	//	} catch (SQLiteException e) {
	//	}
	//	return usuarios;
	//}
}
