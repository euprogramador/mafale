package br.com.aexo.sim.servicos;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.jboss.seam.international.status.Messages;
import org.jboss.solder.servlet.http.RequestParam;

import br.com.aexo.sim.core.faces.Nav;

@Named
@ConversationScoped
public class ServicoHome implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject private Nav nav;
	
	@Inject
	@RequestParam("id")
	private Instance<Long> id;

	@Inject
	EntityManager em;

	@Inject
	Session session;

	@Inject
	Messages messages;

	private Servico servico;

	@Inject
	Conversation conversation;

	@Named
	@Produces
	@ConversationScoped
	public Servico servico() {
		Long id = this.id.get();

		if (id != null && !new Long(0).equals(id)){
			servico = (Servico) em.find(Servico.class, id);
		}
		else
			servico = new Servico();
		return servico;
	}

	public String salvar() {
		if (servico.getId() == null) {
			em.persist(servico);
		}
		em.flush();
		conversation.end();
		messages.info("Serviço salvo com sucesso");
		
		return nav.getListagemServicos().redirect().includeViewParams().s();
	}

	public String remover() {
		em.remove(servico);
		em.flush();
		em.clear();
		conversation.end();
		messages.info("Serviço removido com sucesso");
		return nav.getListagemServicos().redirect().includeViewParams().s();
	}

	public Servico getServico() {
		return servico;
	}

}
