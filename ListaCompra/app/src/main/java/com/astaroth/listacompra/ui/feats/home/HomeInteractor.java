package com.astaroth.listacompra.ui.feats.home;

import com.astaroth.listacompra.core.trolley.Trolley;
import com.astaroth.listacompra.domains.Collection;
import com.astaroth.listacompra.support.base.BaseInteractor;
import com.wokdsem.kommander.Action;
import com.wokdsem.kommander.Kommand;
import java.util.List;

class HomeInteractor extends BaseInteractor {

	Kommand<List<Collection>> getAllCollections() {
		return kommander.makeKommand(new Action<List<Collection>>() {
			@Override
			public List<Collection> kommandAction() throws Throwable {
				return inject(Trolley.class).getCollections();
			}
		})
			.setTag(tag);
	}

	Kommand<Void> deleteCollection(final long collection) {
		return kommander.makeKommand(new Action<Void>() {
			@Override
			public Void kommandAction() throws Throwable {
				inject(Trolley.class).deleteCollection(collection);
				return null;
			}
		})
			.setTag(tag);
	}

	Kommand<Void> updateCollection(final Collection collection) {
		return kommander.makeKommand(new Action<Void>() {
			@Override
			public Void kommandAction() throws Throwable {
				inject(Trolley.class).updateCollection(collection);
				return null;
			}
		})
			.setTag(tag);
	}

	Kommand<Long> createCollection(final Collection collection) {
		return kommander.makeKommand(new Action<Long>() {
			@Override
			public Long kommandAction() throws Throwable {
				return inject(Trolley.class).createCollection(collection);
			}
		})
			.setTag(tag);
	}

}
