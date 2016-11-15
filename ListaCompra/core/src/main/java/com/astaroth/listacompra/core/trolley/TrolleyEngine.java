package com.astaroth.listacompra.core.trolley;

import com.astaroth.listacompra.dep.storage.Storage;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.domains.Detail;
import java.util.List;

public class TrolleyEngine implements Trolley {
	private final Storage storage;

	public TrolleyEngine(Storage storage) {
		this.storage = storage;
	}

	@Override
	public List<Collection> getCollections() {
		return storage.getCollections();
	}

	@Override
	public long createCollection(Collection collection) {
		return storage.createCollection(collection).id;
	}

	@Override
	public void updateCollection(Collection collection) {
		storage.updateCollection(collection);
	}

	@Override
	public void deleteCollection(long collection) {
		storage.deleteCollection(collection);
	}

	@Override
	public List<Detail> getDetails(long collection) {
		return storage.getDetailsByCollection(collection);
	}

	@Override
	public long createDetail(Detail detail) {
		return storage.createDetail(detail).id;
	}

	@Override
	public void updateDetail(Detail detail) {
		storage.updateDetail(detail);
	}

	@Override
	public void deleteDetail(long detail) {
		storage.deleteDetail(detail);
	}
}
