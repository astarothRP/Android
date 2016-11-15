package com.astaroth.listacompra.support.ui;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.List;

public abstract class BaseRenderer<M extends List<?>, L> {

	protected RecyclerView recycler;
	protected View emptyView;
	protected L listener;
	protected ContentLoadingProgressBar progress;

	public BaseRenderer(RecyclerView recyclerView, View emptyView, ContentLoadingProgressBar progress, L listener) {
		initData(recyclerView, emptyView, progress, listener, null);
	}

	public BaseRenderer(RecyclerView recyclerView, View emptyView, ContentLoadingProgressBar progress,
						RecyclerView.LayoutManager manager, L listener) {
		initData(recyclerView, emptyView, progress, listener, manager);
	}

	private void initData(RecyclerView recyclerView, View emptyView, ContentLoadingProgressBar progress, L listener,
						  RecyclerView.LayoutManager manager) {
		this.recycler = recyclerView;
		this.emptyView = emptyView;
		this.listener = listener;
		this.progress = progress;
		if (manager != null) {
			recycler.setLayoutManager(manager);
		} else {
			recycler.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
		}
		recycler.setHasFixedSize(false);
	}

	public void showLoading() {
		if (progress != null) {
			recycler.setVisibility(View.GONE);
			progress.show();
		}
	}

	public void paint(M data) {
		paint(false, data);
	}

	public void paint(boolean keepRecycler, M data) {
		if (progress != null) {
			progress.hide();
		}
		if (!keepRecycler && (data == null || data.isEmpty())) {
			recycler.setVisibility(View.GONE);
			if (emptyView != null) {
				emptyView.setVisibility(View.VISIBLE);
			}
		} else {
			recycler.setVisibility(View.VISIBLE);
			if (emptyView != null) {
				emptyView.setVisibility(View.GONE);
			}
			paintData(data);
		}

	}

	protected abstract void paintData(M data);
}
