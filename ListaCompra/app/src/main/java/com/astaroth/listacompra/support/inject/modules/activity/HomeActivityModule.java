package com.astaroth.listacompra.support.inject.modules.activity;

import com.astaroth.listacompra.ui.feats.home.HomeActivity;
import com.wokdsem.kinject.annotations.Includes;
import com.wokdsem.kinject.annotations.Module;
import com.wokdsem.kinject.annotations.Provides;

@Module(completed = true)
public class HomeActivityModule {

	private final HomeActivity homeActivity;

	public HomeActivityModule(HomeActivity homeActivity) {
		this.homeActivity = homeActivity;
	}

	@Includes
	public BaseActivityModule includeBaseActivityModule() {
		return new BaseActivityModule(homeActivity);
	}

	@Provides
	public HomeActivity provideHomeActivity() {
		return homeActivity;
	}

}
