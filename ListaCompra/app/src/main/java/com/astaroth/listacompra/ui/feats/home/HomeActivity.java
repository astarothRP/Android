package com.astaroth.listacompra.ui.feats.home;

import android.os.Bundle;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.support.base.BaseActivity;
import com.astaroth.listacompra.support.inject.modules.activity.HomeActivityModule;
import com.wokdsem.kommander.Response;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity<HomeView, HomeInteractor> {

	private Collection currentCollection;

	@Override
	protected HomeView getView() {
		return new HomeView(new HomeView.ViewListener() {
			@Override
			public void onCollectionSelected(Collection collection) {
				currentCollection = collection;
			}

			@Override
			public void saveCollection(Collection collection) {
				if (collection.id == 0) {
					createCollection(collection);
				} else {
					updateCollection(collection);
				}
			}

			@Override
			public void deleteCollection() {
				deleteCurrentColection();
			}

			@Override
			public void editCollection() {
				editCurrentCollection();
			}
		});
	}

	private void createCollection(final Collection collection) {
		interactor.createCollection(collection)
			.setOnCompleted(new Response.OnCompleted<Long>() {
				@Override
				public void onCompleted(Long idCollection) {
					collection.id = idCollection;
					view.addCollectionToDrawer(collection);
				}
			})
			.setOnError(new Response.OnError() {
				@Override
				public void onError(Throwable e) {
				}
			})
			.kommand();
	}

	private void updateCollection(final Collection collection) {
		interactor.updateCollection(collection)
			.setOnCompleted(new Response.OnCompleted<Void>() {
				@Override
				public void onCompleted(Void response) {
					view.updateCollectionInDrawer(collection);
				}
			})
			.setOnError(new Response.OnError() {
				@Override
				public void onError(Throwable e) {
				}
			})
			.kommand();
	}

	private void editCurrentCollection() {
		view.createCollectionDialog(currentCollection);
	}

	private void deleteCurrentColection() {
		interactor.deleteCollection(currentCollection.id)
			.setOnCompleted(new Response.OnCompleted<Void>() {
				@Override
				public void onCompleted(Void response) {
					view.deleteCollectionFromDrawer(currentCollection.id);
				}
			})
			.setOnError(new Response.OnError() {
				@Override
				public void onError(Throwable e) {
				}
			})
			.kommand();
	}

	@Override
	protected HomeInteractor getInteractor() {
		return new HomeInteractor();
	}

	@Override
	protected Object getActivityModule() {
		return new HomeActivityModule(this);
	}

	@Override
	protected void onCreated(Bundle savedInstanceState) {
		super.onCreated(savedInstanceState);
		fillCollections();
	}

	private void fillCollections() {
		interactor.getAllCollections()
			.setOnCompleted(new Response.OnCompleted<List<Collection>>() {
				@Override
				public void onCompleted(List<Collection> response) {
					view.fillDrawer(response);
					if (response == null || response.isEmpty()) {
						view.setDataWithOutCollection();
					} else {
						currentCollection = response.get(0);
						view.setDataCollection(currentCollection);
					}
				}
			})
			.setOnError(new Response.OnError() {
				@Override
				public void onError(Throwable e) {
					view.fillDrawer(new ArrayList<Collection>());
					view.setDataWithOutCollection();
				}
			})
			.kommand();
	}
}
