package com.astaroth.listacompra.dep.storage;

import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.domains.Detail;
import java.util.List;

public interface Storage {

	List<Collection> getCollections();

	Collection createCollection(Collection collection);

	void updateCollection(Collection collection);

	void deleteCollection(long idCollection);

	List<Detail> getDetailsByCollection(long collection);

	Detail createDetail(Detail detail);

	void updateDetail(Detail detail);

	void deleteDetail(long idDetail);
}
