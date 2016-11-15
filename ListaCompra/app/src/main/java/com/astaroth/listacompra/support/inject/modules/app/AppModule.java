package com.astaroth.listacompra.support.inject.modules.app;

import android.content.Context;
import com.astaroth.listacompra.ListaCompraApp;
import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module(completed = true)
public class AppModule {

	private ListaCompraApp app;

	public AppModule(ListaCompraApp app) {
		this.app = app;
	}

	@Includes
	public CoreModule includeCoreModule() {
		return new CoreModule();
	}

	@Includes
	public DepModule includeDepModule() {
		return new DepModule();
	}

	@Provides
	public Context provideAppContext() {
		return app;
	}

}
