package com.astaroth.listacompra.ui.feats.home;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.support.base.ActivityNavigator;
import com.astaroth.listacompra.support.base.BaseActivityView;
import com.astaroth.listacompra.support.domain.CollectionToShow;
import com.astaroth.listacompra.ui.feats.home.detail.DetailFragment;
import java.util.List;

class HomeView extends BaseActivityView {

	private final ViewListener listener;
	private DrawerLayout drawer;
	private HomeNavRenderer renderer;
	private Toolbar toolbar;

	private HomeNavRenderer.HomeNavCallback drawerCallback = getDrawerCallback();

	HomeView(ViewListener listener) {
		super(R.layout.home_activity_layout, R.id.home_container_framelayout);
		this.listener = listener;
	}

	@Override
	protected void setUpView(View view) {
		super.setUpView(view);
		setUpToolbar((Toolbar) view.findViewById(R.id.home_toolbar));
		setUpDrawer(view);
	}

	private void setUpToolbar(Toolbar toolbar) {
		this.toolbar = toolbar;
		toolbar.setNavigationIcon(R.drawable.carrito);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawer.openDrawer(Gravity.LEFT);
			}
		});
		toolbar.setOnMenuItemClickListener(getMenuListener());
	}

	private Toolbar.OnMenuItemClickListener getMenuListener() {
		return new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.detail_toolbar_items_edit:
						listener.editCollection();
						return true;
					case R.id.detail_toolbar_items_delete:
						confirmDeleteCollection();
						return true;
					//case R.id.detail_toolbar_items_send:
					//	listener.sendCollection();
					//	return true;
				}
				return false;
			}
		};
	}

	private void confirmDeleteCollection() {
		new AlertDialog.Builder(toolbar.getContext()).setTitle(R.string.delete_collection_title)
			.setMessage(R.string.delete_collection_message)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					listener.deleteCollection();
				}
			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.show();
	}

	private void setUpDrawer(View view) {
		this.drawer = (DrawerLayout) view.findViewById(R.id.home_drawer);
		RecyclerView drawerRecycler = (RecyclerView) view.findViewById(R.id.home_drawer_recycler);
		renderer = new HomeNavRenderer(drawerRecycler, drawerCallback);
	}

	void fillDrawer(List<Collection> collections) {
		renderer.paintData(collections);
	}

	void setDataWithOutCollection() {
		createFragment(DetailFragment.newEmptyInstance());
	}

	void setDataCollection(Collection collection) {
		renderer.setCollectionViewed(collection);
		createFragment(DetailFragment.newInstance(collection));
	}

	void setEmptyToolbarData() {
		setToolbarData(getDefaultAppName(), R.drawable.carrito, false);
	}

	void setToolbarData(String title, int resource, boolean showMenu) {
		toolbar.setTitle(title);
		toolbar.setNavigationIcon(resource);
		showToolbarMenu(showMenu, toolbar.getMenu());
	}

	private void showToolbarMenu(boolean showMenu, Menu menu) {
		if (menu != null) {
			menu.clear();
		}
		if (showMenu) {
			toolbar.inflateMenu(R.menu.detail_toolbar_items_menu);
		}
	}

	private String getDefaultAppName() {
		Resources resources = toolbar.getResources();
		return resources.getString(R.string.app_name);
	}

	private void createFragment(DetailFragment fragmentToShow) {
		ActivityNavigator navigator = viewContextInject(ActivityNavigator.class);
		navigator.navigate(fragmentToShow);
	}

	void addCollectionToDrawer(Collection collection) {
		renderer.addCollection(collection);
		setDataCollection(collection);
	}

	void deleteCollectionFromDrawer(long idCollection) {
		CollectionToShow collectionToShow = renderer.deleteAndGetNextCollection(idCollection);
		switch (collectionToShow.status) {
			case COLLECTION:
				setDataCollection(collectionToShow.collection);
				break;
			case EMPTY:
				setDataWithOutCollection();
		}
	}

	void updateCollectionInDrawer(Collection collection) {
		renderer.updateCollection(collection);
		setDataCollection(collection);
	}

	private HomeNavRenderer.HomeNavCallback getDrawerCallback() {
		return new HomeNavRenderer.HomeNavCallback() {
			@Override
			public void onClickItem(Collection collection) {
				drawer.closeDrawers();
				setDataCollection(collection);
			}

			@Override
			public void onAddCollectionClick() {
				drawer.closeDrawers();
				createCollectionDialog(null);
			}
		};
	}

	void createCollectionDialog(Collection collection) {
		HomeCollectionDialog.createCollectionDialog(viewContextInject(HomeActivity.class), collection,
													new HomeCollectionDialog.HomeCollectionDialogListener() {
														@Override
														public void onItemSaved(Collection collection) {
															listener.saveCollection(collection);
														}
													});
	}

	interface ViewListener {
		void saveCollection(Collection collection);

		void deleteCollection();

		void editCollection();
	}
}
