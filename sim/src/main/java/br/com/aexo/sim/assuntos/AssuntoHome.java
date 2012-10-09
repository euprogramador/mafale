package br.com.aexo.sim.assuntos;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.seam.international.status.Messages;
import org.jboss.solder.servlet.http.RequestParam;

@Named
@ConversationScoped
public class AssuntoHome implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@RequestParam("id")
	private Instance<Long> id;

	@Inject
	EntityManager em;

	@Inject
	Messages messages;

	private Assunto assunto;

	@Inject
	Conversation conversation;

	@Named
	@Produces
	@ConversationScoped
	public Assunto assunto() {
		Long id = this.id.get();

		if (id != null && !new Long(0).equals(id))
			assunto = (Assunto) em.find(Assunto.class, id);
		else
			assunto = new Assunto();
		return assunto;
	}

	public String salvar() {
		if (assunto.getId() == null) {
			em.persist(assunto);
		}
		em.flush();
		conversation.end();
		messages.info("Assunto salvo com sucesso");
		return "/admin/assuntos/assuntos.xhtml?faces-redirect=true&includeViewParams=true";
	}


	public String remover() {
		em.remove(assunto);
		em.flush();
		em.clear();
		conversation.end();
		messages.info("Assunto removido com sucesso");

		return "/admin/assuntos/assuntos.xhtml?faces-redirect=true&includeViewParams=true";
	}

}
