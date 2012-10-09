package br.com.aexo.sim.observacao;

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

public class ObservacaoQuery {

	@Inject
	private Session session;
	
	@Inject
	@RequestParam("id")
	private Instance<Long> id;
	
	@Named
	@Produces
	@ConversationScoped
	public List<Observacao> observacoes(){
		return session.createCriteria(Observacao.class).add(Restrictions.eq("servico.id", id.get())).addOrder(Order.desc("data")).list();
	}
	
}
