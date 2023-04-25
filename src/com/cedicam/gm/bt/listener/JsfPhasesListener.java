/**
 * 
 */
package com.cedicam.gm.bt.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * @author CDENIS
 *
 */
public class JsfPhasesListener implements PhaseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5601657151313992581L;

	public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    public void beforePhase(PhaseEvent event) {
        System.out.println("START PHASE " + event.getPhaseId());
    }

    public void afterPhase(PhaseEvent event) {
        System.out.println("END PHASE " + event.getPhaseId());
    }

}