package com.astaroth.listacompra.support.domain;

import com.astaroth.listacompra.domains.Collection;

public class CollectionToShow {

	public final Collection collection;
	public final Status status;

	private CollectionToShow(Collection collection, Status status) {
		this.collection = collection;
		this.status = status;
	}

	public static CollectionToShow emptyCollection() {
		return new CollectionToShow(null, Status.EMPTY);
	}

	public static CollectionToShow errorCollection() {
		return new CollectionToShow(null, Status.ERROR);
	}

	public static CollectionToShow collection(Collection collection) {
		return new CollectionToShow(collection, Status.COLLECTION);
	}

	public enum Status {
		EMPTY,
		ERROR,
		COLLECTION
	}
}
