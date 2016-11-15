package com.astaroth.listacompra.support.ui;

import com.astaroth.listacompra.R;
import com.astaroth.listacompra.domains.Collection;


public class CollectionTypeUtil {
	public static int getResourceByType(Collection.Type type) {
		switch (type) {
			case BOOK:
				return R.drawable.ico_libro;
			case FILM:
				return R.drawable.ico_peli;
			case TRAVEL:
				return R.drawable.ico_viaje;
			case TROLLEY:
				return R.drawable.ico_compra;
			case FOOD:
				return R.drawable.ico_comida;
			case MUSIC:
				return R.drawable.ico_musica;
			case PRESENT:
				return R.drawable.ico_regalo;
			case INDEFINIED:
				return R.drawable.ico_varios;
		}
		return 0;
	}
}
