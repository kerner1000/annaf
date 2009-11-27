package de.mpg.mpiz.koeln.anna;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import de.kerner.commons.workflow.WorkFlowException;

public class InformByFacesMessageWorkFlowException extends WorkFlowException {

	private static final long serialVersionUID = -6001010151572002001L;

	public InformByFacesMessageWorkFlowException() {
		
	}

	public InformByFacesMessageWorkFlowException(String message) {
		super(message);
		
	}

	public InformByFacesMessageWorkFlowException(Throwable cause) {
		super(cause);
		
	}

	public InformByFacesMessageWorkFlowException(String message, Throwable cause) {
		super(message, cause);
		
	}

	@Override
	public void doSomething() {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(getLocalizedMessage()));
	}

}
