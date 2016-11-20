package com.astaroth.listacompra.ui.feats.home;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Collection;

import static com.astaroth.listacompra.domains.Collection.Type.*;

class HomeCollectionDialogHolder {

	private final ImageView bookImage;
	private final ImageView travelImage;
	private final ImageView filmImage;
	private final ImageView trolleyImage;
	private final ImageView musicImage;
	private final ImageView presentImage;
	private final ImageView foodImage;
	private final ImageView indefiniedImage;
	private final ImageView[] allImages;
	private final EditText description;
	private Collection.Type collectionType = null;
	private Collection collection = null;

	HomeCollectionDialogHolder(AlertDialog dialog, HomeCollectionDialog.HomeCollectionDialogListener listener) {
		bookImage = (ImageView) dialog.findViewById(R.id.collection_dialog_book_imageview);
		travelImage = (ImageView) dialog.findViewById(R.id.collection_dialog_travel_imageview);
		filmImage = (ImageView) dialog.findViewById(R.id.collection_dialog_film_imageview);
		trolleyImage = (ImageView) dialog.findViewById(R.id.collection_dialog_trolley_imageview);
		musicImage = (ImageView) dialog.findViewById(R.id.collection_dialog_music_imageview);
		presentImage = (ImageView) dialog.findViewById(R.id.collection_dialog_present_imageview);
		foodImage = (ImageView) dialog.findViewById(R.id.collection_dialog_food_imageview);
		indefiniedImage = (ImageView) dialog.findViewById(R.id.collection_dialog_indefinied_imageview);
		description = (EditText) dialog.findViewById(R.id.collection_dialog_name_edittext);
		allImages = new ImageView[] { bookImage, travelImage, filmImage, trolleyImage, musicImage, presentImage, foodImage, indefiniedImage };
		loadImageListeners();
		loadListener(dialog, listener);
	}

	void fillData(Collection collection) {
		this.collection = collection;
		if (!isNewCollection()) {
			description.setText(collection.descripcion);
			collectionType = collection.type;
			setImageSelected();
		}
	}

	private void loadImageListeners() {
		bookImage.setOnClickListener(new ImageOnClickListener(BOOK));
		travelImage.setOnClickListener(new ImageOnClickListener(TRAVEL));
		filmImage.setOnClickListener(new ImageOnClickListener(FILM));
		trolleyImage.setOnClickListener(new ImageOnClickListener(TROLLEY));
		musicImage.setOnClickListener(new ImageOnClickListener(MUSIC));
		presentImage.setOnClickListener(new ImageOnClickListener(PRESENT));
		foodImage.setOnClickListener(new ImageOnClickListener(FOOD));
		indefiniedImage.setOnClickListener(new ImageOnClickListener(INDEFINIED));
	}

	private void loadListener(final AlertDialog dialog,
							  final HomeCollectionDialog.HomeCollectionDialogListener listener) {
		View buttonOk = dialog.findViewById(R.id.collection_dialog_ok_layout);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadDataFromForm();
				if (checkData()) {
					dialog.dismiss();
					listener.onItemSaved(collection);
				} else {
					Snackbar.make(v, R.string.error_type_mandatory, Snackbar.LENGTH_LONG)
						.show();
				}
			}

			private boolean checkData() {
				return collection.type != null;
			}
		});
	}

	private void loadDataFromForm() {
		Editable editable = description.getText();
		if (isNewCollection()) {
			collection = new Collection(editable.toString(), collectionType);
		} else {
			collection.descripcion = editable.toString();
			collection.type = collectionType;
		}
	}

	private void setImageSelected() {
		Context context = bookImage.getContext();
		if (collectionType != null) {
			switch (collectionType) {
				case TRAVEL:
					travelImage.setBackground(
						ContextCompat.getDrawable(context, R.drawable.collection_selected_drawable));
					break;
				case FILM:
					filmImage.setBackground(
						ContextCompat.getDrawable(context, R.drawable.collection_selected_drawable));
					break;
				case TROLLEY:
					trolleyImage.setBackground(
						ContextCompat.getDrawable(context, R.drawable.collection_selected_drawable));
					break;
				case BOOK:
					bookImage.setBackground(
						ContextCompat.getDrawable(context, R.drawable.collection_selected_drawable));
					break;
				case MUSIC:
					musicImage.setBackground(
						ContextCompat.getDrawable(context, R.drawable.collection_selected_drawable));
					break;
				case PRESENT:
					presentImage.setBackground(
						ContextCompat.getDrawable(context, R.drawable.collection_selected_drawable));
					break;
				case FOOD:
					foodImage.setBackground(
						ContextCompat.getDrawable(context, R.drawable.collection_selected_drawable));
					break;
				default:
					indefiniedImage.setBackground(
						ContextCompat.getDrawable(context, R.drawable.collection_selected_drawable));
					break;
			}
		}
	}

	private boolean isNewCollection() {
		return collection == null;
	}

	private class ImageOnClickListener implements View.OnClickListener {

		private final Collection.Type collectionTypeListener;

		private ImageOnClickListener(Collection.Type type) {
			collectionTypeListener = type;
		}

		@Override
		public void onClick(View v) {
			collectionType = collectionTypeListener;
			v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.collection_selected_drawable));
			for (ImageView imageView : allImages) {
				if (!imageView.equals(v)) {
					imageView.setBackground(null);
				}
			}
		}
	}
}
