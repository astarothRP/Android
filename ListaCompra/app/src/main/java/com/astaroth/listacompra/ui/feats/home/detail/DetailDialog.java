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
import com.astaroth.listacompra.domains.Detail;

import static com.astaroth.listacompra.domains.Constants.EMPTY_DOUBLE;
import static com.astaroth.listacompra.domains.Constants.EMPTY_INT;

class DetailDialog {

	static void createDetailDialog(Context context, Detail detail, DetailDialogListener listener) {
		final AlertDialog dialog = getDetailDialog(context, listener);
		dialog.show();
		DetailDialogHolder holder = new DetailDialogHolder(dialog, listener);
		holder.fillData(detail);
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
