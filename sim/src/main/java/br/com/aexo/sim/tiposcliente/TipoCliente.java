package br.com.aexo.sim.tiposcliente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotBlank;
import org.jboss.solder.core.Veto;

import br.com.aexo.sim.clientes.Cliente;
import br.com.aexo.sim.core.exceptions.EntidadeRelacionadaException;

@Entity
@Veto
public class TipoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message="Informe a descrição")
	private String descricao;
	
	@OneToMany
	@JoinColumn(name="tipo_id")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<Cliente> clientes = new ArrayList<Cliente>();

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
		TipoCliente other = (TipoCliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@PreRemove
	@SuppressWarnings("unused")
	private void checaPossibilidadeDeExclusao(){
		if (getClientes().size()!=0)
			throw new EntidadeRelacionadaException("Há clientes vinculados ao tipo não sendo possivel remover");
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
	
}
