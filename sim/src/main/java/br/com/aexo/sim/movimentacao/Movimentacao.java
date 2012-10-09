package br.com.aexo.sim.movimentacao;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import br.com.aexo.sim.servicos.Servico;

@Entity
public class Movimentacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "servico_id")
	private Servico servico;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime data;

	private String situacaoAtual;
	private String encontraSeNaAtual;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate encontraSeDesdeAtual;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate dataPublicacaoAtual;
	private String resolucaoAtual;

	private String situacaoAnterior;
	private String encontraSeNaAnterior;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate encontraSeDesdeAnterior;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate dataPublicacaoAnterior;
	private String resolucaoAnterior;

	private Boolean conferido;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}


	public String getSituacaoAtual() {
		return situacaoAtual;
	}

	public void setSituacaoAtual(String situacaoAtual) {
		this.situacaoAtual = situacaoAtual;
	}

	public String getEncontraSeNaAtual() {
		return encontraSeNaAtual;
	}

	public void setEncontraSeNaAtual(String encontraSeNaAtual) {
		this.encontraSeNaAtual = encontraSeNaAtual;
	}

	public LocalDate getEncontraSeDesdeAtual() {
		return encontraSeDesdeAtual;
	}

	public void setEncontraSeDesdeAtual(LocalDate encontraSeDesdeAtual) {
		this.encontraSeDesdeAtual = encontraSeDesdeAtual;
	}

	public LocalDate getDataPublicacaoAtual() {
		return dataPublicacaoAtual;
	}

	public void setDataPublicacaoAtual(LocalDate dataPublicacaoAtual) {
		this.dataPublicacaoAtual = dataPublicacaoAtual;
	}

	public String getResolucaoAtual() {
		return resolucaoAtual;
	}

	public void setResolucaoAtual(String resolucaoAtual) {
		this.resolucaoAtual = resolucaoAtual;
	}

	public String getSituacaoAnterior() {
		return situacaoAnterior;
	}

	public void setSituacaoAnterior(String situacaoAnterior) {
		this.situacaoAnterior = situacaoAnterior;
	}

	public String getEncontraSeNaAnterior() {
		return encontraSeNaAnterior;
	}

	public void setEncontraSeNaAnterior(String encontraSeNaAnterior) {
		this.encontraSeNaAnterior = encontraSeNaAnterior;
	}

	public LocalDate getEncontraSeDesdeAnterior() {
		return encontraSeDesdeAnterior;
	}

	public void setEncontraSeDesdeAnterior(LocalDate encontraSeDesdeAnterior) {
		this.encontraSeDesdeAnterior = encontraSeDesdeAnterior;
	}

	public LocalDate getDataPublicacaoAnterior() {
		return dataPublicacaoAnterior;
	}

	public void setDataPublicacaoAnterior(LocalDate dataPublicacaoAnterior) {
		this.dataPublicacaoAnterior = dataPublicacaoAnterior;
	}

	public String getResolucaoAnterior() {
		return resolucaoAnterior;
	}

	public void setResolucaoAnterior(String resolucaoAnterior) {
		this.resolucaoAnterior = resolucaoAnterior;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movimentacao other = (Movimentacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getConferido() {
		return conferido;
	}

	public void setConferido(Boolean conferido) {
		this.conferido = conferido;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

}
