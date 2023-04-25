package com.cedicam.gm.bt.bean;

import java.util.LinkedList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.springframework.remoting.RemoteAccessException;

import com.cedicam.gm.bt.service.ReponseService;
import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesNuerf;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.bt.constantes.ConstantesTypo;
import com.cedicam.gm.bt.core.service.TypologieService;
import com.cedicam.gm.bt.data.dto.GenericDto;
import com.cedicam.gm.bt.data.model.Typologie;
import com.cedicam.gm.bt.exception.ErreurTechnique;
import com.cedicam.gm.bt.spring.ApplicationContextHolder;

/**
 *
 * @author Administrateur
 *
 * @param <T>
 */
public abstract class Rfxx1Bean<T extends GenericDto> extends DataSelectionGenericBean<T> {

	/** STYLE_DISABLED. */
	public static final String STYLE_DISABLED = "wn-btn-griser";

	/** disabledBtnModifier. */
	private boolean disabledBtnModifier = true;
	/** disabledBtnSupprimer. */
	private boolean disabledBtnSupprimer = true;

	/** accessible. */
	private Boolean accessible = true;

	/** habAjouter. */
	private Boolean habAjouter = true;

	/** habModifier. */
	private Boolean habModifier = true;

	/** habSupprimer. */
	private Boolean habSupprimer = true;

	/** typologieService. */
	private TypologieService typologieService;

	/** criteresStatut. */
	private List<Typologie> criteresStatut;

	/** listItem. */
	private List<SelectItem> listItem = new LinkedList<SelectItem>();

	/** selectedStatus. */
	private String selectedStatus = ConstantesTypo.STATUT_VALIDITE_ENCOURS;


	/** modification. */
	private boolean modification = false;

	/**
	 * Consructeur.
	 */
	public Rfxx1Bean() {
		setEnteteComposant("GMBTBNRFXX10", "BEAN PRINCIPAL ECRAN LISTE");
		typologieService = (TypologieService) ApplicationContextHolder.getContext().getBean("typologieService");
		appelServiceTypologie();
		appelServiceDroitFonction();

		setDisabledBtnModifier(true);
		setDisabledBtnSupprimer(true);
	}

	/**
	 * @return The accessible
	 */
	public final Boolean getAccessible() {
		return this.accessible;
	}

	/**
	 * @param accessible The accessible to set
	 */
	public final void setAccessible(Boolean accessible) {
		this.accessible = accessible;
	}

	/**
	 * @return The criteresStatut
	 */
	public final List<Typologie> getCriteresStatut() {
		return this.criteresStatut;
	}

	/**
	 * @param criteresStatut The criteresStatut to set
	 */
	public final void setCriteresStatut(List<Typologie> criteresStatut) {
		this.criteresStatut = criteresStatut;
	}

	/**
	 * @return The listItem
	 */
	public final List<SelectItem> getListItem() {
		return this.listItem;
	}

	/**
	 * @param listItem The listItem to set
	 */
	public final void setListItem(List<SelectItem> listItem) {
		this.listItem = listItem;
	}

	/**
	 * @return L'etat
	 */
	public abstract String getStatuOccurrence();

	/**
	 * @return The modification
	 */
	public final boolean isModification() {
		return this.modification;
	}

	/**
	 * @param modification The modification to set
	 */
	public final void setModification(boolean modification) {
		this.modification = modification;
	}

	/**
	 * @return L'etat
	 */
	public String getSelectedStatus() {
		return selectedStatus;
	}

	/**
	 * @param selectedStatus L'etat
	 */
	public void setSelectedStatus(String selectedStatus) {
		this.selectedStatus = selectedStatus;

		setDisabledBtnModifier(true);
		setDisabledBtnSupprimer(true);

	}

	/**
	 * @return Le style css
	 */
	public String getClassSupplementaireBtnModifier() {
		String ret = "";
		if (isDisabledBtnModifier()) {
			ret = STYLE_DISABLED;
		}
		return ret;
	}
	/**
	 * @return Le style css
	 */
	public String getClassSupplementaireBtnSupprimer() {
		String ret = "";
		if (isDisabledBtnSupprimer()) {
			ret = STYLE_DISABLED;
		}
		return ret;
	}

	/**
	 * @return Le flag
	 */
	public Boolean getHabAjouter() {
		return habAjouter;
	}

	/**
	 * @param habAjouter Le flag
	 */
	public void setHabAjouter(Boolean habAjouter) {
		this.habAjouter = habAjouter;
	}

	/**
	 * @return Le flag
	 */
	public Boolean getHabModifier() {
		return habModifier;
	}

	/**
	 * @param habModifier Le flag
	 */
	public void setHabModifier(Boolean habModifier) {
		this.habModifier = habModifier;
	}

	/**
	 * @return Le flag
	 */
	public Boolean getHabSupprimer() {
		return habSupprimer;
	}

	/**
	 * @param habSupprimer Le flag
	 */
	public void setHabSupprimer(Boolean habSupprimer) {
		this.habSupprimer = habSupprimer;
	}

	/**
	 * @return Le service
	 */
	public TypologieService getTypologieService() {
		return typologieService;
	}

	/**
	 * @param typologieService Le service
	 */
	public void setTypologieService(TypologieService typologieService) {
		this.typologieService = typologieService;
	}

	// APPEL SERVICE
	/**
	 *
	 */
	public void appelServiceTypologie() {
		try {
			ReponseService<List<Typologie>> reponse = typologieService.listerTypologie("StatutValidite", getContexteCedicam());

			switch (reponse.getNuerf()) {
			case ConstantesNuerf.TRAITEMENT_COMPLETED:
				criteresStatut = reponse.getDonnees();
				for (Typologie typologie : criteresStatut) {
					listItem.add(new SelectItem(typologie.getCode(), typologie.getLibelle()));
				}

				break;

			case ConstantesNuerf.AUCUN_ENREGISTREMENT:
				getEvenementService().emettreEvenement("appelServiceTypologie", ConstantesEvtTech.EVT_OBJET_ABSENT_BDD,
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, "MSG3509", getEnteteComposant(),
						getContexteCedicam());

				getBtmu10().addMessage(Btmu10.MSG_INFO, "MSG3509");
				break;

			case ConstantesNuerf.PARAMETRE_NON_VALIDE:
				getEvenementService().emettreEvenement("appelServiceTypologie", ConstantesEvtTech.EVT_PARAM_INVALIDE_INCOHERENT,
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, "MSG0051", getEnteteComposant(),
						getContexteCedicam());

				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0051");
				break;

			default:
				getEvenementService().emettreEvenement("appelServiceTypologie", ConstantesEvtTech.EVT_PARAM_INVALIDE_INCOHERENT,
						ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_WARN, "MSG00144",
						new String[] {"typologieService.listerTypologie", String.valueOf(reponse.getNuerf()) + " " + reponse.getLierf() },
						getEnteteComposant(), getContexteCedicam());

				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0144",
						new String[] {"typologieService.listerTypologie", String.valueOf(reponse.getNuerf()) + " " + reponse.getLierf() });
				break;
			}
		} catch (ErreurTechnique e) {
			getEvenementService().emettreEvenement("appelServiceTypologie", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL, "MSG0143", "typologieService.listerTypologie",
					getEnteteComposant(), getContexteCedicam());

			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0143", "typologieService.listerTypologie");

		} catch (RemoteAccessException e) {
			// Cas où la couche métier n'est pas accessible: on est en code évènement "EVT_EXCEPTION_COUCHE_METIER_DETECTEE"
			getEvenementService().emettreEvenement("appelServiceTypologie", ConstantesEvtTech.EVT_EXCEPTION_COUCHE_METIER_DETECTEE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL, "MSG0097", e, getEnteteComposant(),
					getContexteCedicam());

			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0163", "appelServiceTypologie");
		} catch (Exception e) {
			getEvenementService().emettreEvenement("appelServiceTypologie", ConstantesEvtTech.EVT_EXCEPTION_INATTENDUE,
					ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL, "MSG0097", e, getEnteteComposant(),
					getContexteCedicam());

			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0163", "appelServiceTypologie");
		}
	}

	/**
	 *
	 */
	public void appelServiceDroitFonction() {
		accessible = true;
		habAjouter = true;
		habModifier = true;
		habSupprimer = true;
	}

	//                Action
	/**
	 * @return Un outcome
	 */
/*	public String ajouter() {
		this.setScrollerPage(getScrollerPage());
		return "ajouter";
	}*/

	/**
	 * @param action L'action
	 * @return Le flag
	 */
	public String isSelectionValide(String action) {
		if (getSelection().getKeys() != null && getSelection().getKeys().hasNext()) {
			if (getStatuOccurrence() == null || getStatuOccurrence().equalsIgnoreCase(ConstantesTypo.STATUT_VALIDITE_ECHU)) {
				getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0025");
				return null;
			} else {
				return action;
			}
		} else {
			getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0002");
			return null;
		}
	}

	/**
	 * @return Un outcome
	 */
	public String modifier() {
		return isSelectionValide("modifier");
	}

	/**
	 * @return Un outcome
	 */
	public String supprimer() {
		return isSelectionValide("supprimer");
	}

	/**
	 * @param disabledBtnModifier  Le flag
	 */
	public void setDisabledBtnModifier(boolean disabledBtnModifier) {
		this.disabledBtnModifier = disabledBtnModifier;
	}

	/**
	 * @return Le flag
	 */
	public boolean isDisabledBtnModifier() {
		return disabledBtnModifier;
	}


	/**
	 * @param disabledBtnSupprimer Le flag
	 */
	public void setDisabledBtnSupprimer(boolean disabledBtnSupprimer) {
		this.disabledBtnSupprimer = disabledBtnSupprimer;
	}

	/**
	 * @return Le flag
	 */
	public boolean isDisabledBtnSupprimer() {
		return disabledBtnSupprimer;
	}

	/**
	 * @param evt Le changement
	 */
	public void actDetecterStatutOccurrence(ActionEvent evt) {
		setDisabledBtnModifier(true);
		setDisabledBtnSupprimer(true);

		if (getSelection().getKeys() != null && getSelection().getKeys().hasNext()) {
			if (ConstantesTypo.STATUT_VALIDITE_ECHU.equalsIgnoreCase(getStatuOccurrence())) {
				setDisabledBtnModifier(true);
				setDisabledBtnSupprimer(true);
			} else {
				setDisabledBtnModifier(false);
				setDisabledBtnSupprimer(false);
			}
		}
	}

/*	@Override
	public void updateToTrue() {
		super.updateToTrue();
		setDisabledBtnModifier(true);
		setDisabledBtnSupprimer(true);
	}
*/
}
