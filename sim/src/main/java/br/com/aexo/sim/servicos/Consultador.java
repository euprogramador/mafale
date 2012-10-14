package br.com.aexo.sim.servicos;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

@Named
@SessionScoped
public class Consultador implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer consultarNovamenteEmXHoras = 3 ;
	
	
	@Inject
	private Event<Consulta> processadorConsulta;

	@Inject
	private Instance<EntityManager> em;

	
	private ResultadoConsulta resultadoConsulta = new ResultadoConsulta();

	private boolean consultando = false;
	
	public void atualizarStatus(){}
	
	@PostConstruct
	public void configurar(){
		resultadoConsulta.setProcessadorConsulta(processadorConsulta);
	}
	
	public void efetuarConsulta(){
		if (!consultando)
			return;
		fazerConsultas();
	}

	public void pararConsultas(){
		consultando= false;
		resultadoConsulta.setContinuarConsultando(false);
		pararAlerta();
	}
	public void pararAlerta(){
		resultadoConsulta.setRegistrosAlterados(false);
	}
	
	public void iniciarConsultas(){
		consultando = true;
		resultadoConsulta.setContinuarConsultando(true);
		fazerConsultas();
	}
	
	public void fazerConsultas() {
		Session session = this.em.get().unwrap(Session.class);

		Integer firstResult = 0;
		Integer maxResults = 30;

		Long total = (Long) session.createCriteria(Servico.class).add(Restrictions.eq("status", Status.EM_TRAMITACAO)).setProjection(Projections.rowCount()).uniqueResult();
		resultadoConsulta.setQuantidadeDeConsultas(0);
		resultadoConsulta.setQuantidadeDeConsultasRestantes(0);
		while (firstResult < total) {
			Criteria criteria = session.createCriteria(Servico.class).add(Restrictions.eq("status", Status.EM_TRAMITACAO));
			criteria.setFirstResult(firstResult);
			criteria.setMaxResults(maxResults);

			@SuppressWarnings("unchecked")
			List<Servico> servicos = criteria.list();

			for (Servico servico : servicos) {
				
				Integer quantidade = resultadoConsulta.getQuantidadeDeConsultas();
				quantidade++;
				resultadoConsulta.setQuantidadeDeConsultas(quantidade);
				
				quantidade = resultadoConsulta.getQuantidadeDeConsultasRestantes();
				quantidade++;
				resultadoConsulta.setQuantidadeDeConsultasRestantes(quantidade);
				
				processadorConsulta.fire(new Consulta(servico, getResultadoConsulta()));
			}

			// faz a paginação
			firstResult += servicos.size() < maxResults ? servicos.size() : maxResults;
			session.clear();
		}
	}

	
	
	public ResultadoConsulta getResultadoConsulta() {
		return resultadoConsulta;
	}

	public void setResultadoConsulta(ResultadoConsulta resultadoConsulta) {
		this.resultadoConsulta = resultadoConsulta;
	}

	public Integer getConsultarNovamenteEmXHoras() {
		return consultarNovamenteEmXHoras;
	}

	public void setConsultarNovamenteEmXHoras(Integer consultarNovamenteEmXHoras) {
		this.consultarNovamenteEmXHoras = consultarNovamenteEmXHoras;
	}

}
