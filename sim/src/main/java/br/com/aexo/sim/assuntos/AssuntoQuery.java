package br.com.aexo.sim.assuntos;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

public class AssuntoQuery {

	@Inject
	private Session session;

	@Inject
	private FacesContext context;

	@SuppressWarnings("unchecked")
	@Named
	@Produces
	@ConversationScoped
	public List<Assunto> listagemDeAssuntos() {

		if (context.isPostback())
			return new ArrayList<Assunto>();
 
		return session.createCriteria(Assunto.class).addOrder(Order.asc("descricao")).list();
	}

}
