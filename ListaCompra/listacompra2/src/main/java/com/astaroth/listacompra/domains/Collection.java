package com.astaroth.listacompra.domains;

public class Collection {

	public long id;
	public String descripcion;
	public Type type;
	public Collection(long id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
		this.type = Type.INDEFINIED;
	}

	public Collection(long id, String descripcion, Type type) {
		this.id = id;
		this.descripcion = descripcion;
		this.type = type;
	}

	public Collection(String descripcion, Type type) {
		this.descripcion = descripcion;
		this.type = type;
	}

	public enum Type {
		TRAVEL,
		BOOK,
		TROLLEY,
		FILM,
		MUSIC,
		PRESENT,
		FOOD,
		INDEFINIED
	}
}
