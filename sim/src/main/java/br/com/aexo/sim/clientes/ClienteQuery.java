package br.com.aexo.sim.clientes;

import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.solder.servlet.http.DefaultValue;
import org.jboss.solder.servlet.http.RequestParam;

import br.com.aexo.sim.core.paginacao.ListaPaginada;
import br.com.aexo.sim.core.util.StringTools;

public class ClienteQuery {

	@Inject
	private Session session;

	@Inject
	@RequestParam("id")
	private Long id;
	
	@Inject
	@RequestParam("pagina")
	@DefaultValue("1")
	private Integer pagina;

	@Inject
	@RequestParam("registrosPorPagina")
	@DefaultValue("10")
	private Integer registrosPorPagina;

	@Inject
	@RequestParam("termo")
	private String termo;
	
	@Inject
	private FacesContext context;

	@Named
	@Produces
	@ConversationScoped
	public List<Cliente> comboClientes(){
		return session.createCriteria(Cliente.class).addOrder(Order.asc("razaoSocial")).list();
	}
	
	@Named
	@Produces
	@ConversationScoped
	public ListaPaginada<Cliente> listagemClientes() {
		
		if (context.isPostback()) 
			return new ListaPaginada<Cliente>();
		
		if (id!=null){
			return new ListaPaginada<Cliente>();
		}
		
		if (pagina == null)
			pagina = 1;
		if (registrosPorPagina == null)
			registrosPorPagina = 10;

		Criteria criteria = session.createCriteria(Cliente.class).addOrder(Order.asc("id"));
		int firstResult = (pagina * registrosPorPagina) - registrosPorPagina;
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(registrosPorPagina);

		if (!StringTools.isNullOrBlank(termo)) {
			Disjunction or = Restrictions.disjunction();

			or.add(Restrictions.ilike("razaoSocial", termo, MatchMode.ANYWHERE));
			or.add(Restrictions.ilike("contato", termo, MatchMode.ANYWHERE));
			or.add(Restrictions.ilike("cnpj", termo, MatchMode.ANYWHERE));

			criteria.add(or);
		}

		@SuppressWarnings("unchecked")
		List<Cliente> lista = criteria.list();

		Criteria criteriaContagem = session.createCriteria(Cliente.class);
		if (!StringTools.isNullOrBlank(termo)) {
			Disjunction or = Restrictions.disjunction();

			or.add(Restrictions.ilike("razaoSocial", termo, MatchMode.ANYWHERE));
			or.add(Restrictions.ilike("contato", termo, MatchMode.ANYWHERE));
			or.add(Restrictions.ilike("cnpj", termo, MatchMode.ANYWHERE));

			criteriaContagem.add(or);
		}
		Long contagem = (Long) criteriaContagem.setProjection(Projections.rowCount()).uniqueResult();

		return new ListaPaginada<Cliente>(lista, contagem, firstResult, registrosPorPagina);
	}
}
