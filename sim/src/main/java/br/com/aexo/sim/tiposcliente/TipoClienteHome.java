package br.com.aexo.sim.tiposcliente;

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
public class TipoClienteHome implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	@RequestParam("id")
	private Instance<Long> id;

	@Inject
	EntityManager em;

	@Inject
	Messages messages;

	private TipoCliente tipoCliente;

	@Inject
	Conversation conversation;

	@Named
	@Produces
	@ConversationScoped
	public TipoCliente tipoCliente() {
		Long id = this.id.get();

		if (id != null && !new Long(0).equals(id))
			tipoCliente = (TipoCliente) em.find(TipoCliente.class, id);
		else
			tipoCliente = new TipoCliente();
		return tipoCliente;
	}

	public String salvar() {
		if (tipoCliente.getId() == null) {
			em.persist(tipoCliente);
		}
		em.flush();
		conversation.end();
		messages.info("Tipo de cliente salvo com sucesso");
		
		return  "/admin/tiposcliente/tiposcliente.xhtml?faces-redirect=true&includeViewParams=true";
	}


	public String remover() {
		em.remove(tipoCliente);
		em.flush();
		em.clear();
		conversation.end();
		messages.info("Tipo de cliente removido com sucesso");
		return  "/admin/tiposcliente/tiposcliente.xhtml?faces-redirect=true&includeViewParams=true";
	}

}
