package com.cedicam.gm.bt.bean;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.NullComparator;
import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.SimpleSelection;

import com.cedicam.gm.bt.data.dto.GenericDto;
import com.cedicam.gm.ui.core.GenericBean;


/**
 * @author Administrateur
 *
 * @param <T>
 */
public abstract class DataSelectionGenericBean<T extends GenericDto> extends GenericBean {

	// TODO : Delete
	static private Integer ordreAppel = 0;

	/** dataTable. */
	private ExtendedTableDataModel<T> dataTable;

	/** data. */
	private List<T> data;

	/** rows.
	 * Le nombre de ligne par pages pour le composant en cours.
	 */
	protected int rows;

	/** selection. */
	private SimpleSelection selection = new SimpleSelection();

	/** tableState. */
	private Object tableState;

	/** theKey. */
	private Object theKey;

	/** occurrence. */
	private T occurrence;

	/** mesColonnes.
	 * Tableau contenant le nom des propri�t�s dans l'ordre de leur apparation dans la vue
	 */
	protected String[] mesColonnes;

	/** propertyIdT.
	 * La propri�t� qui sert d'identifiant � l'objet T
	 */
	protected String propertyIdT;

	/** mesTris.
	 * Une seule case du tableau est � True/False (ascending/descending), les autres sont � null
	 */
	private Boolean[] mesTris;

	/** sortedProperty.
	 * Propri�t� sur laquelle le tri de la liste est effectu�
	 */
	private String sortedProperty = null;

	/** sortOrderChanged
	 * D�finit si l'ordre du tri a chang� ou pas
	 */
	private boolean sortOrderChanged = false;

	/** mesFiltres.
	 * Tableau de String repr�sentant les filtres utilis�s sur chacune des colonnes
	 */
	protected String[] mesFiltres;

	/** mesConverters.
	 * Tableau contenant les converters utilis�s pour chaque colonne de la table dans la vue (sert pour le filtre)
	 */
	// Si un jour on doit g�rer des colonnes � propri�t�s multiples sur lesquelles on applique des converters, il faudra
	// g�rer un tableau de converters � plusieurs dimensions et adapter le code en cons�quence.
	protected Converter[] mesConverters;

	/** total. */
	private BigDecimal total = BigDecimal.ZERO;

	/** totalProperty. */
	protected String totalProperty;

	/** update. */
	private boolean update = true;

	/** originialDatas.
	 * HashMap contenant les donn�es telles que r�cup�r�es par le service
	 */
	private Map<Integer, T> originalDatas = new HashMap<Integer, T>();

	/**
	 * @return List
	 */
	protected abstract List<T> updateData();

	private List<SelectItem> listRows = new ArrayList<SelectItem>();



	/**
	 * @return Objet
	 */
	public final T getOccurrence() {

		if (selection != null && dataTable != null) {
			Iterator<Object> iterator = selection.getKeys();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				occurrence = dataTable.getObjectByKey(key);
			}
		}
		return occurrence;
	}

	/**
	 * Clears the selection.
	 */
	protected final void clearSelection() {
		selection = new SimpleSelection();
	}

	/** isUpdate.
	 * @return Un bool�en d�finissant si il y a eu mise � jour
	 */
	public boolean isUpdate() {
		return update;
	}

	/** setUpdate.
	 * @param update Un bool�en d�finissant si il y a eu mise � jour
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	/**
	 *
	 */
	public void updateToTrue() {
		update = true;
	}

	/**
	 * @return La dimension
	 */
	public final int getDataSize() {
		if (this.data == null) {
			return 0;
		} else {
			return this.data.size();
		}
	}

	/**
	 * @param occurrence L'occurence
	 */
	public final void setOccurrence(final T occurrence) {
		this.occurrence = occurrence;
	}

	/**
	 * @return Etat
	 */
	public final Object getTableState() {
		return this.tableState;
	}

	/**
	 * @param tableState L'etat
	 */
	public final void setTableState(final Object tableState) {
		this.tableState = tableState;
	}

	/** getRows.
	 * @return Le nombre de lignes par page
	 */
	public int getRows() {
		return rows;
	}

	/** setRows.
	 * @param rows Le nombre de lignes par page
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}


	/* Set current page to page 1 if there is not enough elements in the dataTable */
/*	@Override
	public int getScrollerPage() {
		if (Math.ceil((double) data.size() / (double) rows) < super.getScrollerPage()) {
			super.setScrollerPage(1);
		}
		return super.getScrollerPage();
	}*/

	/** getDataTable.
	 * @return Le tableau
	 */
	public final ExtendedTableDataModel<T> getDataTable() {

		if (update) {
			data = updateData();
			update = false;
		}

		if (data == null) {
			data = new LinkedList<T>();
		}
		this.dataTable = new ExtendedTableDataModel<T>(new RfDataProvider());
		try {
			this.dataTable.getRowIndex();
		} catch (NullPointerException e) {
			this.dataTable.reset();
		}

		/**
		if (occurrence != null) {
			// TODO : Modifier en trace
			System.out.println("id occurence : " + occurrence.getId());
		}

		if (null != occurrence) {
			theKey = dataTable.getKey(occurrence);
			if (null != theKey) {
				// TODO : Modifier en trace
				//System.out.println("la cl� : " + theKey);
				selection.clear();
				selection.addKey(theKey);
				Iterator<Object> iterator = selection.getKeys();
				while (iterator.hasNext()) {
					Object key = iterator.next();
					// TODO : Modifier en trace
					//System.out.println(key);
				}
			}
		}

		for (int j=0;j<10;j++) {
			System.out.println("Dans getDataTable");
			System.out.println(data.get(j).toString());
			System.out.println("--------------------------------------");
		}
		**/

		return dataTable;
	}

	/** setDataTable.
	 * @param dataTable Le tableau
	 */
	public final void setDataTable(ExtendedTableDataModel<T> dataTable) {
		this.dataTable = dataTable;
	}

	/** getData.
	 * @return La liste
	 */
	public final List<T> getData() {
		return data;
	}

	/** setData.
	 * @param data La liste
	 */
	public final void setData(List<T> data) {
		this.data = data;
		if (null != data) {
			initOriginalDatas();
		}
	}

	/** getSelection.
	 * @return La selection
	 */
	public final SimpleSelection getSelection() {
		return selection;
	}

	/** setSelection.
	 * @param selection La selection
	 */
	public final void setSelection(SimpleSelection selection) {
		this.selection = selection;
	}

	/** isHasSelection.
	 * @return Selectionn� ou non
	 */
	public boolean isHasSelection() {
		return getSelection().getKeys() != null;
	}

	// DataProvider Implementation

	/**
	 * @author Administrateur
	 *
	 */
	final class RfDataProvider implements DataProvider<T> {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public T getItemByKey(final Object key) {
			if (key != null) {
				for (final T element : data) {
					if (key.equals(getKey(element))) {
						return element;
					}
				}
			}
			return null;
		}

		@Override
		public List<T> getItemsByRange(final int fromIndex, final int toIndex) {
			return data.subList(fromIndex, toIndex);
		}

		@Override
		public Object getKey(final T element) {
			if (element != null) {
				return element.getId();
			} else {
				return null;
			}
		}

		@Override
		public int getRowCount() {
			if (data != null) {
				return data.size();
			} else {
				return 0;
			}
		}

	}

	/** getTheKey.
	 * @return La cl�
	 */
	public Object getTheKey() {
		return theKey;
	}

	/** setTheKey.
	 * @param theKey La cl� � d�finir
	 */
	public void setTheKey(Object theKey) {
		this.theKey = theKey;
	}

	/** getMesColonnes.
	 * @return Les colonnes
	 */
	public String[] getMesColonnes() {
		return mesColonnes;
	}

	/** setMesColonnes.
	 * @param mesColonnes Les colonnes � d�finir
	 */
	public void setMesColonnes(String[] mesColonnes) {
		this.mesColonnes = mesColonnes;
	}

	/** getMesFiltres.
	 * @return Les filtres
	 */
	public String[] getMesFiltres() {
		return mesFiltres;
	}

	/** setMesFiltres.
	 * @param mesFiltres Les filtres � d�finir
	 */
	public void setMesFiltres(String[] mesFiltres) {
		this.mesFiltres = mesFiltres;
	}

	/** getLesConverters.
	 * @return Les converters
	 */
	public Converter[] getMesConverters() {
		return mesConverters;
	}

	/** setMesConverters.
	 * @param mesConverters Les converters � d�finir
	 */
	public void setMesConverters(Converter[] mesConverters) {
		this.mesConverters = mesConverters;
	}

	/** getTotal.
	 * @return Le total.
	 */
	public BigDecimal getTotal() {
		total = BigDecimal.ZERO;

		String getterName = "get" + totalProperty.substring(0, 1).toUpperCase()
				+ totalProperty.substring(1);

		for (T object : data) {
			Method method;
			BigDecimal methodReturn = BigDecimal.ZERO;
			try {
				method = object.getClass().getDeclaredMethod(getterName, (Class<?>[]) null);

				methodReturn = (BigDecimal) method.invoke(object);
			} catch (Exception e) {
				e.printStackTrace();
			}

			total = total.add(methodReturn);
		}
		return total;
	}

	/** setTotal.
	 * @param total Le total.
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/** calculerComparateur.
	 * @return Le comparateur
	 */
	public ComparatorChain calculerComparateur() {
		ComparatorChain chaineComparator = new ComparatorChain();
		for (int i = 0; i < mesTris.length; i++) {
			if (mesTris[i] != null) {
				if (mesTris[i].booleanValue()) {
					BeanComparator comparator = new BeanComparator(mesColonnes[i], new NullComparator(true));
					if (chaineComparator.size() > 0) {
						chaineComparator.setComparator(0, comparator);
					} else {
						chaineComparator.addComparator(comparator);
					}
					return chaineComparator;

				} else {
					BeanComparator comparator = new BeanComparator(mesColonnes[i], new NullComparator(true));
					if (chaineComparator.size() > 0) {
						chaineComparator.setComparator(0, comparator, true);
					} else {
						chaineComparator.addComparator(comparator, true);
					}
					return chaineComparator;
				}
			}
		}
		return null;
	}

	/** setMonComparateurString.
	 * M�thode pour trier la dataTable, utilis�e dans les vues o� l'on doit pouvoir trier
	 * les donn�es, mais pas les filtrer
	 * @param e ActionEvent renvoy� par la vue
	 */
	public void setMonComparateurString(ActionEvent e) {
		if (e != null) {
			// D�finit la sorted property
			Object property = e.getComponent().getAttributes().get("property");

			if (property != null) {
				// Le sens du tri change si la propri�t� sur laquelle on veut trier est identique �
				// celle du tri pr�c�dent
				sortOrderChanged = property.equals(sortedProperty);

				sortedProperty = property.toString();
			}
		}

		// On recharge la liste d'objets de base
		reinitDatas();

		// On trie la liste
		trierData();

		// On actualise la dataTable
		this.dataTable = new ExtendedTableDataModel<T>(new RfDataProvider());
	}

	/** trierData.
	 * Effectue un tri sur la sortedProperty, dans le sens d�fini dans le tableau mesTris
	 */
	@SuppressWarnings("unchecked")
	private void trierData() {
		for (int i = 0; i < mesColonnes.length; i++) {
			if ((mesColonnes[i].split("/"))[0].equals(sortedProperty)) {
				ComparatorChain chaineComparator = new ComparatorChain();
				if (null == mesTris[i]) {
					mesTris[i] = Boolean.TRUE;
				} else if (sortOrderChanged) {
					// Si l'ordre de tri a chang� (c'est � dire, si on a cliqu� sur la m�me colonne une deuxi�me fois)
					mesTris[i] = !mesTris[i];
					sortOrderChanged = false;
				}

				BeanComparator comparator = new BeanComparator(sortedProperty, new NullComparator(true));
				if (chaineComparator.size() > 0) {
					chaineComparator.setComparator(0, comparator, mesTris[i]);
				} else {
					chaineComparator.addComparator(comparator, mesTris[i]);
				}

				Collections.sort(this.data, chaineComparator);
			} else {
				mesTris[i] = null;
			}
		}
	}

	/** reinitDatas.
	 * Recharge la liste telle qu'elle �tait � l'origine
	 */
	private void reinitDatas() {
		this.data = new ArrayList<T>();
//		for (int i = 0; i < originalDatas.size(); i++) {
//			data.add(null);
//		}

//		for (Entry<Integer, T> entry : originalDatas.entrySet()) {
//			this.data.add(entry.getKey(), entry.getValue());
////		}
//
//		for (Entry<Integer, T> entry : originalDatas.entrySet()) {
//			this.data.add(entry.getValue());
//		}

//		boolean hasNull = true;
//		while (hasNull) {
//			hasNull = data.remove(null);
//		}
		setData(Collections.list(Collections.enumeration(originalDatas.values())));
	}

	/** getMesTris.
	 * @return Le sens de tri pour chaque colonne
	 */
	public Boolean[] getMesTris() {
		return mesTris;
	}

	/** setMesTris.
	 * @param mesTris Le sens de tri pour chaque colonne
	 */
	public void setMesTris(Boolean[] mesTris) {
		this.mesTris = mesTris;

		// On retrouve la sortedProperty � partir de la liste
		for (int i = 0; i < mesTris.length; i++) {
			if (mesTris[i] != null) {
				sortedProperty = (mesColonnes[i].split("/"))[0];
				break;
			}
		}
	}

	/** getOriginalDatas.
	 * @return Une Map contenant les donn�es originales
	 */
	public Map<Integer, T> getOriginalDatas() {
		return originalDatas;
	}

	/** setOriginalDatas.
	 * @param originalDatas Une Map contenant les donn�es originales
	 */
	public void setOriginalDatas(Map<Integer, T> originalDatas) {
		this.originalDatas = originalDatas;
	}

	/** initOriginalDatas.
	 * D�finit la HashMap de donn�es originales � partir d'une liste de donn�es
	 */
	private void initOriginalDatas() {
		for (int i = 0; i < this.data.size(); i++) {
			originalDatas.put(i, this.data.get(i));
		}
	}

	/**
	 * Retrouver la page sur laquel on a laiss� l'�cran.
	 */
	private void retrieveScrollerPage() {
		int scrollerPage = 0;

		if (propertyIdT != null) {
			List<String> idGettersName = new ArrayList<String>();

			String[] idProperties = propertyIdT.split("/");
			for (int i = 0; i < idProperties.length; i++) {
				String getterName = "get" + idProperties[i].substring(0, 1).toUpperCase() + idProperties[i].substring(1);
				idGettersName.add(getterName);
			}

			List<Method> methodList = new ArrayList<Method>();
			List<Object> idOccurrenceList = new ArrayList<Object>();
			try {
				for (String getter : idGettersName) {
					Method m = occurrence.getClass().getDeclaredMethod(getter, (Class<?>[]) null);
					methodList.add(m);
					idOccurrenceList.add(m.invoke(occurrence));
				}

				int numOcc = 0;

				for (T element : data) {
					T current = element;
					numOcc++;
					List<Object> idCurrentList = new ArrayList<Object>();

					for (Method m : methodList) {
						idCurrentList.add(m.invoke(current));
					}

					boolean equals = true;
					for (int i = 0; i < idOccurrenceList.size(); i++) {
						equals &= idOccurrenceList.get(i).equals(idCurrentList.get(i));
					}
					if (equals) {
						scrollerPage = (int) (Math.floor(numOcc / rows)) + 1;
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	//	setScrollerPage(scrollerPage);
	}

	/** filtrerEtTrier.
	 * M�thode pour filtrer et trier une dataTable, utilis�e sur toutes les vues o�
	 * l'on doit effectue et tri et filtrer les �l�ments.
	 * @param e ActionEvent renvoy� par la vue
	 */
/*	public void filtrerEtTrier(ActionEvent e) {
		if (e != null) {
			// D�finit la sorted property
			Object property = e.getComponent().getAttributes().get("property");

			if (property != null) {
				// Le sens du tri change si la propri�t� sur laquelle on veut trier est identique �
				// celle du tri pr�c�dent
				sortOrderChanged = property.equals(sortedProperty);

				sortedProperty = property.toString();
			}
		}

		// On recharge la liste d'objets de base
		reinitDatas();

		// On filtre la liste
		filtrerData();

		// On trie la liste
		trierData();

		// On actualise la dataTable
		this.dataTable = new ExtendedTableDataModel<T>(new RfDataProvider());
	}*/

	/** filtrerListe.
	 * Reg�n�re la datatable � partir du tableau de filtres, utilis�e pour dans les vues
	 * on l'on doit filtrer, mais pas trier les �l�ments.
	 * @param e ActionEvent renvoy� par la vue
	 */
/*	public void filtrer(ActionEvent e) {
		// On recharge la liste d'objets de base
		reinitDatas();

		// On filtre la liste
		filtrerData();

		// On actualise la dataTable
		this.dataTable = new ExtendedTableDataModel<T>(new RfDataProvider());
	}*/

	/** filtrerData.
	 * Filtre la liste en fonction des filtres contenus dans le tabeau mesFiltres
	 */
/*	private void filtrerData() {
		// On sauvegarde le nouveau filtre
		backupMesFiltres = mesFiltres.clone();

		for (int i = 0; i < mesFiltres.length; i++) {
			if (mesFiltres[i] != null) {
				// On split la cha�ne pour r�cup�rer toutes les propri�t�s sur lesquelles on filtre
				String[] proprietesFiltrees = mesColonnes[i].split("/");

				// On utilise un set pour ne pas r�cup�rer de doublons quand le filtrage s'effectue sur
				// plusieurs propri�t�s.
				List<T> tempResult = new ArrayList<T>();

				for (int j = 0; j < proprietesFiltrees.length; j++) {

					List<T> listeAFiltrer = new ArrayList<T>();
					listeAFiltrer.addAll(data);

					filtrerColonne(proprietesFiltrees[j], i, listeAFiltrer);

					for (T elem : listeAFiltrer) {
						if (!tempResult.contains(elem)) {
							tempResult.add(elem);
						}
					}
				}

				data.clear();
				data.addAll(tempResult);
			}
		}
	}*/

	/** filtrerColonne.
	 * @param proprieteFiltree Le nom de la propri�t� sur laquelle effectuer le filtre
	 * @param numeroColonne Le num�ro de la colonne sur laquelle effectuer le filtrage
	 * @param elementList La liste � filtrer
	 */
	private void filtrerColonne(String proprieteFiltree, int numeroColonne, List<T> elementList) {
		// On retrouve le nom du getter � partir de la propri�t�
		String getterName = "get" + proprieteFiltree.substring(0, 1).toUpperCase()
				+ proprieteFiltree.substring(1);

		// Puis on supprime de la liste les �l�ments qui ne matchent pas avec le filtre
		 Iterator<T> iterator = elementList.iterator();
		 while (iterator.hasNext()) {
			 T object = iterator.next();
			 if (object == null || !filtrerElement(object, mesFiltres[numeroColonne], getterName,
					 mesConverters[numeroColonne])) {
				 iterator.remove();
			 }
		 }
	}

	/** filterElement.
	 * @param current L'objet T qui doit �tre filtr�
	 * @param filter Le filtre � appliquer
	 * @param getterName Le nom de la m�thode permettant de r�cuperer l'attribut sur lequel porte le filtre
	 * @param converter Le converter utilis� le cas �ch�ant, peut �tre null.
	 * @return True si l'�l�ment doit appara�tre, False sinon.
	 */
	private boolean filtrerElement(T current, String filter, String getterName, Converter converter) {
		if (filter.isEmpty()) {
			return true;
		}

		Method method;
		Object methodReturn;
		String methodReturnAsString;
		try {
			method = current.getClass().getDeclaredMethod(getterName, (Class<?>[]) null);

			methodReturn = method.invoke(current);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		if (methodReturn == null) {
			return false;
		}

		if (converter != null) {
			methodReturnAsString = converter.getAsString(null, null, methodReturn);
		} else {
			methodReturnAsString = methodReturn.toString();
		}

		boolean result = false;
		if (methodReturnAsString != null) {
			result = methodReturnAsString.toLowerCase().contains(filter.toLowerCase());
		}

		return result;
    }

	/** setTrier.
	 * Trie la liste pour retrouver l'�cran dans l'�tat dans lequel on l'a laiss�.
	 * @param param Param�tre fant�me.
	 */
/*	public void setTrier(int param) {
		setMonComparateurString(null);
		retrieveScrollerPage();
	}*/

	/** setFiltrerEtTrier.
	 * Filtre et trie pour retrouver un �cran dans l'�tat dans lequel on l'a laiss�.
	 * @param param Un param�tre fant�me.
	 */
/*	public void setFiltrerEtTrier(int param) {
		filtrerEtTrier(null);
		retrieveScrollerPage();
	}*/

	public List<SelectItem> getListRows() {
		if (listRows.isEmpty()) {
			listRows.add(new SelectItem(5, "5"));
			listRows.add(new SelectItem(10, "10"));
			listRows.add(new SelectItem(15, "15"));
		}
		return listRows;
	}

	public void setListRows(List<SelectItem> listRows) {
		this.listRows = listRows;
	}
}
