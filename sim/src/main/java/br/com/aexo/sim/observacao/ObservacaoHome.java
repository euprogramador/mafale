package br.com.aexo.sim.observacao;

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

import br.com.aexo.sim.core.faces.Nav;
import br.com.aexo.sim.seguranca.UsuarioLogado;
import br.com.aexo.sim.servicos.Servico;

@Named
@ConversationScoped
public class ObservacaoHome implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager em;

	@Inject
	Messages messages;

	@Inject
	UsuarioLogado usuarioLogado;

	private Observacao observacao;

	@Inject
	Conversation conversation;
	
	@Inject
	@RequestParam("id")
	private Instance<Long> id;
	
	@Inject
	Nav nav;

	@Named
	@Produces
	@ConversationScoped
	public Observacao observacao() {
		observacao = new Observacao();
		return observacao;
	}

	public String salvar() {
		observacao.setUsuario(usuarioLogado.getUsuario().getNome());
		Servico servico = em.getReference(Servico.class, id.get());
		observacao.setServico(servico);
		
		em.persist(observacao);
		em.flush();
		conversation.end();
		messages.info("Observação salva com sucesso");

		String s = nav.getDetalheServico().redirect().includeViewParams().s();
		return s;
	}

}
