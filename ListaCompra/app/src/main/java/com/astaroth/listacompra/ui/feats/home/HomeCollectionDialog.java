package com.astaroth.listacompra.ui.feats.home;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Collection;

class HomeCollectionDialog {

	static void createCollectionDialog(Context context, Collection collection, HomeCollectionDialogListener listener) {
		final AlertDialog dialog = getCollectionDialog(context);
		dialog.show();
		HomeCollectionDialogHolder holder = new HomeCollectionDialogHolder(dialog, listener);
		holder.fillData(collection);
	}

	private static AlertDialog getCollectionDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(R.layout.collection_dialog_layout);
		return builder.create();
	}

	interface HomeCollectionDialogListener {

		void onItemSaved(Collection collection);
	}
}
