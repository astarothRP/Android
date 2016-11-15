package com.astaroth.listacompra.support.inject;

import com.astaroth.listacompra.instruments.inject.Injector;
import com.wokdsem.kinject.Kinject;

public class KinjectInjector implements Injector {

	private Kinject injector;

	public KinjectInjector(Object module) {
		injector = Kinject.instantiate(module);
	}

	@Override
	public <T> T inject(Class<T> tClass) {
		return injector.get(tClass);
	}

}
