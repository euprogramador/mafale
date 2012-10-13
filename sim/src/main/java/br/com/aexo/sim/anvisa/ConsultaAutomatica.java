package br.com.aexo.sim.anvisa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import br.com.aexo.sim.anvisa.procedimentoparaconsulta.PassoExecucaoDaConsulta;
import br.com.aexo.sim.anvisa.procedimentoparaconsulta.PassoInterpretacaoDoResultadoDaConsulta;
import br.com.aexo.sim.anvisa.procedimentoparaconsulta.ProcedimentoParaConsultaNaAnvisa;
import br.com.aexo.sim.consultaefetuada.ConsultaEfetuada;
import br.com.aexo.sim.movimentacao.Movimentacao;
import br.com.aexo.sim.servicos.Servico;
import br.com.aexo.sim.servicos.Status;

@Named
@SessionScoped
public class ConsultaAutomatica implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	FacesContext context;

	@Inject
	Instance<EntityManager> entityManager;

	private boolean consultar;
	private Integer intervalo = 60;
	private List<Servico> servicosAlterados = new ArrayList<Servico>();
	boolean encontradoAlteracao = false;

	public void setConsultar(boolean consultar) {
		this.consultar = consultar;
	}

	public void setServico(Long servico) {
		this.servico = servico;
	}

	private Long servico;

	private Consulta consulta;

	public Integer getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(Integer intervalo) {
		this.intervalo = intervalo;
	}

	public Long getServico() {
		return servico;
	}

	public boolean isConsultar() {
		return consultar;
	}

	public void iniciarConsultas() {
		consultar = true;
	}

	public void pararConsultas() {
		consultar = false;
		encontradoAlteracao = false;
	}

	public void pararAlerta() {
		encontradoAlteracao = false;
	}

	public void efetuarConsulta() {
		if (!consultar)
			return;

		EntityManager em = entityManager.get();

		Servico servicoAtual = em.find(Servico.class, servico);

		if (servicoAtual == null) {
			servico = proximoServico();
			System.out.println(servico);
			return;
		}
		try {
			consulta = new Consulta(servicoAtual, false);
			fazerConsulta(consulta);
		} catch (Exception e) {
			System.out.println("Ocorreu um erro ao consultar");
		}
		servico = proximoServico();

	}

	private Long proximoServico() {
		Session session = entityManager.get().unwrap(Session.class);
		Criteria criteria = session.createCriteria(Servico.class);
		criteria.add(Restrictions.eq("status", Status.EM_TRAMITACAO));
		criteria.add(Restrictions.gt("id", servico));
		criteria.addOrder(Order.asc("id"));
		criteria.addOrder(Order.desc("dataUltimaConsulta"));
		criteria.setMaxResults(1);
		Servico servico = (Servico) criteria.uniqueResult();
		if (servico == null)
			return 0L;
		return servico.getId();
	}

	public void servicoRevisado(Servico servico) {
		Session session = entityManager.get().unwrap(Session.class);
		Movimentacao movimentacao = (Movimentacao) session.createCriteria(Movimentacao.class).add(Restrictions.eq("servico", servico)).addOrder(Order.desc("id")).setMaxResults(1).uniqueResult();
		if (movimentacao != null)
			movimentacao.setConferido(true);
		servicosAlterados.remove(servico);
	}

	private void fazerConsulta(Consulta consulta) {
		ProcessoNaAnvisa processo = new ProcessoNaAnvisa();

		Servico servico = consulta.getServico();

		String processoInformado = servico.getNumeroProcesso();
		String cnpj = servico.getCliente().getCnpj();

		processo.setProcesso(processoInformado);
		processo.setCnpj(cnpj);

		PassoExecucaoDaConsulta passoExecucaoDaConsulta = new PassoExecucaoDaConsulta();
		PassoInterpretacaoDoResultadoDaConsulta passoInterpretacaoDoResultadoDaConsulta = new PassoInterpretacaoDoResultadoDaConsulta();
		ProcedimentoParaConsultaNaAnvisa procedimento = new ProcedimentoParaConsultaNaAnvisa(passoExecucaoDaConsulta, passoInterpretacaoDoResultadoDaConsulta);

		Anvisa anvisa = new Anvisa(procedimento);
		anvisa.consultar(processo);

		List<PeticaoNaAnvisa> peticoes = processo.getPeticoes();

		for (PeticaoNaAnvisa peticao : peticoes) {
			if (servico.getExpediente().equals(peticao.getExpediente())) {
				atualizar(consulta, peticao);
			}
		}
	}

	private void atualizar(Consulta consulta, PeticaoNaAnvisa peticao) {
		Servico servico = consulta.getServico();

		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setServico(servico);
		movimentacao.setData(new LocalDateTime());

		movimentacao.setDataPublicacaoAnterior(servico.getDataPublicacao());
		movimentacao.setDataPublicacaoAtual(peticao.getDataPublicacao());
		movimentacao.setEncontraSeDesdeAnterior(servico.getEncontraSeDesde());
		movimentacao.setEncontraSeDesdeAtual(peticao.getEncontraSeDesde());
		movimentacao.setEncontraSeNaAnterior(servico.getEncontraSeNa());
		movimentacao.setEncontraSeNaAtual(peticao.getEncontraSeNa());
		movimentacao.setResolucaoAnterior(servico.getResolucao());
		movimentacao.setResolucaoAtual(peticao.getResolucao());

		movimentacao.setSituacaoAnterior(servico.getSituacao());
		movimentacao.setSituacaoAtual(peticao.getSituacao());

		LocalDate data = new LocalDate();

		LocalDate SencontraSeDesde = servico.getEncontraSeDesde() == null ? data : servico.getEncontraSeDesde();
		LocalDate PencontraSeDesde = peticao.getEncontraSeDesde() == null ? data : peticao.getEncontraSeDesde();

		String SencontraSeNa = servico.getEncontraSeNa() == null ? "" : servico.getEncontraSeNa();
		String PencontraSeNa = peticao.getEncontraSeNa() == null ? "" : peticao.getEncontraSeNa();

		LocalDate SdataPublicacao = servico.getDataPublicacao() == null ? data : servico.getDataPublicacao();
		LocalDate PdataPublicacao = peticao.getDataPublicacao() == null ? data : peticao.getDataPublicacao();

		String Sresolucao = servico.getResolucao() == null ? "" : servico.getResolucao();
		String Presolucao = peticao.getResolucao() == null ? "" : peticao.getResolucao();

		String Ssituacao = servico.getSituacao() == null ? "" : servico.getSituacao();
		String Psituacao = peticao.getSituacao() == null ? "" : peticao.getSituacao();

		if (!SencontraSeDesde.equals(PencontraSeDesde) || !SencontraSeNa.equals(PencontraSeNa) || !SdataPublicacao.equals(PdataPublicacao) || !Sresolucao.equals(Presolucao)
				|| !Ssituacao.equals(Psituacao)) {
			encontradoAlteracao = true;

			servico.setEncontraSeDesde(peticao.getEncontraSeDesde());
			servico.setEncontraSeNa(peticao.getEncontraSeNa());
			servico.setDataPublicacao(peticao.getDataPublicacao());
			servico.setResolucao(peticao.getResolucao());
			servico.setSituacao(peticao.getSituacao());
			servico.setDataUltimaConsulta(new LocalDateTime());

			ConsultaEfetuada consultaEfetuada = new ConsultaEfetuada();

			consultaEfetuada.setServico(servico);
			consultaEfetuada.setHouveModificacao(true);
			consultaEfetuada.setData(new LocalDateTime());

			entityManager.get().persist(consultaEfetuada);
			entityManager.get().persist(movimentacao);
			servicosAlterados.add(servico);
			consulta.setResultado(true);
		}
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public boolean isEncontradoAlteracao() {
		return encontradoAlteracao;
	}

	public void setEncontradoAlteracao(boolean encontradoAlteracao) {
		this.encontradoAlteracao = encontradoAlteracao;
	}

	public List<Servico> getServicosAlterados() {
		return servicosAlterados;
	}

	public void setServicosAlterados(List<Servico> servicosAlterados) {
		this.servicosAlterados = servicosAlterados;
	}
}
