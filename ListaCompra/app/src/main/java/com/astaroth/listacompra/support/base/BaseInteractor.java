package com.astaroth.listacompra.support.base;

import com.astaroth.listacompra.instruments.inject.AppInjector;
import com.astaroth.listacompra.instruments.inject.Injector;
import com.wokdsem.kommander.Kommander;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseInteractor {

	public static final BaseInteractor EMPTY_INTERACTOR;
	private static AtomicLong idGen;

	static {
		idGen = new AtomicLong();
		EMPTY_INTERACTOR = new BaseInteractor() {
		};
	}

	protected final String tag;
	protected final Kommander kommander;
	private final Injector injector;

	public BaseInteractor() {
		tag = getClass().getCanonicalName() + idGen.incrementAndGet();
		injector = AppInjector.getInjector();
		kommander = injector.inject(Kommander.class);
	}

	public final void cancelPendingOperations() {
		cancelPendingOperations(tag);
	}

	public final void cancelPendingOperations(String tag) {
		kommander.cancelKommands(tag);
	}

	protected final <T> T inject(Class<T> tClass) {
		return injector.inject(tClass);
	}

}
