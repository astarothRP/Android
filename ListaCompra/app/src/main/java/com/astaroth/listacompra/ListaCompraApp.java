package com.astaroth.listacompra;

import android.app.Application;
import com.astaroth.listacompra.instruments.inject.AppInjector;
import com.astaroth.listacompra.support.inject.KinjectInjector;
import com.astaroth.listacompra.support.inject.modules.app.AppModule;

public class ListaCompraApp  extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		setUpInjector();
	}

	private void setUpInjector() {
		AppModule appModule = new AppModule(this);
		AppInjector.initInjector(new KinjectInjector(appModule));
	}
}
