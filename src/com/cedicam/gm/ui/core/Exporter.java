package com.cedicam.gm.ui.core;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Cette classe permet de g�rer les exports CSV et XLS au travers de l'application.
 * 
 * @author St�phane Cadena
 *
 */
public class Exporter {

	public final static String FORMAT_CSV = "CSV";
	public final static String FORMAT_XLS = "XLS";
	public final static String DELIMITEUR_COLONNE_CSV = ";";
	public final static String DELIMITEUR_FIN_LIGNE_CSV = "\r\n";
	
	/**
	 * M�thode permettant de g�n�rer un fichier CSV pour le retourner au client.
	 * Le retour se fait sous la forme d'un fichier en attachement (popup "Enregistrer sous").
	 * 
	 * @param lignes	List des lignes � g�n�rer. Chaque �l�ment contient une liste de colonnes.
	 */
    public static void genererRapportCSV(final ArrayList<ArrayList<String>> lignes){  
        HttpServletResponse res = HttpJSFUtil.getResponse();  
        res.setContentType("text/csv");
        //res.setContentType("text/comma-separated-values");
        res.setHeader("Content-disposition",  "attachment; filename=export.csv");  
          
        try {  
            ServletOutputStream out = res.getOutputStream();  
    
            // Alimentation des donn�es dans le fichier
            for (ArrayList<String> arrayList : lignes) {
            	String maLigne = new String();
            	for (String valeur : arrayList) {
            		maLigne = maLigne + Exporter.formaterChaineCSV(valeur) + DELIMITEUR_COLONNE_CSV;
    			}
            	out.print(maLigne + DELIMITEUR_FIN_LIGNE_CSV);
    		}

            out.flush();  
            out.close();  
       } catch (IOException ex) {   
               ex.printStackTrace();  
       }  
        
      FacesContext faces = FacesContext.getCurrentInstance();  
      faces.responseComplete();    
    }  

	/**
	 * M�thode permettant de g�n�rer un fichier XLS pour le retourner au client.
	 * Le retour se fait sous la forme d'un fichier en attachement (popup "Enregistrer sous").
	 * 
	 * @param lignes	List des lignes � g�n�rer. Chaque �l�ment contient une liste de colonnes.
	 */
     public static void genererRapportXLS(final ArrayList<ArrayList<String>> lignes){  
        HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("Export");  
          
        HSSFRow row;  
          
        // Alimentation des donn�es dans le fichier
        int iLigne = 0;
        for (ArrayList<String> arrayList : lignes) {
        	row = sheet.createRow(iLigne);
        	int iCol = 0;
        	for (String valeur : arrayList) {
        		row.createCell(iCol).setCellValue(valeur);
				iCol++;
			}
        	iLigne++;
		}
        
        HttpServletResponse res = HttpJSFUtil.getResponse();  
        res.setContentType("application/vnd.ms-excel");  
        res.setHeader("Content-disposition",  "attachment; filename=export.xls");  
          
        try {  
            ServletOutputStream out = res.getOutputStream();  
    
             wb.write(out);  
             out.flush();  
             out.close();  
       } catch (IOException ex) {   
               ex.printStackTrace();  
       }  
        
      FacesContext faces = FacesContext.getCurrentInstance();  
      faces.responseComplete();    
    }  
     
     /**
      * Exemple d'appel de l'export
      */
     private void exempleAppelGeneration() {
    	 genererRapportCSV(exempleGenererCorpsExport());
     }
     
     /**
      * Exemple de construction des donn�es � exporter
      * @return
      */
     private ArrayList<ArrayList<String>> exempleGenererCorpsExport() {
     	ArrayList<ArrayList<String>> lignes = new ArrayList<ArrayList<String>>();
     	
     	ArrayList<String> ligne = new ArrayList<String>();
     	ligne.add("Crit�res de recherche");
     	lignes.add(ligne);

     	ligne = new ArrayList<String>();
     	ligne.add("Date et heure d'�change du");
     	ligne.add("19/12/2011");
     	ligne.add("");
     	ligne.add("R�seau");
     	ligne.add("");
     	lignes.add(ligne);

     	ligne = new ArrayList<String>();
     	ligne.add("R�sultat de la recherche");
     	lignes.add(ligne);

        	ligne = new ArrayList<String>();
     	ligne.add("");
     	ligne.add("");
     	ligne.add("");
     	ligne.add("Regroupements �mission");
     	ligne.add("Regroupements �mission");
     	lignes.add(ligne);
     	ligne = new ArrayList<String>();
     	ligne.add("Code op�.");
     	ligne.add("Devise");
     	ligne.add("Entit� �met.");
     	ligne.add("Nb.");
     	ligne.add("Signe");
     	lignes.add(ligne);

     	ligne = new ArrayList<String>();
     	ligne.add("100");
     	ligne.add("EUR");
     	ligne.add("30206");
     	ligne.add("10");
     	ligne.add("C");
     	lignes.add(ligne);

     	return lignes;
     }

     /**
      * Formate une chaine dans le format attendu pour l'export CSV :
      * - doublage des "
      * - encadr� par des " suppl�metaires
      * 
      * Cette m�thode est appliqu�e sur chaque cellule (recommandation pour s�curiser le CSV)
      * 
      * @param aString
      * @return String
      */
     public static String formaterChaineCSV(final String aString) {
    	 if (aString == null) {
    		 return "";
    	 }
    	 aString.replaceAll("\"", "\"\"");
    	 return "\"" + aString + "\"";
     }

     /**
      * Formate une date dans le format attendu pour l'export : dd/MM/yyyy
      * 
      * @param aDate
      * @return String
      */
     public static String formaterDate(final Date aDate) {
    	 if (aDate == null) {
    		 return "";
    	 }
    	 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	 return sdf.format(aDate);
     }
     
     /**
      * Formate une date+heure dans le format attendu pour l'export : dd/MM/yyyy  HH:mm
      * 
      * @param aDate
      * @return String
      */
     public static String formaterDateHeure(final Date aDate) {
    	 if (aDate == null) {
    		 return "";
    	 }
    	 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    	 return sdf.format(aDate);
     }

     /**
      * Formate un nombre la date dans le format attendu pour l'export :
      * - pas de s�parateur de milliers
      * - une virgule comme s�parateur d�cimal
      * 
      * @param aNumber
      * @return String
      */
     public static String formaterNombre(final Number aNumber) {
 		// les montants sont stock�s en centimes (2 d�cimales) et affich�es avec le s�parateur d�cimale
    	 if (aNumber == null) {
    		 return "";
    	 }
    	 try {
 			// on affiche les nombres avec le s�parateur d�cimale (stock�s en centimes)
 			double ldNb = aNumber.doubleValue() / 100;
 			// formatage pour l'export
        	 DecimalFormatSymbols dfs = new DecimalFormatSymbols(java.util.Locale.FRANCE);
        	 dfs.setGroupingSeparator(' ');
        	 DecimalFormat df = new DecimalFormat("#0.00", dfs);
        	 String result = df.format(ldNb).replaceAll(" ","");
        	 return result;
    		 
    	 } catch (Exception e) {
    		 return "";
		}
    	 }

     /**
      * Cette fonction r�cup�re le libell� � afficher en fonction d ela valeur.
      * Utilis� dans l'export et l'impression pour reprendre la valeur des �crans 
      * concernant les listes d�roulantes.
      * Si pas de valeur, on retourne le choix par d�faut des listes d�roulantes de crit�res : "- Tous -".
      * 
      * @param aValeur
      * @param aListe
      * @return le libell�
      */
     public static String recupererLabelDansListe(final String aValeur, final List<SelectItem> aListe) {
    	 if (aListe == null) {
    		 return "";
    	 }
    	 if (aValeur == null || aValeur == "null" || aValeur == FiltreEntites.ALL) {
    		 return "- Tous -";
    	 }
    	 for (Iterator<SelectItem> iterator = aListe.iterator(); iterator.hasNext();) {
			SelectItem selectItem = (SelectItem) iterator.next();
			if (selectItem.getValue() != null && selectItem.getValue().equals(aValeur)) {
				return selectItem.getLabel();
			}
		}
    	 // on n'a pas trouv� le libell� pour cette valeur, on retourne la valeur
    	 return aValeur;
     }

}
