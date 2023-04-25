package com.cedicam.gm.bt.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.re.bean.EntiteServicesBean;
import com.cedicam.gm.re.entite.EntiteListSelectManager;
import com.cedicam.gm.ui.core.Exporter;
import com.cedicam.gm.ui.core.GenericBean;

/**
 *  Converter permettant à l'affichage d'un outputText de retrouver le libellé
 *  en fonction de la valeur.
 *  Exemple d'utilisation :
 *  <h:outputText id="txtCdBanqueEntACompt" value="#{refentitebean.entiteDtManager.selectedOccurrence.cdBanqueEntACompt}">
 *     <f:converter converterId="valueToLabelConverter"/>
 *  </h:outputText>
 */
public class ValueToLabelConverter implements Converter  {

	/** getAsObject.
	 * Non implémenté.
	 * 
	 * @param context Le contexte
	 * @param component Le composant
	 * @param value La valeur de la chaîne à convertir
	 * @return La chaîne de caractère sous forme d'objet
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return new String(value);
	}

	/** getAsString.
	 * @param context Le contexte
	 * @param component Le composant
	 * @param value L'object à convertir en String
	 * @return La String représentant l'objet
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		final EntiteServicesBean entiteBean = (EntiteServicesBean) GenericBean.findBean("entiteservicesbean");
		EntiteListSelectManager entiteListManager = new EntiteListSelectManager(entiteBean);
		entiteListManager.refreshCreation();
//		EntiteListSelectManager entiteListManager = ((ReferentielEntiteBean) GenericBean.findBean("refentitebean")).getEntiteListManager();
		final String idComponent = component.getId();
		String label = null;
		
		if (value != null) {
			label = value + " ";
			
			if ("txtCdBanqueEntACompt".equals(idComponent)) {
				label = Exporter.recupererLabelDansListe(""+value, entiteListManager.getListEntiteCompta());
			} else if ("txtCirc1CdBqReglInter".equals(idComponent)) {
				label = Exporter.recupererLabelDansListe(""+value, entiteListManager.getListEntiteReglement());
			} else if ("txtCirc1CdBqReglIntra".equals(idComponent)) {
				label = Exporter.recupererLabelDansListe(""+value, entiteListManager.getListEntiteReglement());
			} else if ("txtCirc1CdBqReglCore".equals(idComponent)) {
				label = Exporter.recupererLabelDansListe(""+value, entiteListManager.getListEntiteReglement());
			} else if ("txtCirc1CdBqReglCup".equals(idComponent)) {
				label = Exporter.recupererLabelDansListe(""+value, entiteListManager.getListEntiteReglement());
			} else if ("txtCirc1CdBqReglMci".equals(idComponent)) {
				label = Exporter.recupererLabelDansListe(""+value, entiteListManager.getListEntiteReglement());
			} else if ("txtChefDeFile".equals(idComponent)) {
				label = Exporter.recupererLabelDansListe(""+value, entiteListManager.getListChefDeFile());
			} else if ("txtCirc1CdBqReglVisa".equals(idComponent)) {
				label = Exporter.recupererLabelDansListe(""+value, entiteListManager.getListEntiteReglement());
			}
			
			if (label.equals("" + value)) {
				label = value + " - " + ConstantesTypo.LIBELLE_ENTITE_NON_REFERENCEE;
			}
		}
		
		return label;
	}

}
