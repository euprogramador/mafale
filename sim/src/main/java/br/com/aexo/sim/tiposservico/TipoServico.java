package br.com.aexo.sim.tiposservico;

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

import br.com.aexo.sim.core.exceptions.EntidadeRelacionadaException;
import br.com.aexo.sim.servicos.Servico;

@Entity
@Veto
public class TipoServico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Informe a descrição")
	private String descricao;

	@OneToMany
	@JoinColumn(name = "tiposervico_id")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<Servico> servicos = new ArrayList<Servico>();

	@PreRemove
	@SuppressWarnings("unused")
	private void checaPossibilidadeDeExclusao() {
		if (servicos.size() != 0)
			throw new EntidadeRelacionadaException("Há serviços vinculados a este tipo não sendo possivel remover");
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
		TipoServico other = (TipoServico) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
