package com.astaroth.listacompra.ui.feats.home.detail;

import android.os.Bundle;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.domains.Detail;
import com.astaroth.listacompra.support.base.BaseFragment;
import com.astaroth.listacompra.support.ui.NotParcelled;
import com.wokdsem.kommander.Response;
import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends BaseFragment<DetailView, DetaiInteractor> {

	private static String ARG_COLLECTION = DetailFragment.class.getSimpleName() + "arg_collection";
	Collection collection;

	public static DetailFragment newInstance(Collection collection) {
		DetailFragment detailFragment = new DetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_COLLECTION, NotParcelled.toNotParcelled(collection));
		detailFragment.setArguments(bundle);
		return detailFragment;
	}

	public static DetailFragment newEmptyInstance() {
		return new DetailFragment();
	}

	@Override
	protected DetaiInteractor getInteractor() {
		return new DetaiInteractor();
	}

	@Override
	protected DetailView getFragmentView() {
		return new DetailView(new DetailView.ViewListener() {

			@Override
			public void saveDetail(Detail detail) {
				if (detail.id == 0) {
					createDetail(detail);
				} else {
					updateDetail(detail);
				}
			}

			@Override
			public void deleteDetail(Detail detail) {
				DetailFragment.this.deleteDetail(detail.id);
			}
		});
	}

	@Override
	protected void loadArguments(Bundle arguments) {
		super.loadArguments(arguments);
		String parcelledCollection = arguments.getString(ARG_COLLECTION, null);
		collection = parcelledCollection == null ?
			null :
			NotParcelled.fromNotParcelled(parcelledCollection, Collection.class);
	}

	@Override
	public void onStart() {
		super.onStart();
		fillAllData();
	}

	private void fillAllData() {
		if (collection == null) {
			fragmentView.fillCurrentCollection(collection, new ArrayList<Detail>());
		} else {
			getDetails();
		}
	}

	private void getDetails() {
		interactor.getAllDetails(collection.id)
			.setOnCompleted(new Response.OnCompleted<List<Detail>>() {
				@Override
				public void onCompleted(List<Detail> response) {
					fragmentView.fillCurrentCollection(collection, response);
				}
			})
			.setOnError(new Response.OnError() {
				@Override
				public void onError(Throwable e) {
					fragmentView.showError(getString(R.string.recycler_empty));
				}
			})
			.kommand();
	}

	private void createDetail(final Detail detail) {
		detail.fkIdCollection = collection.id;
		interactor.createDetail(detail)
			.setOnCompleted(new Response.OnCompleted<Long>() {
				@Override
				public void onCompleted(Long response) {
					detail.id = response;
					fragmentView.addDetailToRenderer(detail);
				}
			})
			.setOnError(new Response.OnError() {
				@Override
				public void onError(Throwable e) {
					fragmentView.showError(getString(R.string.error_saving_detail));
				}
			})
			.kommand();
	}

	private void updateDetail(final Detail detail) {
		interactor.updateDetail(detail)
			.setOnCompleted(new Response.OnCompleted<Void>() {
				@Override
				public void onCompleted(Void response) {
					fragmentView.updateDetailInRenderer(detail);
				}
			})
			.setOnError(new Response.OnError() {
				@Override
				public void onError(Throwable e) {
					fragmentView.showError(getString(R.string.error_saving_detail));
				}
			})
			.kommand();
	}

	private void deleteDetail(final long idDetail) {
		interactor.deleteDetail(idDetail)
			.setOnCompleted(new Response.OnCompleted<Void>() {
				@Override
				public void onCompleted(Void response) {
					fragmentView.deleteDetailInRenderer(idDetail);
				}
			})
			.setOnError(new Response.OnError() {
				@Override
				public void onError(Throwable e) {
					fragmentView.showError(getString(R.string.error_saving_detail));
				}
			})
			.kommand();
	}
}
