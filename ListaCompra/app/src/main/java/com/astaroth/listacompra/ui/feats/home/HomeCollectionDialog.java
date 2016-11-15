package com.astaroth.listacompra.ui.feats.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Collection;

import static com.astaroth.listacompra.domains.Collection.Type.*;

class HomeCollectionDialog {

	static Collection.Type collectionType = Collection.Type.TROLLEY;

	static void createCollectionDialog(Context context, Collection collection, HomeCollectionDialogListener listener) {
		final AlertDialog dialog = getCollectionDialog(context);
		dialog.show();
		setDescriptionText(collection, (EditText) dialog.findViewById(R.id.collection_dialog_name_edittext));
		setImageType(collection, dialog);
		setButtonListener(collection, dialog, listener);
	}

	private static void setImageType(Collection collection, AlertDialog dialog) {
		ImageView bookImage = (ImageView) dialog.findViewById(R.id.collection_dialog_book_imageview);
		ImageView travelImage = (ImageView) dialog.findViewById(R.id.collection_dialog_travel_imageview);
		ImageView filmImage = (ImageView) dialog.findViewById(R.id.collection_dialog_film_imageview);
		ImageView trolleyImage = (ImageView) dialog.findViewById(R.id.collection_dialog_trolley_imageview);
		ImageView musicImage = (ImageView) dialog.findViewById(R.id.collection_dialog_music_imageview);
		ImageView presentImage = (ImageView) dialog.findViewById(R.id.collection_dialog_present_imageview);
		ImageView foodImage = (ImageView) dialog.findViewById(R.id.collection_dialog_food_imageview);
		ImageView indefiniedImage = (ImageView) dialog.findViewById(R.id.collection_dialog_indefinied_imageview);
		setImageListeners(bookImage, travelImage, filmImage, trolleyImage, musicImage, presentImage, foodImage,
						  indefiniedImage);
		if (collection != null) {
			collectionType = collection.type;
		} else {
			collectionType = null;
		}
		setImageSelected(bookImage, travelImage, filmImage, trolleyImage, musicImage, presentImage, foodImage,
						 indefiniedImage);
	}

	private static void setImageListeners(ImageView bookImg, ImageView travelImg, ImageView filmImg,
										  ImageView trolleyImg, ImageView musicImage, ImageView presentImage,
										  ImageView foodImage, ImageView indefiniedImage) {
		bookImg.setOnClickListener(new ImageOnClickListener(BOOK,
															new ImageView[] { travelImg, filmImg, trolleyImg, musicImage, presentImage, foodImage, indefiniedImage }));
		travelImg.setOnClickListener(new ImageOnClickListener(TRAVEL,
															  new ImageView[] { bookImg, filmImg, trolleyImg, musicImage, presentImage, foodImage, indefiniedImage }));
		filmImg.setOnClickListener(new ImageOnClickListener(FILM,
															new ImageView[] { bookImg, travelImg, trolleyImg, musicImage, presentImage, foodImage, indefiniedImage }));
		trolleyImg.setOnClickListener(new ImageOnClickListener(TROLLEY,
															   new ImageView[] { bookImg, travelImg, filmImg, musicImage, presentImage, foodImage, indefiniedImage }));
		musicImage.setOnClickListener(new ImageOnClickListener(MUSIC,
															   new ImageView[] { bookImg, travelImg, filmImg, trolleyImg, presentImage, foodImage, indefiniedImage }));
		presentImage.setOnClickListener(new ImageOnClickListener(PRESENT,
																 new ImageView[] { bookImg, travelImg, filmImg, trolleyImg, musicImage, foodImage, indefiniedImage }));
		foodImage.setOnClickListener(new ImageOnClickListener(FOOD,
															  new ImageView[] { bookImg, travelImg, filmImg, trolleyImg, musicImage, presentImage, indefiniedImage }));
		indefiniedImage.setOnClickListener(new ImageOnClickListener(INDEFINIED,
																	new ImageView[] { bookImg, travelImg, filmImg, trolleyImg, musicImage, presentImage, foodImage }));
	}

	private static void setImageSelected(ImageView bookImage, ImageView travelImage, ImageView filmImage,
										 ImageView trolleyImage, ImageView musicImage, ImageView presentImage,
										 ImageView foodImage, ImageView indefiniedImage) {
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

	private static void setDescriptionText(Collection collection, EditText descriptionEditText) {
		if (collection != null) {
			descriptionEditText.setText(collection.descripcion);
		}
	}

	private static void setButtonListener(final Collection collection, final AlertDialog dialog,
										  final HomeCollectionDialogListener listener) {
		View buttonOk = dialog.findViewById(R.id.collection_dialog_ok_layout);
		buttonOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText description = (EditText) dialog.findViewById(R.id.collection_dialog_name_edittext);
				Collection newColection = fillData(collection, description);
				dialog.dismiss();
				listener.onItemSaved(newColection);
			}
		});
	}

	private static Collection fillData(Collection collection, EditText description) {
		Editable editable = description.getText();
		if (collection == null) {
			collection = new Collection(editable.toString(), collectionType);
		} else {
			collection.descripcion = editable.toString();
			collection.type = collectionType;
		}
		return collection;
	}

	private static AlertDialog getCollectionDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(R.layout.collection_dialog_layout);
		return builder.create();
	}

	interface HomeCollectionDialogListener {

		void onItemSaved(Collection collection);
	}

	private static class ImageOnClickListener implements View.OnClickListener {

		private final ImageView[] imagesToUnselect;
		private final Collection.Type collectionTypeListener;

		private ImageOnClickListener(Collection.Type type, ImageView[] imagesToUnselect) {
			this.imagesToUnselect = imagesToUnselect;
			collectionTypeListener = type;
		}

		@Override
		public void onClick(View v) {
			collectionType = collectionTypeListener;
			v.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.collection_selected_drawable));
			for (ImageView imageView : imagesToUnselect) {
				imageView.setBackground(null);
			}
		}
	}
	
}
