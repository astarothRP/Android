package com.astaroth.listacompra.ui.feats.home.detail;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Detail;
import com.astaroth.listacompra.support.ui.BaseRenderer;
import java.util.List;

class DetailRenderer extends BaseRenderer<List<Detail>, DetailRenderer.DetailRendererListener> {

	private DetailAdapter adapter;

	DetailRenderer(RecyclerView recyclerView, View emptyView, ContentLoadingProgressBar progress,
				   DetailRendererListener listener) {
		super(recyclerView, emptyView, progress, listener);
	}

	@Override
	protected void paintData(List<Detail> itemList) {
		adapter = new DetailAdapter(itemList);
		recycler.setAdapter(adapter);
	}

	void addDetail(Detail detail) {
		adapter.addDetail(detail);
		adapter.notifyDataSetChanged();
	}

	void updateDetail(Detail detail) {
		adapter.updateDetail(detail);
		adapter.notifyDataSetChanged();
	}

	void deleteDetail(long idDetail) {
		adapter.deleteDetail(idDetail);
		adapter.notifyDataSetChanged();
	}

	interface DetailRendererListener {

		void showEditDetail(Detail detail);

		void saveDetail(Detail detail);
	}

	private class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

		private final int TYPE_ITEM = 1;
		private final int TYPE_EMPTY = 0;

		private final List<Detail> items;

		DetailAdapter(List<Detail> itemList) {
			this.items = itemList;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			if (viewType == TYPE_ITEM) {
				View itemView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.detail_item_layout, parent, false);
				return new DetailRendererHolder(itemView, listener);
			} else {
				return new EmptyHolder(LayoutInflater.from(parent.getContext())
										   .inflate(R.layout.detail_item_empty_layout, parent, false));
			}
		}

		@Override
		public int getItemViewType(int position) {
			if ((position + 1) != getItemCount()) {
				return TYPE_ITEM;
			} else {
				return TYPE_EMPTY;
			}
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			if ((position + 1) != getItemCount()) {
				((DetailRendererHolder) holder).paint(items.get(position));
			}
		}

		void updateDetail(Detail detail) {
			for (Detail details : items) {
				if (detail.id == details.id) {
					details = detail;
				}
			}
		}

		void addDetail(Detail detail) {
			items.add(detail);
		}

		@Override
		public int getItemCount() {
			return items.size() + 1;
		}

		void deleteDetail(long idDetail) {
			Detail detail;
			for (int f = 0; f < items.size(); f++) {
				detail = items.get(f);
				if (idDetail == detail.id) {
					items.remove(f);
				}
			}
		}
	}

	private class EmptyHolder extends RecyclerView.ViewHolder {

		EmptyHolder(View itemView) {
			super(itemView);
		}
	}
}
