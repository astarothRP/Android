package com.astaroth.listacompra.dep.storage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.domains.Detail;
import java.util.ArrayList;
import java.util.List;

import static com.astaroth.listacompra.dep.storage.db.DBConstants.*;
import static com.astaroth.listacompra.domains.Collection.Type.*;

public class Db {

	private final DbHelper dbhelper;
	private SQLiteDatabase db = null;

	public Db(Context context) {
		dbhelper = new DbHelper(context);
	}

	private void open() throws SQLException {
		db = dbhelper.getWritableDatabase();
	}

	private void close() {
		dbhelper.close();
	}

	public List<Collection> getAllCollections() {
		open();
		List<Collection> collections = new ArrayList<>();
		Cursor cursor = db.query(TABLE_COLLECTIONS, CAMPOS_COLLECTIONS, null, null, null, null, null);
		while (cursor.moveToNext()) {
			collections.add(getCollectionByRow(cursor));
		}
		close();
		return collections;
	}

	private Collection getCollectionByRow(Cursor cursor) {
		return new Collection(cursor.getInt(COLUMN_COL_ID_POSITION), cursor.getString(COLUMN_COL_DESCRIPTION_POSITION),
							  convertIntToCollectionType(cursor.getInt(COLUMN_COL_TYPE_POSITION)));
	}

	private Collection.Type convertIntToCollectionType(int type) {
		switch (type) {
			case COL_TYPE_FILM:
				return FILM;
			case COL_TYPE_TRAVEL:
				return TRAVEL;
			case COL_TYPE_TROLLEY:
				return TROLLEY;
			case COL_TYPE_BOOK:
				return BOOK;
			case COL_TYPE_MUSIC:
				return MUSIC;
			case COL_TYPE_PRESENT:
				return PRESENT;
			case COL_TYPE_FOOD:
				return FOOD;
			case COL_TYPE_INDEFINIED:
			default:
				return INDEFINIED;
		}
	}

	private int convertCollectionTypeToInt(Collection.Type type) {
		switch (type) {
			case FILM:
				return COL_TYPE_FILM;
			case TRAVEL:
				return COL_TYPE_TRAVEL;
			case TROLLEY:
				return COL_TYPE_TROLLEY;
			case BOOK:
				return COL_TYPE_BOOK;
			case MUSIC:
				return COL_TYPE_MUSIC;
			case PRESENT:
				return COL_TYPE_PRESENT;
			case FOOD:
				return COL_TYPE_FOOD;
			case INDEFINIED:
			default:
				return COL_TYPE_INDEFINIED;
		}
	}

	public List<Detail> getDetailsByCollection(long collection) {
		open();
		List<Detail> details = new ArrayList<>();
		Cursor cursor = getCursorDetailsByCollection(collection);
		while (cursor.moveToNext()) {
			details.add(getDetailByRow(cursor));
		}
		close();
		return details;
	}

	private Cursor getCursorDetailsByCollection(long collection) {
		String conditionQuery = COLUMN_DETAILS_IDCOLLECTION + "=?";
		String[] params = new String[] { String.valueOf(collection) };
		return db.query(TABLE_DETAILS, CAMPOS_DETAILS, conditionQuery, params, null, null, null);
	}

	private Detail getDetailByRow(Cursor cursor) {
		return new Detail(cursor.getInt(COLUMN_DET_ID_POSITION), cursor.getString(COLUMN_DET_DESCRIPTION_POSITION),
						  cursor.getString(COLUMN_DET_NAME_POSITION), cursor.getInt(COLUMN_DET_CANTIDAD_POSITION),
						  cursor.getInt(COLUMN_DET_COLLECTION_POSITION), cursor.getDouble(COLUMN_DET_IMPORTE_POSITION),
						  cursor.getInt(COLUMN_DET_MARCADO_POSITION));
	}

	public Collection createOrUpdateCollecion(Collection collection) {
		open();
		if (collection.id == 0) {
			collection.id = db.insert(TABLE_COLLECTIONS, null, getCollectionContentValues(collection));
		} else {
			String conditionQuery = COLUMN_COLLECTION_ID + "=?";
			String[] params = new String[] { String.valueOf(collection.id) };
			db.update(TABLE_COLLECTIONS, getCollectionContentValues(collection), conditionQuery, params);
		}
		close();
		return collection;
	}

	private ContentValues getCollectionContentValues(Collection collection) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_COLLECTION_DESCRIPCION, collection.descripcion);
		initialValues.put(COLUMN_COLLECTION_TYPE, convertCollectionTypeToInt(collection.type));
		return initialValues;
	}

	public void deleteCollection(long idCollection) {
		open();
		deleteDetailsByCollection(idCollection);
		String conditionQuery = COLUMN_COLLECTION_ID + "=?";
		String[] params = new String[] { String.valueOf(idCollection) };
		db.delete(TABLE_COLLECTIONS, conditionQuery, params);
		close();
	}

	private void deleteDetailsByCollection(long idCollection) {
		String conditionQuery = COLUMN_DETAILS_IDCOLLECTION + "=?";
		String[] params = new String[] { String.valueOf(idCollection) };
		db.delete(TABLE_DETAILS, conditionQuery, params);
	}

	public Detail createOrUpdateDetail(Detail detail) {
		open();
		if (detail.id == 0) {
			detail.id = db.insert(TABLE_DETAILS, null, getDetailContentValues(detail));
		} else {
			String conditionQuery = COLUMN_DETAILS_ID + "=?";
			String[] params = new String[] { String.valueOf(detail.id) };
			db.update(TABLE_DETAILS, getDetailContentValues(detail), conditionQuery, params);
		}
		close();
		return detail;
	}

	public void deleteDetail(long idDetail) {
		open();
		String conditionQuery = COLUMN_DETAILS_ID + "=?";
		String[] params = new String[] { String.valueOf(idDetail) };
		db.delete(TABLE_DETAILS, conditionQuery, params);
		close();
	}

	private ContentValues getDetailContentValues(Detail detail) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(COLUMN_DETAILS_DESCRIPCION, detail.description);
		initialValues.put(COLUMN_DETAILS_NAME, detail.name);
		initialValues.put(COLUMN_DETAILS_CANTIDAD, detail.quantity);
		initialValues.put(COLUMN_DETAILS_IDCOLLECTION, detail.fkIdCollection);
		initialValues.put(COLUMN_DETAILS_IMPORTE, detail.amount);
		initialValues.put(COLUMN_DETAILS_MARCADO, detail.mark);
		return initialValues;
	}
}
