package com.astaroth.listacompra.ui.feats.home.detail;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Detail;
import com.astaroth.listacompra.support.ui.BaseRenderer;
import java.util.List;

import static com.astaroth.listacompra.domains.Constants.EMPTY_DOUBLE;
import static com.astaroth.listacompra.domains.Constants.EMPTY_INT;

class DetailRenderer extends BaseRenderer<List<Detail>, DetailRenderer.DetailRendererListener> {
	private DetailAdapter adapter;
	DetailRenderer(RecyclerView recyclerView, View emptyView, ContentLoadingProgressBar progress,
				   DetailRendererListener listener) {
		super(recyclerView, emptyView, progress, listener);
	}

	interface DetailRendererListener {
		void showEditDetail(Detail detail);
		void saveDetail(Detail detail);
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

	private class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
		private final int TYPE_ITEM = 1;
		private final int TYPE_EMPTY = 0;

		private final List<Detail> items;

		DetailAdapter(List<Detail> itemList) {
			this.items = itemList;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			if (viewType==TYPE_ITEM) {
				return new DetailHolder(LayoutInflater.from(parent.getContext())
											.inflate(R.layout.detail_item_layout, parent, false));
			} else {
				return new EmptyHolder(LayoutInflater.from(parent.getContext())
											.inflate(R.layout.detail_item_empty_layout, parent, false));
			}
		}

		@Override
		public int getItemViewType(int position) {
			if ((position+1)!=getItemCount()) {
				return TYPE_ITEM;
			} else {
				return TYPE_EMPTY;
			}
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			if ((position+1)!=getItemCount()) {
				((DetailHolder)holder).paint(items.get(position));
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
			for (int f = 0; f<items.size(); f++) {
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

	private class DetailHolder extends RecyclerView.ViewHolder {
		private TextView name;
		private TextView description;
		private TextView amount;
		private View amountLayout;
		private TextView quantity;
		private View quantityLayout;
		private View mark;
		private Detail detail;

		DetailHolder(View itemView) {
			super(itemView);
			name = (TextView) itemView.findViewById(R.id.detail_item_name_textview);
			description = (TextView) itemView.findViewById(R.id.detail_item_description_textview);
			amount = (TextView) itemView.findViewById(R.id.detail_item_amount_textview);
			quantity = (TextView) itemView.findViewById(R.id.detail_item_quantity_textview);
			amountLayout = itemView.findViewById(R.id.detail_item_amount_layout);
			quantityLayout = itemView.findViewById(R.id.detail_item_quantity_layout);
			mark = itemView.findViewById(R.id.detail_item_mark_imageview);
			itemView.findViewById(R.id.detail_item_mark_layout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					changeMark();
				}
			});
			itemView.findViewById(R.id.detail_item_texts_layout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.showEditDetail(detail);
				}
			});
			itemView.findViewById(R.id.detail_item_numbers_layout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.showEditDetail(detail);
				}
			});
		}

		private void changeMark() {
			if (detail.mark==1) {
				putMark(false);
			} else {
				putMark(true);
			}
			listener.saveDetail(detail);
		}

		private void putMark(boolean actived) {
			detail.mark = actived ? 1 : 0;
			putMarkInLayout(actived);
		}

		private void putMarkInLayout(boolean actived) {
			mark.setVisibility(actived ? View.VISIBLE : View.GONE);
		}

		void paint(Detail detail) {
			this.detail = detail;
			setItemText(detail.name, name);
			setItemText(detail.description, description);
			if (detail.amount!=EMPTY_DOUBLE) {
				amountLayout.setVisibility(View.VISIBLE);
				amount.setText(String.format("%1$.2f", detail.amount));
			} else {
				amountLayout.setVisibility(View.GONE);
			}
			if (detail.quantity!=EMPTY_INT) {
				quantityLayout.setVisibility(View.VISIBLE);
				quantity.setText(String.valueOf(detail.quantity));
			} else {
				quantityLayout.setVisibility(View.GONE);
			}
			putMarkInLayout(detail.mark==1);
		}
		void setItemText(String text, TextView textView) {
			if (text.isEmpty()) {
				textView.setVisibility(View.GONE);
			} else {
				textView.setVisibility(View.VISIBLE);
				textView.setText(text);
			}
		}
	}
}
