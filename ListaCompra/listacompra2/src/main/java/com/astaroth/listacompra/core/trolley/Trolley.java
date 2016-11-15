package com.astaroth.listacompra.core.trolley;

import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.domains.Detail;
import java.util.List;

public interface Trolley {

	List<Collection> getCollections();

	long createCollection(Collection collection);

	void updateCollection(Collection collection);

	void deleteCollection(long collection);

	List<Detail> getDetails(long collection);

	long createDetail(Detail detail);

	void updateDetail(Detail detail);

	void deleteDetail(long detail);

}
