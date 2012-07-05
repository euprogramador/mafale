package br.com.aexo.mafale.anvisa;

import java.util.Date;

public class SituacaoNaAnvisa {

	private String expediente;
	private Date dataEntrada;
	private String protocolo;
	private String assunto;
	private String situacao;
	private String encontraSeNa;
	private Date encontraSeDesde;
	private Date dataPublicacao;
	private String resolucao;

	public String getExpediente() {
		return expediente;
	}

	public void setExpediente(String expediente) {
		this.expediente = expediente;
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

	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public Date getEncontraSeDesde() {
		return encontraSeDesde;
	}

	public void setEncontraSeDesde(Date encontraSeDesde) {
		this.encontraSeDesde = encontraSeDesde;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public String getResolucao() {
		return resolucao;
	}

	public void setResolucao(String resolucao) {
		this.resolucao = resolucao;
	}

}
