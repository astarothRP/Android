package com.astaroth.listacompra.support.inject.modules.app;

import android.os.Handler;
import android.os.Looper;
import com.astaroth.listacompra.core.trolley.Trolley;
import com.astaroth.listacompra.core.trolley.TrolleyEngine;
import com.astaroth.listacompra.dep.storage.Storage;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;
import com.wokdsem.kommander.CentralKommander;
import com.wokdsem.kommander.Deliverer;
import com.wokdsem.kommander.Kommander;

@Module
public class CoreModule {

	private CentralKommander centralKommander = CentralKommander.getInstance();

	@Provides(singleton = true)
	public Kommander provideKommander() {
		return centralKommander.buildKommander(new Deliverer() {
			private Handler handler = new Handler(Looper.getMainLooper());

			@Override
			public void deliver(Runnable runnable) {
				handler.post(runnable);
			}
		});
	}

	@Provides(singleton = true)
	public Trolley provideTrolley(Storage storage) {
		return new TrolleyEngine(storage);
	}

}
