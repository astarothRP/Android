package com.astaroth.listacompra.ui.feats.home.detail;

import com.astaroth.listacompra.core.trolley.Trolley;
import com.astaroth.listacompra.domains.Detail;
import com.astaroth.listacompra.support.base.BaseInteractor;
import com.wokdsem.kommander.Action;
import com.wokdsem.kommander.Kommand;
import java.util.List;

public class DetaiInteractor extends BaseInteractor {

	Kommand<List<Detail>> getAllDetails(final long collection) {
		return kommander.makeKommand(new Action<List<Detail>>() {
			@Override
			public List<Detail> kommandAction() throws Throwable {
				return inject(Trolley.class).getDetails(collection);
			}
		})
			.setTag(tag);
	}

	Kommand<Long> createDetail(final Detail detail) {
		return kommander.makeKommand(new Action<Long>() {
			@Override
			public Long kommandAction() throws Throwable {
				return inject(Trolley.class).createDetail(detail);
			}
		})
			.setTag(tag);
	}

	Kommand<Void> updateDetail(final Detail detail) {
		return kommander.makeKommand(new Action<Void>() {
			@Override
			public Void kommandAction() throws Throwable {
				inject(Trolley.class).updateDetail(detail);
				return null;
			}
		})
			.setTag(tag);
	}

	Kommand<Void> deleteDetail(final long idDetail) {
		return kommander.makeKommand(new Action<Void>() {
			@Override
			public Void kommandAction() throws Throwable {
				inject(Trolley.class).deleteDetail(idDetail);
				return null;
			}
		})
			.setTag(tag);
	}
}
