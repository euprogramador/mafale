package br.com.aexo.sim.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Event;

public class ResultadoConsulta implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean continuarConsultando;

	// tempo em minutos
	private Integer consultarNovamenteEmCasoDeErro = 5;

	private Integer quantidaDeConsultasComErro = 0;

	private Integer quantidadeDeConsultas = 0;

	private Integer quantidadeDeConsultasRestantes = 0;

	private Integer quantidadeDeTentativas = 0;

	private boolean registrosAlterados = false;

	private List<Servico> servicosAlterados = new ArrayList<Servico>();

	private Event<Consulta> processadorConsulta;

	private AgendadorExecucaoConsulta agendadorExecucao;

	public synchronized void registrarTentativa(Consulta consulta) {
		quantidadeDeTentativas++;
	}

	public synchronized void registrarErro(Consulta consulta) {
		quantidaDeConsultasComErro++;
		consulta.setDelay(1000 * 60 * consultarNovamenteEmCasoDeErro);
		agendadorExecucao.agendar(consulta);
	}

	public synchronized void registrarQuantidadeDeConsultas(Integer quantidade) {
		quantidadeDeConsultas = quantidade;
		quantidadeDeConsultasRestantes = quantidade;
	}

	public synchronized void registarSucesso(Consulta consulta) {
		quantidadeDeConsultasRestantes--;
	}

	public void registrarAlteracao() {
		registrosAlterados = true;
	}

	public synchronized void registrarServicoAlterado(Servico servico) {
		servicosAlterados.add(servico);
	}

	public Integer getQuantidaDeConsultasComErro() {
		return quantidaDeConsultasComErro;
	}

	public void setQuantidaDeConsultasComErro(Integer quantidaDeConsultasComErro) {
		this.quantidaDeConsultasComErro = quantidaDeConsultasComErro;
	}

	public Integer getQuantidadeDeConsultas() {
		return quantidadeDeConsultas;
	}

	public void setQuantidadeDeConsultas(Integer quantidadeDeConsultas) {
		this.quantidadeDeConsultas = quantidadeDeConsultas;
	}

	public Integer getQuantidadeDeConsultasRestantes() {
		return quantidadeDeConsultasRestantes;
	}

	public void setQuantidadeDeConsultasRestantes(
			Integer quantidadeDeConsultasRestantes) {
		this.quantidadeDeConsultasRestantes = quantidadeDeConsultasRestantes;
	}

	public Integer getQuantidadeDeTentativas() {
		return quantidadeDeTentativas;
	}

	public void setQuantidadeDeTentativas(Integer quantidadeDeTentativas) {
		this.quantidadeDeTentativas = quantidadeDeTentativas;
	}

	public boolean isRegistrosAlterados() {
		return registrosAlterados;
	}

	public void setRegistrosAlterados(boolean registrosAlterados) {
		this.registrosAlterados = registrosAlterados;
	}

	public List<Servico> getServicosAlterados() {
		return servicosAlterados;
	}

	public void setServicosAlterados(List<Servico> servicosAlterados) {
		this.servicosAlterados = servicosAlterados;
	}

	public synchronized void servicoRevisado(Servico servico) {
		this.servicosAlterados.remove(servico);
	}

	public Integer getConsultarNovamenteEmCasoDeErro() {
		return consultarNovamenteEmCasoDeErro;
	}

	public void setConsultarNovamenteEmCasoDeErro(
			Integer consultarNovamenteEmCasoDeErro) {
		this.consultarNovamenteEmCasoDeErro = consultarNovamenteEmCasoDeErro;
	}

	public Event<Consulta> getProcessadorConsulta() {
		return processadorConsulta;
	}

	public void setProcessadorConsulta(Event<Consulta> processadorConsulta) {
		this.processadorConsulta = processadorConsulta;
	}

	public boolean isContinuarConsultando() {
		return continuarConsultando;
	}

	public synchronized void setContinuarConsultando(
			boolean continuarConsultando) {
		this.continuarConsultando = continuarConsultando;
	}

	public void setAgendadorExecucao(AgendadorExecucaoConsulta agendadorExecucao) {
		this.agendadorExecucao = agendadorExecucao;
	}

}
