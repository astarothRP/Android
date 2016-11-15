package com.astaroth.listacompra.support.base;

import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astaroth.listacompra.instruments.inject.Injector;

public class BaseFragmentView {

	protected final ViewVisibilityHint visibilityHint;
	private final int layoutId;
	View view;
	private Injector viewContextInjector;

	public BaseFragmentView(@LayoutRes int layoutId) {
		this.layoutId = layoutId;
		visibilityHint = new ViewVisibilityHint();
	}

	void setUp(Injector viewContextInjector) {
		this.viewContextInjector = viewContextInjector;
	}

	void setUpFragmentView(LayoutInflater inflater, ViewGroup container) {
		this.view = inflater.inflate(layoutId, container, false);
		setUpView(view);
	}

	protected void setUpView(View view) {
	}

	protected boolean onBackIntercept() {
		return false;
	}

	protected <T> T viewContextInject(Class<T> tClass) {
		return viewContextInjector.inject(tClass);
	}

	public void showError(String error) {
		Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
	}
}
