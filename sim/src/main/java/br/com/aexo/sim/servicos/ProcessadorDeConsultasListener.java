package br.com.aexo.sim.servicos;

import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import br.com.aexo.sim.anvisa.Anvisa;
import br.com.aexo.sim.anvisa.PeticaoNaAnvisa;
import br.com.aexo.sim.anvisa.ProcessoNaAnvisa;
import br.com.aexo.sim.anvisa.procedimentoparaconsulta.PassoExecucaoDaConsulta;
import br.com.aexo.sim.anvisa.procedimentoparaconsulta.PassoInterpretacaoDoResultadoDaConsulta;
import br.com.aexo.sim.anvisa.procedimentoparaconsulta.ProcedimentoParaConsultaNaAnvisa;
import br.com.aexo.sim.assuntos.Assunto;
import br.com.aexo.sim.consultaefetuada.ConsultaEfetuada;
import br.com.aexo.sim.movimentacao.Movimentacao;

@Stateless
public class ProcessadorDeConsultasListener {

	@PersistenceContext
	private EntityManager em;

	@Asynchronous
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void consultarNaAnvisa(@Observes Consulta consulta) {

		if (consulta.getResultadoConsulta().isContinuarConsultando()) {
			consulta.getResultadoConsulta().registrarTentativa(consulta);
			fazerConsulta(consulta);
		}

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
		ProcedimentoParaConsultaNaAnvisa procedimento = new ProcedimentoParaConsultaNaAnvisa(
				passoExecucaoDaConsulta,
				passoInterpretacaoDoResultadoDaConsulta);

		Anvisa anvisa = new Anvisa(procedimento);
		try {
			anvisa.consultar(processo);

		} catch (Exception e) {
			consulta.getResultadoConsulta().registrarErro(consulta);
		}
		List<PeticaoNaAnvisa> peticoes = processo.getPeticoes();

		for (PeticaoNaAnvisa peticao : peticoes) {
			if (servico.getExpediente().equals(peticao.getExpediente())) {
				consulta.getResultadoConsulta().registarSucesso(consulta);
				atualizarSeNecessario(consulta, peticao);
				
				ConsultaEfetuada consultaEfetuada = new ConsultaEfetuada();

				consultaEfetuada.setServico(servico);
				consultaEfetuada.setHouveModificacao(true);
				consultaEfetuada.setData(new LocalDateTime());

				em.persist(consultaEfetuada);
			}
		}
	}

	private void atualizarSeNecessario(Consulta consulta,
			PeticaoNaAnvisa peticao) {
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

		LocalDate SencontraSeDesde = servico.getEncontraSeDesde() == null ? data
				: servico.getEncontraSeDesde();
		LocalDate PencontraSeDesde = peticao.getEncontraSeDesde() == null ? data
				: peticao.getEncontraSeDesde();

		String SencontraSeNa = servico.getEncontraSeNa() == null ? "" : servico
				.getEncontraSeNa();
		String PencontraSeNa = peticao.getEncontraSeNa() == null ? "" : peticao
				.getEncontraSeNa();

		LocalDate SdataPublicacao = servico.getDataPublicacao() == null ? data
				: servico.getDataPublicacao();
		LocalDate PdataPublicacao = peticao.getDataPublicacao() == null ? data
				: peticao.getDataPublicacao();

		String Sresolucao = servico.getResolucao() == null ? "" : servico
				.getResolucao();
		String Presolucao = peticao.getResolucao() == null ? "" : peticao
				.getResolucao();

		String Ssituacao = servico.getSituacao() == null ? "" : servico
				.getSituacao();
		String Psituacao = peticao.getSituacao() == null ? "" : peticao
				.getSituacao();

		String Sassunto = servico.getAssunto() == null ? "" : servico
				.getAssunto().getDescricao();

		String Passunto = peticao.getAssunto() == null ? "" : peticao
				.getAssunto();

		if (!SencontraSeDesde.equals(PencontraSeDesde)
				|| !SencontraSeNa.equals(PencontraSeNa)
				|| !SdataPublicacao.equals(PdataPublicacao)
				|| !Sresolucao.equals(Presolucao)
				|| !Ssituacao.equals(Psituacao)
				|| !Sassunto.equals(Passunto)
				) {

			consulta.getResultadoConsulta().registrarAlteracao();

			
			Session session = em.unwrap(Session.class);
			
			Assunto assunto = (Assunto) session.createCriteria(Assunto.class).add(Restrictions.eq("descricao", peticao.getAssunto())).setMaxResults(1).uniqueResult();
			if (assunto == null) {
				assunto = new Assunto();
				assunto.setDescricao(peticao.getAssunto());
				session.saveOrUpdate(assunto);
				servico.setAssunto(assunto);
			}
			
			servico.setEncontraSeDesde(peticao.getEncontraSeDesde());
			servico.setEncontraSeNa(peticao.getEncontraSeNa());
			servico.setDataPublicacao(peticao.getDataPublicacao());
			servico.setResolucao(peticao.getResolucao());
			servico.setSituacao(peticao.getSituacao());
			servico.setDataUltimaConsulta(new LocalDateTime());

			em.merge(servico);
			em.persist(movimentacao);
			consulta.getResultadoConsulta().registrarServicoAlterado(servico);
		}
	}

}
