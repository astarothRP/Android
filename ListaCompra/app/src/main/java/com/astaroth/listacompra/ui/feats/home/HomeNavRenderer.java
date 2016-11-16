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

	}

	private class HomeNavAdapter extends RecyclerView.Adapter<HomeNavHolder> {

		private final List<Collection> items;

		private HomeNavAdapter(List<Collection> items) {
			this.items = items;
		}

		@Override
		public HomeNavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
				return new HomeNavHolder(getLoadingView(parent, R.layout.home_drawer_item_view));
		}

		@Override
		public void onBindViewHolder(HomeNavHolder holder, int position) {
				holder.paint(items.get(position));
		}

		@Override
		public int getItemCount() {
			return items.size();
		}

		void updateCollection(Collection newCollection) {
			Collection collection;
			for (int f = 0; f < items.size(); f++) {
				collection = items.get(f);
				if (collection.id == newCollection.id) {
					items.remove(f);
					items.add(f, newCollection);
				}
			}
		}

		void addColection(Collection collection) {
			items.add(collection);
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
}
