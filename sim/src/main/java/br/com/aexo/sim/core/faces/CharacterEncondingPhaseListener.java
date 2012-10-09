package br.com.aexo.sim.core.faces;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class CharacterEncondingPhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent arg0) {
		
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
		FacesContext.getCurrentInstance().getExternalContext().setResponseCharacterEncoding("UTF-8");
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
