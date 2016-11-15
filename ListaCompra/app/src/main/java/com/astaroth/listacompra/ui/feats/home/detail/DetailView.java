package com.astaroth.listacompra.ui.feats.home.detail;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.domains.Detail;
import com.astaroth.listacompra.support.base.BaseFragmentView;
import com.astaroth.listacompra.ui.feats.home.HomeActivity;
import java.util.ArrayList;
import java.util.List;

class DetailView extends BaseFragmentView {

	private final ViewListener listener;
	private DetailRenderer renderer;
	private FloatingActionButton addDetailFab;
	private DetailRenderer.DetailRendererListener rendererListener = getDetailRendererListener();

	DetailView(ViewListener listener) {
		super(R.layout.detail_fragment_layout);
		this.listener = listener;
	}

	@Override
	protected void setUpView(View view) {
		setUpFabClick((FloatingActionButton)view.findViewById(R.id.detail_fab));
		renderer = new DetailRenderer((RecyclerView) view.findViewById(R.id.detail_recycler),
									  view.findViewById(R.id.detail_nocontent_textview),
									  (ContentLoadingProgressBar) view.findViewById(
										  R.id.detail_contentloadingprogressbar), rendererListener);

	}

	private void setUpFabClick(FloatingActionButton fabButton) {
		addDetailFab = fabButton;
		addDetailFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDetailDialog(null);
			}
		});
	}

	private void showDetailDialog(Detail detail) {
		DetailDialog.createDetailDialog(viewContextInject(HomeActivity.class), detail,
										new DetailDialog.DetailDialogListener() {
											@Override
											public void onItemSaved(Detail detail) {
												listener.saveDetail(detail);
											}

											@Override
											public void delete(Detail detail) {
												listener.deleteDetail(detail);
											}
										});
	}

	void fillCurrentCollection(Collection collection, List<Detail> details) {
		if (collection != null) {
			addDetailFab.show();
			renderer.paintData(details);
		} else {
			addDetailFab.hide();
			renderer.paintData(new ArrayList<Detail>());
		}
	}

	void addDetailToRenderer(Detail detail) {
		addDetailFab.show();
		renderer.addDetail(detail);
	}

	void updateDetailInRenderer(Detail detail) {
		renderer.updateDetail(detail);
	}

	void deleteDetailInRenderer(long idDetail) {
		addDetailFab.show();
		renderer.deleteDetail(idDetail);
	}

	private DetailRenderer.DetailRendererListener getDetailRendererListener() {
		return new DetailRenderer.DetailRendererListener() {

			@Override
			public void showEditDetail(Detail detail) {
				showDetailDialog(detail);
			}

			@Override
			public void saveDetail(Detail detail) {
				listener.saveDetail(detail);
			}
		};
	}

	interface ViewListener {

		void saveDetail(Detail detail);

		void deleteDetail(Detail detail);
	}

}
