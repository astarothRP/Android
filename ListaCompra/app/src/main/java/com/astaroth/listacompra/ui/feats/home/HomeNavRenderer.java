package com.astaroth.listacompra.ui.feats.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.support.domain.CollectionToShow;
import com.astaroth.listacompra.support.ui.BaseRenderer;
import com.astaroth.listacompra.support.ui.CollectionTypeUtil;
import java.util.List;

class HomeNavRenderer extends BaseRenderer<List<Collection>, HomeNavRenderer.HomeNavCallback> {

	private HomeNavAdapter adapter;
	private Collection itemViewed;

	HomeNavRenderer(RecyclerView recyclerView, HomeNavCallback callback) {
		super(recyclerView, null, null, callback);
	}

	@Override
	protected void paintData(List<Collection> data) {
		adapter = new HomeNavAdapter(data);
		recycler.setAdapter(adapter);
	}

	void addCollection(Collection collection) {
		adapter.addColection(collection);
		adapter.notifyDataSetChanged();
	}

	void updateCollection(Collection collection) {
		adapter.updateCollection(collection);
		adapter.notifyDataSetChanged();
	}

	CollectionToShow deleteAndGetNextCollection(long idCollection) {
		CollectionToShow collectionToShow = adapter.deleteAndGetNextCollection(idCollection);
		adapter.notifyDataSetChanged();
		return collectionToShow;
	}

	void setCollectionViewed(Collection collection) {
		itemViewed = collection;
	}

	interface HomeNavCallback {

		void onClickItem(Collection collection);

		void onAddCollectionClick();

	}

	private class HomeNavAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

		private final List<Collection> items;
		private final int FOOTER = 2;
		private final int ITEM = 1;
		private final int HEADER = 0;
		private int footerPosition;

		private HomeNavAdapter(List<Collection> items) {
			this.items = items;
			footerPosition = items.size() + 1;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			if (viewType == HEADER) {
				return new HomeNavHeaderHolder(getLoadingView(parent, R.layout.home_drawer_header_item_view));
			} else if (viewType == FOOTER) {
				return new HomeNavFooterHolder(getLoadingView(parent, R.layout.home_drawer_footer_item_view));
			} else {
				return new HomeNavHolder(getLoadingView(parent, R.layout.home_drawer_item_view));
			}
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
			if (position != footerPosition && position != 0) {
				((HomeNavHolder) holder).paint(items.get(position - 1));
			}
		}

		@Override
		public int getItemCount() {
			return items.size() + 2;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == 0) {
				return HEADER;
			} else if (position == footerPosition) {
				return FOOTER;
			} else {
				return ITEM;
			}
		}

		void updateCollection(Collection collection) {
			for (Collection collections : items) {
				if (collection.id == collections.id) {
					collections = collection;
				}
			}
		}

		void addColection(Collection collection) {
			items.add(collection);
			footerPosition++;
		}

		private View getLoadingView(ViewGroup parent, int layout) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			return inflater.inflate(layout, parent, false);
		}

		CollectionToShow deleteAndGetNextCollection(long idCollection) {
			Collection collection;
			for (int f = 0; f < items.size(); f++) {
				collection = items.get(f);
				if (idCollection == collection.id) {
					items.remove(f);
					footerPosition--;
					if (f != 0) {
						return CollectionToShow.collection(items.get(f - 1));
					} else if (items.size() > 0) {
						return CollectionToShow.collection(items.get(f));
					} else {
						return CollectionToShow.emptyCollection();
					}
				}
			}
			return CollectionToShow.errorCollection();
		}
	}

	private class HomeNavHolder extends RecyclerView.ViewHolder {

		private Collection collection;
		private TextView text;
		private ImageView icon;

		private HomeNavHolder(View view) {
			super(view);
			icon = (ImageView) view.findViewById(R.id.home_drawer_item_imageview);
			text = (TextView) view.findViewById(R.id.home_drawer_item_description);
			View itemLayout = view.findViewById(R.id.home_drawer_item_layout);
			itemLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isItemViewed()) {
						listener.onClickItem(collection);
					}
				}

				private boolean isItemViewed() {
					return itemViewed != null && collection.id == itemViewed.id;
				}
			});
		}

		private void paint(Collection collection) {
			this.collection = collection;
			text.setText(collection.descripcion);
			icon.setVisibility(View.VISIBLE);
			icon.setBackgroundResource(CollectionTypeUtil.getResourceByType(collection.type));
		}
	}

	private class HomeNavFooterHolder extends RecyclerView.ViewHolder {

		private HomeNavFooterHolder(View view) {
			super(view);
			view.findViewById(R.id.home_drawer_footer_layout)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						listener.onAddCollectionClick();
					}
				});
		}
	}

	private class HomeNavHeaderHolder extends RecyclerView.ViewHolder {

		private HomeNavHeaderHolder(View view) {
			super(view);
		}
	}
}
