package com.astaroth.mytestapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rperez on 14/12/15.
 */
public class MyListFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.view_list_layout, container, false);
		RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
		recyclerView.setAdapter(new NewsBodyAdapter());
		return v;
	}
	private class NewsBodyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
		public NewsBodyAdapter() {
			super();

		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.content_main, parent, false));
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

		}

		@Override
		public int getItemViewType(int position) {
			return 1;
		}

		@Override
		public int getItemCount() {
			return 100;
		}
		private class MainHolder extends RecyclerView.ViewHolder {
			public MainHolder(View itemView) {
				super(itemView);
			}
		}
	}
}
