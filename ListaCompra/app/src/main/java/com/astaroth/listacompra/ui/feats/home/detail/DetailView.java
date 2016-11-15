package com.astaroth.listacompra.ui.feats.home.detail;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
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
	private View coordinatorLayout;

	DetailView(ViewListener listener) {
		super(R.layout.detail_fragment_layout);
		this.listener = listener;
	}

	@Override
	protected void setUpView(View view) {
		setUpFabClick((FloatingActionButton) view.findViewById(R.id.detail_fab), view.findViewById(R.id.detail_layout));
		renderer = new DetailRenderer((RecyclerView) view.findViewById(R.id.detail_recycler),
									  view.findViewById(R.id.detail_nocontent_textview),
									  (ContentLoadingProgressBar) view.findViewById(
										  R.id.detail_contentloadingprogressbar), rendererListener);

	}

	private void setUpFabClick(FloatingActionButton fabButton, View rootView) {
		this.coordinatorLayout = rootView;
		addDetailFab = fabButton;
		addDetailFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDetailDialog(null);
			}
		});
		addDetailFab.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (android.os.Build.VERSION.SDK_INT >= 21) {
					animateRevealColorFromCoordinates(coordinatorLayout, (int) event.getRawX(), (int) event.getRawY());
				}
				return false;
			}
		});
	}

	@TargetApi(21)
	private Animator animateRevealColorFromCoordinates(View viewRoot, int x, int y) {
		float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());
		int color = ContextCompat.getColor(viewRoot.getContext(), R.color.grey_medium);
		int duration = viewRoot.getResources()
			.getInteger(R.integer.anim_duration);

		Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
		viewRoot.setBackgroundColor(color);
		anim.setDuration(duration);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.start();
		return anim;
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

											@Override
											public void onDismis() {
												setDefaultBackgroundColor();
											}

											private void setDefaultBackgroundColor() {
												int color = ContextCompat.getColor(coordinatorLayout.getContext(), R.color
													.grey_light);
												coordinatorLayout.setBackgroundColor(color);
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
