package br.com.aexo.sim.core.exceptions;

import javax.enterprise.inject.Instance;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

import org.jboss.seam.international.status.Messages;
import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.exception.control.HandlesExceptions;
import org.jboss.weld.context.http.HttpConversationContext;

import br.com.aexo.sim.core.faces.ConversationManagement;

@HandlesExceptions
public class GerenciadorDeExceptions {

	@Inject
	Messages messages;
	@Inject
	ConversationManagement conversationManagement;

	public void tratarEntidadeQueNaoPodeSerExcluida(@Handles CaughtException<EntidadeRelacionadaException> e) {
		messages.error(e.getException().getMessage());
		conversationManagement.end();
		e.handled();
	}

	public void tratarEntidadeQueNaoExiste(@Handles CaughtException<RegistroInexistenteException> e, HttpConversationContext conversationContext) {
		
		
		// TODO resolver problema da falta de mensagem
		
		if (!conversationContext.isActive())
			conversationContext.activate(null);
		

		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "You do not have the necessary permissions to perform that operation", ""));

		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.doPostPhaseActions(facesContext);
		flash.setRedirect(true);
		String redirect = e.getException().getPagina();
		NavigationHandler myNav = facesContext.getApplication().getNavigationHandler();
		myNav.handleNavigation(facesContext, null, redirect);
		facesContext.renderResponse();

		e.handled();

	}
}
