package br.com.aexo.mafale.administrativo.servicos;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.LocalDate;

import br.com.aexo.util.dominio.Entidade;
import br.com.aexo.util.exceptions.DominioException;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
public class Peticao extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message="Informe o serviço")
	@ManyToOne
	@JoinColumn(name = "servico_id")
	private Servico servico;
	
	@ManyToOne
	@JoinColumn(name="assunto_id")
	private Assunto assunto;

	@NotBlank(message = "Informe o expediente")
	private String expediente;

	@Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
	private LocalDate dataEntrada;

	private String protocolo;
	
	
	@XStreamOmitField
	@OneToMany
	@JoinColumn(name="peticao_id")
	private List<Historico> historicos = new ArrayList<Historico>();

	@Transient
	private transient final Session session;

	public Peticao(Session session) {
		this.session = session;
	}

	@Override
	public void remover() {
		if (!historicos.isEmpty())
			throw new DominioException("Há históricos vinculados a esta petição, não sendo possível remover");
		
		session.delete(this);
	}

	@Override
	public void salvar() {
		session.saveOrUpdate(this);
	}

	@Override
	public Entidade carregar() {
		return (Entidade) session.get(Peticao.class, id);
	}

	@Override
	public void preencherCom(Entidade entidade) {
		Peticao me = (Peticao) entidade;
		servico = me.getServico()!=null && me.getServico().getId()!=null ? me.getServico() : null;
		assunto = me.getAssunto()!=null && me.getAssunto().getId()!=null ? me.getAssunto() : null;
		expediente = me.getExpediente();
		dataEntrada = me.getDataEntrada();
		protocolo = me.getProtocolo();
	}

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

	public Assunto getAssunto() {
		return assunto;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

}