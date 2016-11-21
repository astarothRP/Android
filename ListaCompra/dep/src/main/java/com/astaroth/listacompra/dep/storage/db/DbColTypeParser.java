package com.astaroth.listacompra.dep.storage.db;

import com.astaroth.listacompra.domains.Collection;

import static com.astaroth.listacompra.dep.storage.db.DBConstants.*;
import static com.astaroth.listacompra.domains.Collection.Type.*;

class DbColTypeParser {

	static Collection.Type convertIntToCollectionType(int type) {
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

	static int convertCollectionTypeToInt(Collection.Type type) {
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
}
