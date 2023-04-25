package com.cedicam.gm.bt.bean;

import com.cedicam.gm.bt.constantes.ConstantesEvtTech;
import com.cedicam.gm.bt.constantes.ConstantesTrace;
import com.cedicam.gm.ui.core.GenericBean;

/**
 * @author Administrateur
 *
 * @param <T> L'objet dto
 */
public abstract class Rfxx2Bean<T> extends GenericBean {

	/** OUTCOME_FERMER. */
	private static final String OUTCOME_FERMER = "fermer";

	/** blnSuppPhysique. */
	private boolean blnSuppPhysique = false;

	/** confirmerSuppressionPhysique. */
	private boolean confirmerSuppressionPhysique = false;
	/** confirmerAbandonModification. */
	private boolean confirmerAbandonModification = false;
	/** msgConfirmerSuppression. */
	private String msgConfirmerSuppression;
	/** msgConfirmerAbandonModification. */
	private String msgConfirmerAbandonModification;

	// public static final String ETATDTO = "1";

	/** occurrence. */
	private T occurrence;
	/** unEnreg. */
	private T unEnreg;

	/** action. */
	private String action;

	/**
	 * Consructeur.
	 */
	public Rfxx2Bean() {
		setEnteteComposant("GMBTBNRFXX20", "BEAN PRINCIPAL ECRAN DETAIL");
		this.msgConfirmerSuppression = getBtmu10().getMessage("MSG3511");
		this.msgConfirmerAbandonModification = getBtmu10().getMessage("MSG3510");
	}

	/**
	 * @return The blnSuppPhysique
	 */
	public final boolean isBlnSuppPhysique() {
		return this.blnSuppPhysique;
	}

	/**
	 * @param blnSuppPhysique The blnSuppPhysique to set
	 */
	public final void setBlnSuppPhysique(boolean blnSuppPhysique) {
		this.blnSuppPhysique = blnSuppPhysique;
	}

	/**
	 * @return The confirmerSuppressionPhysique
	 */
	public final boolean isConfirmerSuppressionPhysique() {
		return this.confirmerSuppressionPhysique;
	}

	/**
	 * @param confirmerSuppressionPhysique The confirmerSuppressionPhysique to set
	 */
	public final void setConfirmerSuppressionPhysique(boolean confirmerSuppressionPhysique) {
		this.confirmerSuppressionPhysique = confirmerSuppressionPhysique;
	}

	/**
	 * @return The confirmerAbandonModification
	 */
	public final boolean isConfirmerAbandonModification() {
		return this.confirmerAbandonModification;
	}

	/**
	 * @param confirmerAbandonModification The confirmerAbandonModification to set
	 */
	public final void setConfirmerAbandonModification(boolean confirmerAbandonModification) {
		this.confirmerAbandonModification = confirmerAbandonModification;
	}

	/**
	 * @return The msgConfirmerSuppression
	 */
	public final String getMsgConfirmerSuppression() {
		return this.msgConfirmerSuppression;
	}

	/**
	 * @param msgConfirmerSuppression The msgConfirmerSuppression to set
	 */
	public final void setMsgConfirmerSuppression(String msgConfirmerSuppression) {
		this.msgConfirmerSuppression = msgConfirmerSuppression;
	}

	/**
	 * @return The msgConfirmerAbandonModification
	 */
	public final String getMsgConfirmerAbandonModification() {
		return this.msgConfirmerAbandonModification;
	}

	/**
	 * @param msgConfirmerAbandonModification The msgConfirmerAbandonModification to set
	 */
	public final void setMsgConfirmerAbandonModification(String msgConfirmerAbandonModification) {
		this.msgConfirmerAbandonModification = msgConfirmerAbandonModification;
	}

	/**
	 * @return The occurrence
	 */
	public final T getOccurrence() {
		return this.occurrence;
	}

	/**
	 * @param occurrence The occurrence to set
	 */
	public final void setOccurrence(T occurrence) {
		this.occurrence = occurrence;
	}

	/**
	 * @return The action
	 */
	public final String getAction() {
		return this.action;
	}

	/**
	 * @param action The action to set
	 */
	public final void setAction(String action) {
		this.action = action;
	}

	/**
	 * @param unEnreg The unEnreg to set
	 */
	public final void setUnEnreg(T unEnreg) {
		this.unEnreg = unEnreg;
	}

	/**
	 * getSupprimerPhysique.
	 * @return Le flag
	 */
	public abstract boolean getSupprimerPhysique();

	/**
	 * appelServiceCreer.
	 * @return Un outcome
	 */
	public abstract String appelServiceCreer();

	/**
	 * appelServiceModifier.
	 * @return Un outcome
	 */
	public abstract String appelServiceModifier();

	/**
	 * appelServiceSupprimer.
	 * @return Un outcome
	 */
	public abstract String appelServiceSupprimer();

	/**
	 * appelServiceRechercher.
	 * @return L'objet dto
	 */
	public abstract T appelServiceRechercher();

	/**
	 * initUnEnrg.
	 */
	protected abstract void initUnEnrg();

	/**
	 * getUnEnreg1.
	 * @return L'objet dto
	 */
	public T getUnEnreg1() {
		return unEnreg;
	}

	/**
	 * getUnEnreg.
	 * @return L'objet dto
	 */
	public T getUnEnreg() {
		initUnEnrg();
		return unEnreg;
	}

	/**
	 * isUnEneg.
	 * @return Le flag
	 */
	public boolean isUnEneg() {
		return unEnreg != null;
	}

	/**
	 * valider.
	 * @return Un outcome
	 */
	public String valider() {
		if ("ajouter".equals(this.action)) {
			return appelServiceCreer();

		} else {
			if ("modifier".equals(this.action)) {
				return appelServiceModifier();

			} else {
				if ("supprimer".equals(this.action)) {
					if (getSupprimerPhysique()) {
						this.confirmerSuppressionPhysique = true;
						return null;

					} else {
						this.confirmerSuppressionPhysique = false;
						this.blnSuppPhysique = false;
						return appelServiceSupprimer();
					}
				} else {
					getEvenementService().emettreEvenement("valider", ConstantesEvtTech.EVT_PARAM_INVALIDE_INCOHERENT,
							ConstantesTrace.CATEGORIE_ERREUR, ConstantesTrace.GRAVITE_FATAL, "MSG0051", getEnteteComposant(),
							getContexteCedicam());

					getBtmu10().addMessage(Btmu10.MSG_ERREUR, "MSG0051");
					return "erreur";
				}
			}
		}
	}

	/**
	 * fermer.
	 * @return Un outcome
	 */
	public final String fermer() {
		if (isModification()) {
			this.confirmerAbandonModification = true;
			return null;
		} else {
			this.confirmerAbandonModification = false;
			return appelCodeFermeture();
		}

	}

	/**
	 * validerSuppressionPhysique.
	 * @return Un outcome
	 */
	public String validerSuppressionPhysique() {
		this.confirmerSuppressionPhysique = false;
		this.confirmerAbandonModification = false;

		this.blnSuppPhysique = true;
		return appelServiceSupprimer();
	}

	/**
	 * validerSuppressionLogique.
	 * @return Un outcome
	 */
	public String validerSuppressionLogique() {
		this.confirmerSuppressionPhysique = false;
		this.confirmerAbandonModification = false;

		this.blnSuppPhysique = false;
		return appelServiceSupprimer();
	}

	/**
	 * validerSortie.
	 * @return Un outcome
	 */
	public String validerSortie() {
		this.confirmerSuppressionPhysique = false;
		this.confirmerAbandonModification = false;

		return appelCodeFermeture();
	}

	/**
	 * continuerSaisie.
	 * @return Un outcome
	 */
	public String continuerSaisie() {
		this.confirmerSuppressionPhysique = false;
		this.confirmerAbandonModification = false;

		return null;
	}

	/**
	 * appelCodeFermeture.
	 * @return Un outcome
	 */
	public String appelCodeFermeture() {
		return Rfxx2Bean.OUTCOME_FERMER;
	}

}
