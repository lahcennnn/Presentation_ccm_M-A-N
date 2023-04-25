package com.cedicam.gm.ui.core;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cedicam.gm.re.data.dto.EntiteDto;

public class Sort {

	protected static class GenericComparator implements Comparator<Object> {
		/** Tri ascendant. */
		private boolean sortOrderAscending = true;
		/** Le getter de la propriété en cours. */
		private Method getter;

		/**
		 * Constructeur du comparator.
		 * @param getter Le getter de la propriété. 
		 * @param ascending Vrai pour un tri ascendant.
		 */
		public GenericComparator(Method getter, boolean ascending) {
			sortOrderAscending = ascending;
			this.getter = getter;
		}

		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public int compare(Object o1, Object o2) {
			int result = 0;
			if (getter != null) {			
				try {
					if (!sortOrderAscending) {
						Object temp = o2;
						o2 = o1;
						o1 = temp;
					}
					
					Comparable val1 = null;					
					if (o1 != null) {
						val1 = (Comparable) getter.invoke(o1);
					}
					
					Comparable val2 = null;
					if (o2 != null) {
						val2 = (Comparable) getter.invoke(o2);
					}
					
					if (val1 == null && val2 == null) {
						result = 0;
					} else if (val1 == null) {
						result = -1;
					} else if (val2 == null) {
						result = 1;
					} else {
						result = val1.compareTo(val2);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}

	}

	/** Tri ascendant. */
	private boolean sortAscending = true;
	
	/** La propriété sur laquelle effectuer le tri. */
	private String sortProperty = null;

	/** Réinitialise les tris. */
	public void clear() {
		sortAscending = true;
		sortProperty = null;
	}

	/**
	 * Retourne l'ordre du tri.
	 * @return Vrai si le tri est ascendant.
	 */
	public boolean isSortAscending() {
		return sortAscending;
	}

	/**
	 * Retourne la propriété de tri.
	 * @return La propriété de tri.
	 */
	public String getSortProperty() {
		return sortProperty;
	}

	/**
	 * Définit la propriété de tri.
	 * @param property La propriété de tri.
	 */
	public void setSortProperty(String property) {
		if (property.equals(sortProperty)) {
			sortAscending = !sortAscending;
		} else {
			sortAscending = true;
			sortProperty = property;
		}
	}

	/**
	 * Trie la liste passée en paramètre.
	 * @param list La liste à trier.
	 */
	public void trier(List<Object> list) {
		if (sortProperty != null && list != null && list.size() != 0) {
			Object o = list.get(0);
			GenericComparator genericComparator = new Sort.GenericComparator(getGetter(sortProperty, o.getClass()), sortAscending);
			Collections.sort(list, genericComparator);
		}
	}

	/**
	 * Remet les tris à zero.
	 * @param property La propriété sur laquelle filtrer.
	 */
	public void resetSort(String property) {
		sortAscending = true;
		sortProperty = property;
	}

	/**
	 * Retourne le getter pour la propriété passée en paramètre.
	 * @param propertyName La propriété dont on veut trouver le getter.
	 * @param cl La classe de l'objet.
	 * @return Le getter à appeler.
	 */
	@SuppressWarnings("rawtypes")
	protected Method getGetter(String propertyName, Class cl) {
		try {
			PropertyDescriptor pd =  new PropertyDescriptor(propertyName, cl);
			return pd.getReadMethod();
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*public static String capitalize(String name) {
		if (name == null || name.length() == 0) {
		    return name;
		}
		return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
    }

	@SuppressWarnings("rawtypes")
	protected Method getGetter(String propertyName, Object o){
		Class cl = o.getClass();
		String capitalized = capitalize(propertyName);
		try {
			PropertyDescriptor pd ;
			if(o instanceof Boolean) pd =  new PropertyDescriptor(propertyName, cl,"is"+capitalized, "set"+capitalized);
			else pd =  new PropertyDescriptor(propertyName, cl, "get"+capitalized, "set"+capitalized);
			return pd.getReadMethod();
		} catch (IntrospectionException e) {
			e.printStackTrace();
			return null;
		}
	}*/
}
