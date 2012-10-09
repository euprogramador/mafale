package br.com.aexo.sim.core.faces;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.solder.logging.Logger;

@RequestScoped
@Named
public class ConversationManagement implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;
	
	@Inject
	private Conversation conversation;	
	
	public String begin()
	{
		if(conversation.isTransient())
		{
			conversation.begin();
			log.info("Began Long Running Conversation <" + conversation.getId() + ">");
			return null;
		}
		else
		{
			log.info("Long Running Conversation <" + conversation.getId() + "> already active");
			return null;
		}
	}

	public void end() {
		if (!conversation.isTransient()){
			conversation.end();
		}
	}

}
