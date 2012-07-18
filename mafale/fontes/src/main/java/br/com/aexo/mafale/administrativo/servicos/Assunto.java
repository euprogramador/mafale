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

import br.com.aexo.util.dominio.Entidade;
import br.com.aexo.util.exceptions.DominioException;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
public class Assunto extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Informe a descrição")
	private String descricao;

	private Boolean renovavel;

	private Integer renovavelEmAnos;

	@XStreamOmitField
	@OneToMany
	@JoinColumn(name = "assunto_id")
	private List<Peticao> peticoes = new ArrayList<Peticao>();

	@Transient
	private transient final Session session;

	public Assunto(Session session) {
		this.session = session;
	}
	
	@Override
	public void remover() {
		if (!peticoes.isEmpty())
			throw new DominioException("Há Petições vinculados a este assunto, não sendo possível excluir");
		
		session.delete(this);
	}

	@Override
	public void salvar() {
		session.saveOrUpdate(this);
	}

	@Override
	public Entidade carregar() {
		return (Entidade) session.get(Assunto.class, id);
	}

	@Override
	public void preencherCom(Entidade entidade) {
		Assunto me = (Assunto) entidade;
		descricao = me.getDescricao();
		renovavel = me.getRenovavel();
		renovavelEmAnos = me.getRenovavelEmAnos();
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

	public Boolean getRenovavel() {
		return renovavel;
	}

	public void setRenovavel(Boolean renovavel) {
		this.renovavel = renovavel;
	}

	public Integer getRenovavelEmAnos() {
		return renovavelEmAnos;
	}

	public void setRenovavelEmAnos(Integer renovavelEmAnos) {
		this.renovavelEmAnos = renovavelEmAnos;
	}

}
