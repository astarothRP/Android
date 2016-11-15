package com.astaroth.listacompra.support.base;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ViewNavigator {

	private final BaseActivity baseActivity;
	private final ActivityNavigator navigator;
	private final NavAction navAction;
	private final int navDrawableId;
	private final int backDrawableId;

	public ViewNavigator(BaseActivity baseActivity, ActivityNavigator navigator) {
		this.baseActivity = baseActivity;
		this.navigator = navigator;
		NavConf navConf = new NavConf();
		baseActivity.setUp(navConf);
		this.navAction = navConf.navAction;
		this.navDrawableId = navConf.navDrawableId;
		this.backDrawableId = navConf.backDrawableId;
	}

	public void setUpNavigation(Toolbar toolbar) {
		final boolean navState = navigator.getFragmentCount() == 1 && navAction != null;
		toolbar.setNavigationIcon(navState ? navDrawableId : backDrawableId);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (navState) {
					navAction.onNav();
				} else if (!navigator.navigateUp()) {
					if (!navigator.navigateBack()) {
						baseActivity.finish();
					}
				}
			}
		});
	}

	public interface NavAction {

		void onNav();

	}

	public static class NavConf {

		private NavAction navAction;
		//Todo: define icon
		//		private int navDrawableId = R.drawable.ic_toolbar_menu_white;
		//		private int backDrawableId = R.drawable.ic_toolbar_back_white;
		private int navDrawableId = android.R.drawable.arrow_down_float;
		private int backDrawableId = android.R.drawable.arrow_down_float;

		private NavConf() {
		}

		public NavConf navAction(NavAction navAction) {
			this.navAction = navAction;
			return this;
		}

		public NavConf navDrawable(@DrawableRes int navDrawableId) {
			this.navDrawableId = navDrawableId;
			return this;
		}

		public NavConf backDrawableId(@DrawableRes int backDrawableId) {
			this.backDrawableId = backDrawableId;
			return this;
		}

	}

}
