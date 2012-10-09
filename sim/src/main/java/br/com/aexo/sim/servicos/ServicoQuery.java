package br.com.aexo.sim.servicos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jboss.solder.servlet.http.DefaultValue;
import org.jboss.solder.servlet.http.RequestParam;
import org.joda.time.LocalDate;

import edu.emory.mathcs.backport.java.util.Arrays;

import br.com.aexo.sim.core.paginacao.ListaPaginada;
import br.com.aexo.sim.core.util.StringTools;

public class ServicoQuery {

	@Inject
	Session session;

	@Inject
	@RequestParam("pagina")
	@DefaultValue("1")
	private Integer pagina;

	@Inject
	@RequestParam("registrosPorPagina")
	@DefaultValue("10")
	private Integer registrosPorPagina;

	@Inject
	@RequestParam("cliente")
	private String cliente;

	@Inject
	@RequestParam("produto")
	private String produto;

	@Inject
	@RequestParam("statusPendente")
	private String statusPendente;

	@Inject
	@RequestParam("dataInicialPendente")
	private String dataInicialPendente;

	@Inject
	@RequestParam("dataFinalPendente")
	private String dataFinalPendente;

	@Inject
	@RequestParam("statusTramitacao")
	private String statusTramitacao;

	@Inject
	@RequestParam("dataInicialTramitacao")
	private String dataInicialTramitacao;

	@Inject
	@RequestParam("dataFinalTramitacao")
	private String dataFinalTramitacao;

	@Inject
	@RequestParam("statusConcluidoComRenovacao")
	private String statusConcluidoComRenovacao;

	@Inject
	@RequestParam("dataInicialConcluidoComRenovacao")
	private String dataInicialConcluidoComRenovacao;

	@Inject
	@RequestParam("dataFinalConcluidoComRenovacao")
	private String dataFinalConcluidoComRenovacao;

	@Inject
	@RequestParam("statusConcluidoSemRenovacao")
	private String statusConcluidoSemRenovacao;

	@Inject
	@RequestParam("dataInicialConcluidoSemRenovacao")
	private String dataInicialConcluidoSemRenovacao;

	@Inject
	@RequestParam("dataFinalConcluidoSemRenovacao")
	private String dataFinalConcluidoSemRenovacao;

	@Named
	@Produces
	@RequestScoped
	public ListaPaginada<Servico> listagemServicos() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return new ListaPaginada<Servico>();
		}

		if (pagina == null)
			pagina = 1;
		if (registrosPorPagina == null)
			registrosPorPagina = 10;

		Criteria criteria = session.createCriteria(Servico.class).addOrder(Order.asc("cliente.razaoSocial"));
		int firstResult = (pagina * registrosPorPagina) - registrosPorPagina;
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(registrosPorPagina);
		executarFiltroDeServicoNa(criteria);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<Servico> lista = criteria.list();

		Criteria criteriaContagem = session.createCriteria(Servico.class);
		executarFiltroDeServicoNa(criteriaContagem);
		Long contagem = (Long) criteriaContagem.setProjection(Projections.rowCount()).uniqueResult();
		return new ListaPaginada<Servico>(lista, contagem, firstResult, registrosPorPagina);
	}

	private void executarFiltroDeServicoNa(Criteria criteria) {
		criteria.createAlias("cliente", "cliente");

		if (!StringTools.isNullOrBlank(cliente)) {
			Disjunction or = Restrictions.disjunction();
			or.add(Restrictions.ilike("cliente.razaoSocial", cliente, MatchMode.ANYWHERE));
			or.add(Restrictions.ilike("cliente.cnpj", cliente, MatchMode.ANYWHERE));
			criteria.add(or);
		}

		if (!StringTools.isNullOrBlank(produto)) {
			Disjunction or = Restrictions.disjunction();
			or.add(Restrictions.ilike("nomeProduto", produto, MatchMode.ANYWHERE));
			criteria.add(or);
		}

		Disjunction orStatus = Restrictions.disjunction();
		boolean usouFiltroPorSituacao = false;

		if (!StringTools.isNullOrBlank(statusPendente)) {
			usouFiltroPorSituacao = true;
			Conjunction and = Restrictions.conjunction();
			and.add(Restrictions.eq("status", Status.PENDENTE));

			Date data = converteData(dataInicialPendente);
			if (data != null) {
				and.add(Restrictions.ge("dataStatus", LocalDate.fromDateFields(data)));
			}

			data = converteData(dataFinalPendente);
			if (data != null) {
				and.add(Restrictions.le("dataStatus", LocalDate.fromDateFields(data)));
			}
			orStatus.add(and);
		}

		if (!StringTools.isNullOrBlank(statusTramitacao)) {
			usouFiltroPorSituacao = true;
			Conjunction and = Restrictions.conjunction();
			and.add(Restrictions.eq("status", Status.EM_TRAMITACAO));

			Date data = converteData(dataInicialTramitacao);
			if (data != null) {
				and.add(Restrictions.ge("dataStatus", LocalDate.fromDateFields(data)));
			}

			data = converteData(dataFinalTramitacao);
			if (data != null) {
				and.add(Restrictions.le("dataStatus", LocalDate.fromDateFields(data)));
			}
			orStatus.add(and);
		}

		if (!StringTools.isNullOrBlank(statusConcluidoComRenovacao)) {
			usouFiltroPorSituacao = true;
			Conjunction and = Restrictions.conjunction();
			and.add(Restrictions.eq("status", Status.CONCLUIDO_COM_RENOVACAO));


			Date data = converteData(dataInicialConcluidoComRenovacao);
			if (data != null) {
				and.add(Restrictions.ge("dataStatus", LocalDate.fromDateFields(data)));
			}

			data = converteData(dataFinalConcluidoComRenovacao);
			if (data != null) {
				and.add(Restrictions.le("dataStatus", LocalDate.fromDateFields(data)));
			}
			orStatus.add(and);
		}

		if (!StringTools.isNullOrBlank(statusConcluidoSemRenovacao)) {
			usouFiltroPorSituacao = true;
			Conjunction and = Restrictions.conjunction();
			and.add(Restrictions.eq("status", Status.CONCLUIDO_COM_RENOVACAO));

			Date data = converteData(dataInicialConcluidoSemRenovacao);
			if (data != null) {
				and.add(Restrictions.ge("dataStatus", LocalDate.fromDateFields(data)));
			}

			data = converteData(dataFinalConcluidoSemRenovacao);
			if (data != null) {
				and.add(Restrictions.le("dataStatus", LocalDate.fromDateFields(data)));
			}
			orStatus.add(and);
		}
		if (usouFiltroPorSituacao)
			criteria.add(orStatus);
	}

	private Date converteData(String data) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			return format.parse(data);
		} catch (Exception e) {
			// data informada é inválida
		}
		return null;
	}
	
	@Named
	@Produces
	@RequestScoped
	public List<Status> statusDosServicos(){
		return Arrays.asList(Status.values());
	}

}
