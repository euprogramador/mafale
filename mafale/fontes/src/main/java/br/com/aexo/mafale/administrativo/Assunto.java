package br.com.aexo.mafale.administrativo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.aexo.Entidade;

@Entity
public class Assunto extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String descricao;
	private Integer renovarEmDias;

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

	public Integer getRenovarEmDias() {
		return renovarEmDias;
	}

	public void setRenovarEmDias(Integer renovarEmDias) {
		this.renovarEmDias = renovarEmDias;
	}

}
