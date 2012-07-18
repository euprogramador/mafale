package br.com.aexo.mafale.administrativo.servicos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import br.com.aexo.util.dominio.Entidade;

@Entity
public class Historico extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Informe a petição")
	@ManyToOne
	@JoinColumn(name = "peticao_id")
	private Peticao peticao;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
	private LocalDate consultadoEm = new LocalDate();

	private String situacao;

	private String encontraSeNa;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
	private LocalDate encontraSeDesde;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
	private LocalDate dataPublicacao;

	private String resolucao;

	@Transient
	private transient final Session session;

	public Historico(Session session) {
		this.session = session;
	}

	@Override
	public void remover() {
		session.delete(this);
	}

	@Override
	public void salvar() {
		session.saveOrUpdate(this);
	}

	@Override
	public Entidade carregar() {
		return (Entidade) session.get(Historico.class, id);
	}

	@Override
	public void preencherCom(Entidade entidade) {
		Historico me = (Historico) entidade;
		peticao = me.getPeticao();
		// auto preenchido
		// consultadoEm = me.getConsultadoEm();
		situacao = me.getSituacao();
		encontraSeNa = me.getEncontraSeNa();
		encontraSeDesde = me.getEncontraSeDesde();
		dataPublicacao = me.getDataPublicacao();
		resolucao = me.getResolucao();
	}

	public Peticao getPeticao() {
		return peticao;
	}

	public void setPeticao(Peticao peticao) {
		this.peticao = peticao;
	}

	public LocalDate getConsultadoEm() {
		return consultadoEm;
	}

	public void setConsultadoEm(LocalDate consultadoEm) {
		this.consultadoEm = consultadoEm;
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

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

}
