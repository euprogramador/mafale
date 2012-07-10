package br.com.aexo.mafale.anvisa;

import org.joda.time.LocalDate;

public class PeticaoNaAnvisa {

	private String expediente;
	private LocalDate dataEntrada;
	private String protocolo;
	private String assunto;
	private String situacao;
	private String encontraSeNa;
	private LocalDate encontraSeDesde;
	private LocalDate dataPublicacao;
	private String resolucao;

	public String getExpediente() {
		return expediente;
	}

	public void setExpediente(String expediente) {
		this.expediente = expediente;
	}

	public LocalDate getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(LocalDate dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getEncontraSeNa() {
		return encontraSeNa;
	}

	public void setEncontraSeNa(String encontraSeNa) {
		this.encontraSeNa = encontraSeNa;
	}

	public LocalDate getEncontraSeDesde() {
		return encontraSeDesde;
	}

	public void setEncontraSeDesde(LocalDate encontraSeDesde) {
		this.encontraSeDesde = encontraSeDesde;
	}

	public LocalDate getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(LocalDate dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getResolucao() {
		return resolucao;
	}

	public void setResolucao(String resolucao) {
		this.resolucao = resolucao;
	}

}
