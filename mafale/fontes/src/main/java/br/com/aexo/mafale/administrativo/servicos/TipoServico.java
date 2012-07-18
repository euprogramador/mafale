package br.com.aexo.mafale.administrativo.servicos;

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
public class TipoServico extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Informe a descricao")
	private String descricao;

	@XStreamOmitField
	@OneToMany
	@JoinColumn(name = "tiposervico_id")
	private List<Servico> servicos = new ArrayList<Servico>();

	@Transient
	private transient final Session session;

	public TipoServico(Session session) {
		this.session = session;
	}

	@Override
	public void remover() {
		if (!servicos.isEmpty())
			throw new DominioException("Há serviços vinculados, não sendo possivel remover");

		session.delete(this);
	}

	@Override
	public void salvar() {
		session.saveOrUpdate(this);
	}

	@Override
	public Entidade carregar() {
		return (Entidade) session.get(TipoServico.class, id);
	}

	@Override
	public void preencherCom(Entidade entidade) {
		TipoServico me = (TipoServico) entidade;
		descricao = me.getDescricao();
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
