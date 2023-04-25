package com.cedicam.gm.ui.core;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.richfaces.model.ExtendedTableDataModel;


public class DatatableScroller<T> {
	/** Nombre d'�l�ments par page. */
	private int pageSize = 25;
	
	/**
	 * Retourne le nombre d'�l�ments par page.
	 * @return Le nombre d'�l�ments par page.
	 */
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		update();
	}

	/** La page courante. */
	private int currentPage = 1;
	
	/**
	 * D�finit la page courante.
	 * @param _currentPage La page courante.
	 */
	public void setCurrentPage(Integer _currentPage) {
		if (_currentPage != null) {
			gotoPage(_currentPage);
		}
		//updateModel();
	}
	
	/** Le nombre total d'�l�ments. */
	private int numberOfElements = 0;
	
	/**
	 * Retourne le nombre total d'�l�ments.
	 * @return Le nombre total d'�l�ments.
	 */
	public int getNumberOfElements() {
		return numberOfElements;
	}

	/** Le nombre de page list�es. */
	private int numberOfPageListed = 5;
	
	/** Le mod�le. */
	private ExtendedTableDataModel<T> model;
	
	/** Mettre � jour le mod�le. */
	private void updateModel() {
		model.reset();
	}

	/** Remettre le scroller � z�ro. */
	public void reset() {
		currentPage = 1;
		updateModel();
	}

	/**
	 * Retourne la page courante.
	 * @return La page courante.
	 */
	public Integer getCurrentPage() {
		return currentPage;
	}

	/**
	 * Retourne la liste de nombre d'�l�ments affichables par page.
	 * @return La liste de nombre d'�l�ments affichables par page.
	 */
	public List<Integer> pageNumberList() {
		ArrayList<Integer> res = new ArrayList<Integer>();
		if (numberOfPageListed <= 1) {
			return res;
		}

		int pageStart = currentPage;
		int margeBefore = (numberOfPageListed / 2) + 1;
		pageStart -= margeBefore;
		if (pageStart < 1) {
			pageStart = 1;
		}

		int pageEnd = pageStart + numberOfPageListed - 1;
		if (pageEnd > getPageCount()) {
			pageEnd = getPageCount();
		}

		for (int i = pageStart; i <= pageEnd; i++) {
			res.add(new Integer(i));
		}
		return res;
	}

	/**
	 * Aller � la page donn�e.
	 * @param page La page � atteindre.
	 */
	public void gotoPage(int page) {
		if (numberOfElements == 0) {
			currentPage = 1;
		} else if (page < 1) {
			page = 1;
		}
		
		if (page > getPageCount()) {
			currentPage = getPageCount();
		} else {
			currentPage = page;
		}

		updateModel();
	}

	/**
	 * Retourne le nombre de pages.
	 * @return Le nombre de pages.
	 */
	public int getPageCount() {
		if (numberOfElements != 0 && numberOfElements % pageSize == 0) {
			return (int) (numberOfElements / pageSize);
		} else {
			return (int) (numberOfElements / pageSize) + 1;
		}
	}

	/** 
	 * Retourne le num�ro du premier �l�ment courant du scroller.
	 * @return Le num�ro du premier �l�ment courant du scroller.
	 */
	public int getFromElement()	{
		return (currentPage - 1) * pageSize;
	}

	/** 
	 * Retourne le num�ro du dernier �l�ment courant du scroller.
	 * @return Le num�ro du dernier �l�ment courant du scroller.
	 */
	public int getToElement() {
		if (numberOfElements == 0) {
			return getFromElement();
		}
		
		int res = getFromElement() + pageSize - 1;
		if (res >= numberOfElements) {
			return numberOfElements - 1;
		} else {
			return res;
		}
	}

	/** Atteindre la page suivante. */
	public void gotoNext() {
		gotoPage(currentPage + 1);
		updateModel();
	}

	/** Atteindre la page pr�c�dente. */
	public void gotoPrevious() {
		gotoPage(currentPage - 1);
		updateModel();
	}
	
	/** Atteindre la premi�re page. */
	public void gotoFirst() {
		gotoPage(1);
		updateModel();
	}

	/** Atteindre la derni�re page. */
	public void gotoLast() {
		gotoPage(getPageCount());
		updateModel();
	}

	/**
	 * Mets � jour le nombre d'�l�ments. 
	 * @param numberOfelements Le nombre d'�l�ments.
	 */
	public void update(int numberOfelements) {
		this.numberOfElements = numberOfelements;
		update();
	}

	/** Mets � jour les donn�ees. */
	public void update() {
		gotoPage(currentPage);
		updateModel();
	}

	/** 
	 * Retourne le mod�le.
	 * @return Le mod�le.
	 */
	public ExtendedTableDataModel<T> getModel() {
		return model;
	}

	/**
	 * D�finit le mod�le.
	 * @param model Le mod�le.
	 */
	public void setModel(ExtendedTableDataModel<T> model) {
		this.model = model;
	}

	/**
	 * ??.
	 * @param event L'evenement �mis.
	 */
	public void  setPageSizeAction(ActionEvent event) {

	}

	/**
	 * D�clenche l'action d'atteindre la premiere page. 
	 * @param event L'evenement �mis.
	 */
	public void  gotoFirstAction(ActionEvent event) {	
		this.gotoFirst();
	}
	
	/**
	 * D�clenche l'action d'atteindre la page pr�c�dente. 
	 * @param event L'evenement �mis.
	 */
	public void  gotoPreviousAction(ActionEvent event) {
		this.gotoPrevious();
	}
	
	/**
	 * D�clenche l'action d'atteindre la page suivante. 
	 * @param event L'evenement �mis.
	 */
	public void  gotoNextAction(ActionEvent event){
		this.gotoNext();
	}
	
	/**
	 * D�clenche l'action d'atteindre la derni�re page. 
	 * @param event L'evenement �mis.
	 */
	public void  gotoLastAction(ActionEvent event){
		this.gotoLast();
	}
	
	/**
	 * ??.
	 * @param event L'evenement �mis.
	 */
	public void  gotoValueAction(ActionEvent event) {

	}
}
