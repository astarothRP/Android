package com.astaroth.listacompra.ui.feats.home.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Detail;

import static com.astaroth.listacompra.domains.Constants.EMPTY_DOUBLE;
import static com.astaroth.listacompra.domains.Constants.EMPTY_INT;

class DetailRendererHolder extends RecyclerView.ViewHolder {
	private TextView name;
	private TextView description;
	private TextView amount;
	private View amountLayout;
	private TextView quantity;
	private View quantityLayout;
	private View mark;
	private Detail detail;
	private final DetailRenderer.DetailRendererListener listener;

	DetailRendererHolder(View itemView, DetailRenderer.DetailRendererListener detailRendererListener) {
		super(itemView);
		this.listener = detailRendererListener;
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
