package br.com.aexo.sim.tiposservico;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.seam.international.status.Messages;
import org.jboss.solder.exception.control.ExceptionToCatch;
import org.jboss.solder.servlet.http.RequestParam;

import br.com.aexo.sim.core.exceptions.RegistroInexistenteException;

@Named
@ConversationScoped
public class TipoServicoHome implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@RequestParam("id")
	private Instance<Long> id;

	@Inject
	EntityManager em;

	@Inject
	Messages messages;

	private TipoServico tipoServico;

	@Inject
	Conversation conversation;

	@Named("tipoServico")
	@Produces
	@ConversationScoped
	public TipoServico carregar() {
		Long id = this.id.get();

		if (id != null && !new Long(0).equals(id))
			tipoServico = (TipoServico) em.find(TipoServico.class, id);
		else
			tipoServico = new TipoServico();
		
		if (tipoServico ==null){
			throw new RegistroInexistenteException("Tentativa de alterar um registro que não existe mais", "/admin/tiposservico/tiposservico.xhtml?faces-redirect=true&includeViewParams=true");
		}
		
		return tipoServico;
	}

	public String salvar() {
		if (tipoServico.getId() == null) {
			em.persist(tipoServico);
		}
		em.flush();
		conversation.end();
		messages.info("Tipo de serviço salvo com sucesso");
		return "/admin/tiposservico/tiposservico.xhtml?faces-redirect=true&includeViewParams=true";
	}


	public String remover() {
		em.remove(tipoServico);
		em.flush();
		em.clear();
		conversation.end();
		messages.info("Tipo de serviço removido com sucesso");
		return "/admin/tiposservico/tiposservico.xhtml?faces-redirect=true&includeViewParams=true";
	}

}
