package br.com.aexo.mafale.administrativo.cliente;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.aexo.util.dominio.Entidade;

@Entity
public class Cliente extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String razaoSocial;

	@ManyToOne
	@JoinColumn(name = "porte_id")
	private PorteCliente porte;

	@ManyToOne
	@JoinColumn(name = "tipo_id")
	private TipoCliente tipo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public PorteCliente getPorte() {
		return porte;
	}

	public void setPorte(PorteCliente porte) {
		this.porte = porte;
	}

	public TipoCliente getTipo() {
		return tipo;
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo;
	}

	@Override
	public void remover() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void salvar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Entidade carregar() {
		// TODO Auto-generated method stub
		return null;
	}

}