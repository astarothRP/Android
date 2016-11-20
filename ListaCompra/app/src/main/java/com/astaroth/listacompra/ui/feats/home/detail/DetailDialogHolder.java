package com.astaroth.listacompra.ui.feats.home.detail;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Detail;

import static com.astaroth.listacompra.domains.Constants.EMPTY_DOUBLE;
import static com.astaroth.listacompra.domains.Constants.EMPTY_INT;

class DetailDialogHolder {
	private final EditText name;
	private final EditText description;
	private final EditText quantity;
	private final EditText amount;
	private final ImageView marked;
	private Detail detail = null;

	DetailDialogHolder(AlertDialog dialog, DetailDialog.DetailDialogListener listener){
		name = (EditText) dialog.findViewById(R.id.detail_dialog_name_edittext);
		description = (EditText) dialog.findViewById(R.id.detail_dialog_description_edittext);
		quantity = (EditText) dialog.findViewById(R.id.detail_dialog_quantity_edittext);
		amount = (EditText) dialog.findViewById(R.id.detail_dialog_amount_edittext);
		marked = (ImageView) dialog.findViewById(R.id.detail_dialog_mark_imageview);
		loadListeners(dialog, listener);
	}

	void fillData(Detail detail) {
		this.detail = detail;
		if (!isNewDetail()) {
			name.setText(detail.name);
			description.setText(detail.description);
			if (detail.quantity != EMPTY_INT) {
				quantity.setText(String.valueOf(detail.quantity));
			}
			if (detail.amount != EMPTY_DOUBLE) {
				amount.setText(String.valueOf(detail.amount));
			}
			marked.setVisibility((detail.mark == 1) ? View.VISIBLE : View.GONE);
		}
	}

	private void loadListeners(final AlertDialog dialog, final DetailDialog.DetailDialogListener listener) {
		View buttonOk = dialog.findViewById(R.id.collection_dialog_ok_layout);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadData();
				if (checkData()) {
					dialog.dismiss();
					listener.onItemSaved(detail);
				} else {
					Snackbar.make(v, R.string.error_name_mandatory, Snackbar.LENGTH_LONG)
						.show();
				}
			}

			private boolean checkData() {
				return !detail.name.isEmpty();
			}
		});
		View markLayout = dialog.findViewById(R.id.detail_dialog_mark_layout);
		markLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView marked = (ImageView) dialog.findViewById(R.id.detail_dialog_mark_imageview);
				marked.setVisibility(marked.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
			}
		});
		View delete = dialog.findViewById(R.id.detail_dialog_delete_imageview);
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				listener.delete(detail);
			}
		});
		delete.setVisibility(isNewDetail() ? View.GONE : View.VISIBLE);
	}

	private void loadData(){
		if (isNewDetail()) {
			detail = new Detail(getStringValue(description), getStringValue(name), getIntValue(quantity), 0L,
								getDoubleValue(amount), getBooleanValue(marked));
		} else {
			detail.description = getStringValue(description);
			detail.name = getStringValue(name);
			detail.quantity = getIntValue(quantity);
			detail.amount = getDoubleValue(amount);
			detail.mark = getBooleanValue(marked);
		}
	}

	private boolean isNewDetail() {
		return detail == null;
	}

	private String getStringValue(EditText editText) {
		return getEditable(editText).toString();
	}

	private int getIntValue(EditText editText) {
		String stringValue = getStringValue(editText);
		if (stringValue.isEmpty()) {
			return EMPTY_INT;
		}
		return Integer.parseInt(stringValue);
	}

	private double getDoubleValue(EditText editText) {
		String stringValue = getStringValue(editText);
		if (stringValue.isEmpty()) {
			return EMPTY_DOUBLE;
		}
		return Double.parseDouble(stringValue);
	}

	private Editable getEditable(EditText editText) {
		return editText.getText();
	}

	private int getBooleanValue(View view) {
		return view.getVisibility() == View.VISIBLE ? 1 : 0;
	}
}
