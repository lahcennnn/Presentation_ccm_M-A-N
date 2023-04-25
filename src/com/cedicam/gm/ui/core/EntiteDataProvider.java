package com.cedicam.gm.ui.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.richfaces.model.ExtendedTableDataModel;

import com.cedicam.gm.bt.data.dto.GenericDto;
import com.cedicam.gm.re.data.dto.EntiteDto;

public class EntiteDataProvider<T extends GenericDto> extends DefaultDataProvider<T> implements DataManager<T> {

	public static final long serialVersionUID = 5989163624268384873L;

	public FiltreEntites filter = new FiltreEntites();
	public FiltreEntites getFilter() {
		return filter;
	}


	public ArrayList<EntiteDto> filteredData= new ArrayList<EntiteDto>();

	public void doSort(String propertyName) {
		sort.setSortProperty(propertyName);
		ArrayList al = filteredData;
		sort.trier(al);
		model.reset();
	}

	public void reSort() {
		if (null != sort.getSortProperty()) {
			ArrayList al = filteredData;
			sort.trier(al);
			model.reset();
		}
	}

	public void clear() {
		if (null != filteredData) {
			filteredData.clear();
		}
		filter.clear();
		super.clear();
	}

	public EntiteDataProvider() {
		super();
		filteredData = new ArrayList<EntiteDto>();
	}

	public void updateData(T[] data, int nbRes) {
		fullData.clear();
		filter.clear();
		if (null != data) {
			fullData.addAll(Arrays.asList(data));
			filteredData = filter.filtrer((List<EntiteDto>) fullData);
		}

		reSort();
		if (null != data) {
			scroller.update(data.length);
		}
		totalResultSize = nbRes;
	}

	public List<T> getData() {
		if (null == scroller || filteredData.size() == 0) {
			return (List<T>) filteredData;
		}
		
		int fromIndex = scroller.getFromElement();
		int toIndex = scroller.getToElement() + 1;
		
		if (toIndex > filteredData.size()) {
			toIndex = filteredData.size();
		}
		
		return (List<T>) filteredData.subList(fromIndex, toIndex);
	}


	public void filtrer() {		
		filteredData = filter.filtrer((List<EntiteDto>) fullData);		
		reSort();
		scroller.reset();
		// patch : le scroller doit se baser sur les données filtrées (affichées) et non sur le total ramené
		scroller.update(filteredData.size());
	}
}
