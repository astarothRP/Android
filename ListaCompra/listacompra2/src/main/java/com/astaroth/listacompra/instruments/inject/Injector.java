package com.astaroth.listacompra.instruments.inject;

public interface Injector {

	<T> T inject(Class<T> tClass);

}
