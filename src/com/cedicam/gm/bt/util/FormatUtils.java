package com.cedicam.gm.bt.util;

public class FormatUtils {
	/**
	 * 
	 * @param taille La taille du champ
	 * @param string La cha�ne � transformer
	 * @param filler Le caract�re de compl�tion
	 * @return L'objet formatt� en cha�ne de caract�res
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
