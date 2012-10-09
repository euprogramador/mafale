package br.com.aexo.sim.movimentacao;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.solder.servlet.http.RequestParam;

@Named
@ConversationScoped
public class MovimentacaoQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Session session;

	@Inject
	@RequestParam("id")
	private Instance<Long> id;

	@Named
	@Produces
	@ConversationScoped
	@SuppressWarnings("unchecked")
	public List<Movimentacao> movimentacoesEfetuadas() {
		return session.createCriteria(Movimentacao.class).add(Restrictions.eq("servico.id", id.get())).addOrder(Order.desc("data")).list();
	}

}
