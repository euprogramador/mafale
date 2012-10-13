package br.com.aexo.sim.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import br.com.aexo.sim.assuntos.Assunto;
import br.com.aexo.sim.clientes.Cliente;
import br.com.aexo.sim.consultaefetuada.ConsultaEfetuada;
import br.com.aexo.sim.movimentacao.Movimentacao;
import br.com.aexo.sim.observacao.Observacao;
import br.com.aexo.sim.tiposservico.TipoServico;

@Entity
public class Servico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	@NotNull(message = "Informe o cliente")
	private Cliente cliente;
	@NotBlank(message = "Informe o numero do processo")
	private String numeroProcesso;
	@ManyToOne
	@JoinColumn(name = "assunto_id")
	private Assunto assunto;
	@ManyToOne
	@JoinColumn(name = "tiposervico_id")
	private TipoServico tipoServico;
	private String nomeProduto;
	private String expediente;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate dataEntrada;
	private String protocolo;
	private String situacao;
	private String encontraSeNa;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate encontraSeDesde;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate dataPublicacao;
	private String resolucao;

	@Enumerated(EnumType.STRING)
	private Status status;
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
	private LocalDate dataStatus;

	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime dataUltimaConsulta;

	@SuppressWarnings("unused")
	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "servico_id")
	private List<Movimentacao> movimentacoes = new ArrayList<Movimentacao>();

	@SuppressWarnings("unused")
	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "servico_id")
	private List<Observacao> observacoes = new ArrayList<Observacao>();

	@SuppressWarnings("unused")
	@OneToMany(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "servico_id")
	private List<ConsultaEfetuada> consultasEfetuadas = new ArrayList<ConsultaEfetuada>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Assunto getAssunto() {
		return assunto;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDate getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(LocalDate dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDateTime getDataUltimaConsulta() {
		return dataUltimaConsulta;
	}

	public void setDataUltimaConsulta(LocalDateTime dataUltimaConsulta) {
		this.dataUltimaConsulta = dataUltimaConsulta;
	}

}
