package com.cedicam.gm.bt.util;

public class FormatUtils {
	/**
	 * 
	 * @param taille La taille du champ
	 * @param string La chaîne à transformer
	 * @param filler Le caractère de complétion
	 * @return L'objet formatté en chaîne de caractères
	 */
	public static String format(int taille, String string, char filler) {
		String result = "";
		if (null != string && !string.trim().isEmpty()) {
			result = new String(new char[taille]).replaceAll("" + Character.MIN_VALUE, "" + filler);
			result += string;
			result = result.substring(result.length() - taille);
		}
		
		return result;
	}
}
