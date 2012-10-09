package br.com.aexo.sim.clientes;

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
public class ClienteHome implements Serializable {

	private static final long serialVersionUID = 1L;

	private String client;

	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	
	private String produto;

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	@Inject
	@RequestParam("id")
	private Instance<Long> id;

	@Inject
	EntityManager em;

	@Inject
	Messages messages;

	private Cliente cliente;

	@Inject
	Conversation conversation;

	@Named
	@Produces
	@ConversationScoped
	public Cliente cliente() {
		Long id = this.id.get();

		if (id != null && !new Long(0).equals(id))
			cliente = (Cliente) em.find(Cliente.class, id);
		else
			cliente = new Cliente();
		return cliente;
	}

	public String salvar() {
		if (cliente.getId() == null) {
			em.persist(cliente);
		}
		em.flush();
		conversation.end();
		messages.info("Cliente salvo com sucesso");
		return "/admin/clientes/clientes.xhtml?faces-redirect=true&amp;includeViewParams=true";
	}

	public String remover() {
		em.remove(cliente);
		em.flush();
		em.clear();
		conversation.end();
		messages.info("Cliente removido com sucesso");
		return "/admin/clientes/clientes.xhtml?faces-redirect=true&includeViewParams=true";
	}

}
