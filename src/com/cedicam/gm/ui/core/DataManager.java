package com.cedicam.gm.ui.core;

import java.util.List;

import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;

public interface DataManager<T> extends 	DataProvider<T> {

	public void clear();
	public void updateData(T[] data, int nombreTotalResultat);
	public List<T> getData();
	public void setModel(ExtendedTableDataModel<T> model);
	public List<T> getFullData();
	public void doSort(String propertyName);
	public int getTotalResultSize();
	public void updateRow(T row);
}
