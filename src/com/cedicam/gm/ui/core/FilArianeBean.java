/**
 * 
 */
package com.cedicam.gm.ui.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.richfaces.component.UIColorPicker;

import com.cedicam.gm.bt.spring.ApplicationContextHolder;
import com.cedicam.gm.re.entite.ReferentielEntiteBean;
import com.sun.xml.bind.v2.model.core.Ref;

/**
 * @author CDENIS
 *
 */
public class FilArianeBean extends GenericBean {

	/** Bundle du fil d'Ariane.*/
	private static ResourceBundle rbAriane = ResourceBundle.getBundle("ariane");
	private List<ElementFilAriane> filAriane = new ArrayList<ElementFilAriane>();

	public FilArianeBean() {
		super();
		setEnteteComposant("GMUIBNFIL", "Fil d'ariane");
	}

	/** getFilAriane.
	 * Retourne le chemin du fil d'ariane.
	 * @return le html du fil d'ariane relatif au codeEcran en cours.
	 */
	public List<ElementFilAriane> getFilAriane() {
		//filAriane = new ArrayList<ElementFilAriane>();
		String urlEcran = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		
		int posLastSlash = urlEcran.lastIndexOf('/');
		int posExt = urlEcran.lastIndexOf(".jspx");
		String codeEcran = urlEcran.substring(posLastSlash + 1, posExt);
		filAriane = getFilAriane(codeEcran); 
		return filAriane;
	}

	/** getFileAriane.
	 * Retourne le chemin du fil d'ariane d'un écran particulier.
	 * @param code le code de l'écran pour lequel il faut retrouver le fil d'ariane.
	 * @return le html du fil d'ariane.
	 */
	public List<ElementFilAriane> getFilAriane(String code) {
		List<ElementFilAriane> fil = new ArrayList<ElementFilAriane>();
		
		String nomEcranPrefixe = "gm.";
		String nomEcranSuffixe = ".el000";

		if (rbAriane == null) {
			fil.add(new ElementFilAriane("Impossible lire bundle Ariane", null));
		} else {
			if ("EC035".equalsIgnoreCase(code)) {
				ReferentielEntiteBean refEntiteBean = (ReferentielEntiteBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("refentitebean");
				boolean creation = refEntiteBean.getEntiteEdition().isCreation();
				if (!creation) {
					nomEcranSuffixe += "-b";
				}
			}
			
			String chemin = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

			String nomEcran = code;
			String parentNom = "";
			String parentCode = "";
			String parentOutcome = "";

			// Nom de l'écran courant
			try {
				nomEcran = RUBRIQUE_PROPERTIES.getString(nomEcranPrefixe + code + nomEcranSuffixe);
			} catch (Exception ex) {
				// Pas grave
				getTraceService().enregistrerTraceDebug("getFilAriane", "Impossible de trouver le titre pour l'écran : " + code,
						getEnteteComposant(), getContexteCedicam());
			}
			fil.add(new ElementFilAriane(nomEcran, null));

			// On remonte le fil d'Ariane
			boolean b = true;
			String defaultOutcome = null;
			int compteurHistorique = 0;
			nomEcranSuffixe = ".el000";
			while (b) {
				compteurHistorique++;
				parentOutcome = "";
				parentNom = "";
				try {
					parentCode = rbAriane.getString(code + ".parent");

					// On récupère l'URL du parent
					try {
						parentOutcome = rbAriane.getString(parentCode + ".outcome");
					} catch (Exception ex) {
						// Le parent n'a pas d'URL
						parentOutcome = defaultOutcome;
						parentOutcome = "javascript:history.go(-" + compteurHistorique + ")";
						getTraceService().enregistrerTraceDebug("getFilAriane", "Impossible de trouver l'url pour : " + parentCode,
								getEnteteComposant(), getContexteCedicam());
					}

					// On cherche le libellé du parent
					try {
						parentNom = RUBRIQUE_PROPERTIES.getString(nomEcranPrefixe + parentCode + nomEcranSuffixe);
					} catch (Exception x) {
						// Le parent n'a pas de libellé.
						parentNom = parentCode;
						getTraceService().enregistrerTraceDebug("getFilAriane", "Impossible de trouver le titre pour l'écran : " + parentCode,
								getEnteteComposant(), getContexteCedicam());
					}

					fil.add(0, new ElementFilAriane(parentNom, parentOutcome));

					// On boucle
					code = parentCode;

				} catch (Exception ex) {
					// Pas de parent trouvé : le fil d'Ariane est terminé.
					b = false;
				}
			}
		}

		return fil;
	}
}
