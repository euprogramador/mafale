package br.com.aexo.sim.consultaefetuada;

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

public class ConsultaEfetuadaQuery {

	@Inject
	private Session session;
	
	@Inject
	@RequestParam("id")
	private Instance<Long> id;
	
	@Named
	@Produces
	@ConversationScoped
	public List<ConsultaEfetuada> consultasEfetuadas(){
		return session.createCriteria(ConsultaEfetuada.class).add(Restrictions.eq("servico.id",id.get())).addOrder(Order.asc("data")).setMaxResults(10).list();
	}
	
	
}
