package com.cedicam.gm.ui.core;

public abstract class DatatableFilter<T> {

	public abstract boolean keep(T data);

	public void reset(){	};
}
