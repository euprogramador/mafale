package br.com.aexo.mafale.administrativo.cliente;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.validator.constraints.NotBlank;

import br.com.aexo.util.dominio.Entidade;

@Entity
public class TipoCliente extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message="Informe a descrição")
	private String descricao;

	@Transient
	private transient final Session session;

	public TipoCliente(Session session){
		this.session = session;
	}
	
	public void salvar(){
		session.saveOrUpdate(this);
	}
	
	public void remover(){
		session.delete(this);
	}
	
	public TipoCliente carregar(){
		return (TipoCliente) session.get(TipoCliente.class,id);
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
