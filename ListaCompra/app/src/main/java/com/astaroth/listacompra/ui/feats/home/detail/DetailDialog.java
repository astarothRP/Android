package com.astaroth.listacompra.ui.feats.home.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.domains.Detail;

import static com.astaroth.listacompra.domains.Constants.EMPTY_DOUBLE;
import static com.astaroth.listacompra.domains.Constants.EMPTY_INT;

class DetailDialog {

	static Collection.Type collectionType = Collection.Type.TROLLEY;

	static void createDetailDialog(Context context, Detail detail, DetailDialogListener listener) {
		final AlertDialog dialog = getDetailDialog(context, listener);
		dialog.show();
		if (!isNewDetail(detail)) {
			fillFormWithDetail(detail, dialog);
		}
		setListeners(detail, dialog, listener);
	}

	private static boolean isNewDetail(Detail detail) {
		return detail == null;
	}

	private static void fillFormWithDetail(Detail detail, AlertDialog dialog) {
		EditText name = (EditText) dialog.findViewById(R.id.detail_dialog_name_edittext);
		EditText description = (EditText) dialog.findViewById(R.id.detail_dialog_description_edittext);
		EditText quantity = (EditText) dialog.findViewById(R.id.detail_dialog_quantity_edittext);
		EditText amount = (EditText) dialog.findViewById(R.id.detail_dialog_amount_edittext);
		ImageView marked = (ImageView) dialog.findViewById(R.id.detail_dialog_mark_imageview);
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

	private static void setListeners(final Detail detail, final AlertDialog dialog,
									 final DetailDialogListener listener) {
		View buttonOk = dialog.findViewById(R.id.collection_dialog_ok_layout);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Detail newDetail = fillDetailWithForm(detail, dialog);
				if (checkDetail(newDetail)) {
					dialog.dismiss();
					listener.onItemSaved(newDetail);
				} else {
					Snackbar.make(v, R.string.error_name_mandatory, Snackbar.LENGTH_LONG)
						.show();
				}
			}

			private boolean checkDetail(Detail newDetail) {
				return !newDetail.name.isEmpty();
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
		delete.setVisibility(isNewDetail(detail) ? View.GONE : View.VISIBLE);
	}

	private static Detail fillDetailWithForm(Detail detail, AlertDialog dialog) {
		EditText name = (EditText) dialog.findViewById(R.id.detail_dialog_name_edittext);
		EditText description = (EditText) dialog.findViewById(R.id.detail_dialog_description_edittext);
		EditText quantity = (EditText) dialog.findViewById(R.id.detail_dialog_quantity_edittext);
		EditText amount = (EditText) dialog.findViewById(R.id.detail_dialog_amount_edittext);
		ImageView marked = (ImageView) dialog.findViewById(R.id.detail_dialog_mark_imageview);
		if (isNewDetail(detail)) {
			detail = new Detail(getStringValue(description), getStringValue(name), getIntValue(quantity), 0L,
								getDoubleValue(amount), getBooleanValue(marked));
		} else {
			detail.description = getStringValue(description);
			detail.name = getStringValue(name);
			detail.quantity = getIntValue(quantity);
			detail.amount = getDoubleValue(amount);
			detail.mark = getBooleanValue(marked);
		}
		return detail;
	}

	private static String getStringValue(EditText editText) {
		return getEditable(editText).toString();
	}

	private static int getIntValue(EditText editText) {
		String stringValue = getStringValue(editText);
		if (stringValue.isEmpty()) {
			return EMPTY_INT;
		}
		return Integer.parseInt(stringValue);
	}

	private static double getDoubleValue(EditText editText) {
		String stringValue = getStringValue(editText);
		if (stringValue.isEmpty()) {
			return EMPTY_DOUBLE;
		}
		return Double.parseDouble(stringValue);
	}

	private static Editable getEditable(EditText editText) {
		return editText.getText();
	}

	private static int getBooleanValue(View view) {
		return view.getVisibility() == View.VISIBLE ? 1 : 0;
	}

	private static AlertDialog getDetailDialog(Context context, final DetailDialogListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(R.layout.detail_dialog_layout);
		builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				listener.onDismis();
			}
		});
		return builder.create();
	}

	interface DetailDialogListener {

		void onItemSaved(Detail detail);

		void delete(Detail detail);

		void onDismis();
	}
}
