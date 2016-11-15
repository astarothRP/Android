package com.astaroth.listacompra.support.ui;

import com.google.gson.Gson;

public class NotParcelled {

	private static final Gson gson = new Gson();

	public static <T> String toNotParcelled(T t) {
		return gson.toJson(t);
	}

	public static <T> T fromNotParcelled(String notParcelled, Class<T> tClass) {
		return gson.fromJson(notParcelled, tClass);
	}

}
