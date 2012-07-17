package br.com.aexo.mafale.administrativo.cliente;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.validator.constraints.NotBlank;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import br.com.aexo.util.dominio.Entidade;
import br.com.aexo.util.exceptions.DominioException;

@Entity
public class PorteCliente extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Informe a descrição")
	private String descricao;

	@Transient
	private transient final Session session;

	@XStreamOmitField
	@OneToMany
	@JoinColumn(name = "porte_id")
	private List<Cliente> clientes = new ArrayList<Cliente>();

	public PorteCliente(Session session) {
		this.session = session;
	}

	public void salvar() {
		session.saveOrUpdate(this);
	}

	public void remover() {
		if (!clientes.isEmpty())
			throw new DominioException("Há Clientes vinculados ao porte, não é possivel remover");

		session.delete(this);
	}

	public void preencherCom(Entidade entidade) {
		PorteCliente me  = (PorteCliente) entidade;
		descricao = me.getDescricao();
	}

	public PorteCliente carregar() {
		return (PorteCliente) session.get(PorteCliente.class, id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}