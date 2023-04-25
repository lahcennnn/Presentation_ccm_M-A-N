package com.cedicam.gm.ui.core;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.richfaces.model.ExtendedTableDataModel;


public class DatatableScroller<T> {
	/** Nombre d'éléments par page. */
	private int pageSize = 25;
	
	/**
	 * Retourne le nombre d'éléments par page.
	 * @return Le nombre d'éléments par page.
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
	 * Définit la page courante.
	 * @param _currentPage La page courante.
	 */
	public void setCurrentPage(Integer _currentPage) {
		if (_currentPage != null) {
			gotoPage(_currentPage);
		}
		//updateModel();
	}
	
	/** Le nombre total d'éléments. */
	private int numberOfElements = 0;
	
	/**
	 * Retourne le nombre total d'éléments.
	 * @return Le nombre total d'éléments.
	 */
	public int getNumberOfElements() {
		return numberOfElements;
	}

	/** Le nombre de page listées. */
	private int numberOfPageListed = 5;
	
	/** Le modèle. */
	private ExtendedTableDataModel<T> model;
	
	/** Mettre à jour le modèle. */
	private void updateModel() {
		model.reset();
	}

	/** Remettre le scroller à zéro. */
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
	 * Retourne la liste de nombre d'éléments affichables par page.
	 * @return La liste de nombre d'éléments affichables par page.
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
	 * Aller à la page donnée.
	 * @param page La page à atteindre.
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
	 * Retourne le numéro du premier élément courant du scroller.
	 * @return Le numéro du premier élément courant du scroller.
	 */
	public int getFromElement()	{
		return (currentPage - 1) * pageSize;
	}

	/** 
	 * Retourne le numéro du dernier élément courant du scroller.
	 * @return Le numéro du dernier élément courant du scroller.
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

	/** Atteindre la page précédente. */
	public void gotoPrevious() {
		gotoPage(currentPage - 1);
		updateModel();
	}
	
	/** Atteindre la première page. */
	public void gotoFirst() {
		gotoPage(1);
		updateModel();
	}

	/** Atteindre la dernière page. */
	public void gotoLast() {
		gotoPage(getPageCount());
		updateModel();
	}

	/**
	 * Mets à jour le nombre d'éléments. 
	 * @param numberOfelements Le nombre d'éléments.
	 */
	public void update(int numberOfelements) {
		this.numberOfElements = numberOfelements;
		update();
	}

	/** Mets à jour les donnéees. */
	public void update() {
		gotoPage(currentPage);
		updateModel();
	}

	/** 
	 * Retourne le modèle.
	 * @return Le modèle.
	 */
	public ExtendedTableDataModel<T> getModel() {
		return model;
	}

	/**
	 * Définit le modèle.
	 * @param model Le modèle.
	 */
	public void setModel(ExtendedTableDataModel<T> model) {
		this.model = model;
	}

	/**
	 * ??.
	 * @param event L'evenement émis.
	 */
	public void  setPageSizeAction(ActionEvent event) {

	}

	/**
	 * Déclenche l'action d'atteindre la premiere page. 
	 * @param event L'evenement émis.
	 */
	public void  gotoFirstAction(ActionEvent event) {	
		this.gotoFirst();
	}
	
	/**
	 * Déclenche l'action d'atteindre la page précédente. 
	 * @param event L'evenement émis.
	 */
	public void  gotoPreviousAction(ActionEvent event) {
		this.gotoPrevious();
	}
	
	/**
	 * Déclenche l'action d'atteindre la page suivante. 
	 * @param event L'evenement émis.
	 */
	public void  gotoNextAction(ActionEvent event){
		this.gotoNext();
	}
	
	/**
	 * Déclenche l'action d'atteindre la dernière page. 
	 * @param event L'evenement émis.
	 */
	public void  gotoLastAction(ActionEvent event){
		this.gotoLast();
	}
	
	/**
	 * ??.
	 * @param event L'evenement émis.
	 */
	public void  gotoValueAction(ActionEvent event) {

	}
}
