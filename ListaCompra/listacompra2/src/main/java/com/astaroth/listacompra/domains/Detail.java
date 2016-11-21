package com.astaroth.listacompra.domains;

public class Detail {
	public long id;
	public long collectionId;
	public String description;
	public String name;
	public int quantity;
	public double amount;
	public int mark;

	public Detail(long id, String description, String name, int quantity, long collectionId, double importe, int marcado) {
		this.id = id;
		this.description = description;
		this.name = name;
		this.quantity = quantity;
		this.collectionId = collectionId;
		this.amount = importe;
		this.mark = marcado;
	}

	public Detail(String description, String name, int quantity, long collectionId, double importe, int marcado) {
		this.description = description;
		this.name = name;
		this.quantity = quantity;
		this.collectionId = collectionId;
		this.amount = importe;
		this.mark = marcado;
	}
}
