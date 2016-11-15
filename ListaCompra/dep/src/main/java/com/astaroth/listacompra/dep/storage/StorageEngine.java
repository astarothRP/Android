package com.astaroth.listacompra.dep.storage;

import android.content.Context;
import com.astaroth.listacompra.dep.storage.Storage;
import com.astaroth.listacompra.dep.storage.db.Db;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.domains.Detail;
import java.util.List;

public class StorageEngine implements Storage {

	private final Db bbdd;
	
	public StorageEngine(Context context) {
		this.bbdd = new Db(context);
	}

	public List<Collection> getCollections() {
		return bbdd.getAllCollections();
	}

	@Override
	public Collection createCollection(Collection collection) {
		return bbdd.createOrUpdateCollecion(collection);
	}

	@Override
	public void updateCollection(Collection collection) {
		bbdd.createOrUpdateCollecion(collection);
	}

	@Override
	public void deleteCollection(long idCollection) {
		bbdd.deleteCollection(idCollection);
	}

	@Override
	public List<Detail> getDetailsByCollection(long collection) {
		return bbdd.getDetailsByCollection(collection);
	}

	@Override
	public Detail createDetail(Detail detail) {
		return bbdd.createOrUpdateDetail(detail);
	}

	@Override
	public void updateDetail(Detail detail) {
		bbdd.createOrUpdateDetail(detail);
	}

	@Override
	public void deleteDetail(long idDetail) {
		bbdd.deleteDetail(idDetail);
	}
}
