package com.cedicam.gm.ui.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.richfaces.model.ExtendedTableDataModel;

import com.cedicam.gm.bt.data.dto.GenericDto;

public class DefaultDataProvider<T extends GenericDto> implements DataManager<T> {
	public static final long serialVersionUID = 7614740152977457039L;
	protected ArrayList<T> fullData;
	protected int totalResultSize;

	protected DatatableScroller<T> scroller = new DatatableScroller<T>();

	protected Sort sort;
	protected ExtendedTableDataModel<T> model;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doSort(String propertyName){
		sort.setSortProperty(propertyName);
		ArrayList al = fullData;
		sort.trier(al);
		model.reset();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void reSort(){
		if(sort.getSortProperty()==null)return;
		ArrayList al = fullData;
		sort.trier(al);
		model.reset();
	}

	public int getTotalResultSize() {
		return totalResultSize;
	}
	public DatatableScroller<T> getScroller() {		return scroller;	}
	public Sort getSort() {		return sort;	}

	public void clear(){
		if(fullData!=null) fullData.clear();
		if(model!=null)model.reset();
		if (scroller != null) {
			scroller.reset();
		}
		totalResultSize = 0;
		sort.clear();
	}

	public DefaultDataProvider(){
		fullData = new ArrayList<T>();
		sort = new Sort();

	}

	public void updateData(T[] data, int nbRes){
		fullData.clear();
		if(data!=null) fullData.addAll(Arrays.asList(data));
		reSort();
		if(data!=null) scroller.update(data.length);
		totalResultSize = nbRes;
	}

	public List<T> getData() {
		if(scroller==null || fullData.size()==0)return fullData;
		int fromIndex = scroller.getFromElement();
		int toIndex = scroller.getToElement();
		return fullData.subList(fromIndex, toIndex+1);

	}

	@Override
	public T getItemByKey(Object key) {
		if (key != null) {
			for (final T element : getData()) {
				if (key.equals(getKey(element))) return element;
			}
		}
		return null;
	}

	@Override
	public List<T> getItemsByRange(int fromIndex, int toIndex) {
		if(getData()==null)return new ArrayList<T>();
		return getData().subList(fromIndex, toIndex);
	}

	@Override
	public Object getKey(T obj) {
		if(obj!=null)return obj.getId();
		return null;
	}

	@Override
	public int getRowCount() {
		if(null==getData())return 0;
		return getData().size();
	}

	@Override
	public ArrayList<T> getFullData() {
		return fullData;
	}

	public ExtendedTableDataModel<T> getModel() {
		return model;
	}

	public void setModel(ExtendedTableDataModel<T> model) {
		this.model = model;
		if(scroller!=null)scroller.setModel(model);
	}

	@Override
	public void updateRow(T row) {
		for(int i=0;i<fullData.size();i++){
			if (fullData.get(i).equals(row)){
				fullData.set(i, row);
				//reSort();
				model.reset();
				return;
			}
		}
		return;

	}
}
