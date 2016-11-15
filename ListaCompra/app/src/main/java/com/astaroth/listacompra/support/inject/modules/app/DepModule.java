package com.astaroth.listacompra.support.inject.modules.app;

import android.content.Context;
import com.astaroth.listacompra.dep.storage.Storage;
import com.astaroth.listacompra.dep.storage.StorageEngine;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module
public class DepModule {

	@Provides(singleton = true)
	public Storage provideStorage(Context context) {
		return new StorageEngine(context);
	}
}
