package br.com.aexo.sim.tiposcliente;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

public class TipoClienteQuery {

	@Inject
	private Session session;

	@Inject
	private FacesContext context;

	@SuppressWarnings("unchecked")
	@Named
	@Produces
	@ConversationScoped
	public List<TipoCliente> listagemDeTiposDeClientes() {
		if (context.isPostback())
			return new ArrayList<TipoCliente>();
		
		return session.createCriteria(TipoCliente.class).addOrder(Order.asc("descricao")).list();
	}
}
