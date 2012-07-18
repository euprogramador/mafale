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
import org.hibernate.validator.constraints.NotBlank;

import br.com.aexo.mafale.administrativo.cliente.Cliente;
import br.com.aexo.util.dominio.Entidade;
import br.com.aexo.util.exceptions.DominioException;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
public class Servico extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name="cliente_id")
	private Cliente cliente;

	@NotBlank(message = "Informe o protocolo")
	private String protocolo;
	
	@ManyToOne
	@JoinColumn(name="tiposervico_id")
	private TipoServico tipoServico;
	
	private String nomeProduto;

	@XStreamOmitField
	@OneToMany
	@JoinColumn(name="servico_id")
	private List<Peticao> peticoes = new ArrayList<Peticao>();

	@Transient
	private transient final Session session;

	public Servico(Session session) {
		this.session = session;
	}

	@Override
	public void remover() {
		if (!peticoes.isEmpty())
			throw new DominioException("Há Petições vinculadas a este serviço, não sendo possível remover");
		
		session.delete(this);
	}

	@Override
	public void salvar() {
		session.saveOrUpdate(this);
	}

	@Override
	public Entidade carregar() {
		return (Entidade) session.get(Servico.class, id);
	}

	@Override
	public void preencherCom(Entidade entidade) {
		Servico me = (Servico) entidade;
		cliente = me.getCliente()!=null && me.getCliente().getId()!=null ? me.getCliente() : null;
		protocolo = me.getProtocolo();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public TipoServico getTipoServico() {
		return tipoServico;
	}

	public void setTipoServico(TipoServico tipoServico) {
		this.tipoServico = tipoServico;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

}
