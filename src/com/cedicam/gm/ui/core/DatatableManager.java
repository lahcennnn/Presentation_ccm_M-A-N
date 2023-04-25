package com.cedicam.gm.ui.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.SimpleSelection;

import com.cedicam.gm.bt.data.dto.GenericDto;

public class DatatableManager<T extends GenericDto> {
	private DataManager<T> dataProvider;
	private ExtendedTableDataModel<T> datatableModel;
	private SimpleSelection selection = new SimpleSelection();
	private T ligneTotal;
	private T selectedObject = null;

	@SuppressWarnings("rawtypes")
	List<DatatableManager> dtManagersToClearSelection = new ArrayList<DatatableManager>();

    public static ExtendedTableDataModel<GenericDto> getDataModel(GenericDto dto){
    	DefaultDataProvider<GenericDto> provider = new DefaultDataProvider<GenericDto>();
    	ExtendedTableDataModel<GenericDto> res = new ExtendedTableDataModel<GenericDto>(provider);
    	provider.setModel(res);
    	provider.updateData(new GenericDto[]{dto}, 1);

    	res.reset();
    	return res;
    }


	public boolean isHasRowSelected() {
		boolean res =  selection!=null && selection.size()>0;
		return res;
	}

	public final T getSelectedOccurrence() {
		if (selection != null && datatableModel != null) {
			Iterator<Object> iterator = selection.getKeys();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				selectedObject = datatableModel.getObjectByKey(key);
			}
		}
		return selectedObject;
	}

	public void clearSelection() {
		selection = new SimpleSelection();
		selectedObject=null;
	}

	@SuppressWarnings("rawtypes")
	public void selectRow(){
		selectedObject = getSelectedOccurrence();
		for (DatatableManager mng : dtManagersToClearSelection) {
			mng.clearSelection();
		}
/*		System.out.println(getDataProvider().getTotalResultSize());
		System.out.println(isHasRowSelected());
	*/}

	public DatatableManager(DataManager<T> _dataProvider, T _total){
		super();
		dataProvider = _dataProvider;
		datatableModel = new ExtendedTableDataModel<T>(dataProvider);
		dataProvider.setModel(datatableModel);
		ligneTotal = _total;
	}

	public void updateData(T[] data, int nombreTotalResultat){
		dataProvider.updateData(data, nombreTotalResultat);
		ligneTotal.clear();
		ligneTotal.fillWithTotal(data, nombreTotalResultat);
	}

	public void updateRow(T row){
		dataProvider.updateRow(row);
	}

	public void reset(){
		dataProvider.clear();
		selection.clear();
		selectedObject = null;
	}

	public DataManager<T> getDataProvider() {		return dataProvider;	}
	public ExtendedTableDataModel<T> getDatatableModel() {
		return datatableModel;
	}
	public void setDatatableModel(ExtendedTableDataModel<T> datatableModel) {		this.datatableModel = datatableModel;	}
	public SimpleSelection getSelection() {		return selection;	}
	public void setSelection(SimpleSelection selection) {		this.selection = selection;	}
	public void setDataProvider(DataManager<T> dataProvider) {		this.dataProvider = dataProvider;	}
	public T getLigneTotal() {		return ligneTotal;	}
	public void setLigneTotal(T ligneTotal) {		this.ligneTotal = ligneTotal;	}
	public void setSelectedObject(T selectedObject) {		this.selectedObject = selectedObject;	}
	public DataManager<T> getDataManager() {		return dataProvider;	}
	public T getSelectedObject() {		return selectedObject;	}
	@SuppressWarnings("rawtypes")
	public List<DatatableManager> getDtManagersToClearSelection() {		return dtManagersToClearSelection;	}
	@SuppressWarnings("rawtypes")
	public void setDtManagersToClearSelection(List<DatatableManager> dtManagersToClearSelection) {this.dtManagersToClearSelection = dtManagersToClearSelection;	}


}
