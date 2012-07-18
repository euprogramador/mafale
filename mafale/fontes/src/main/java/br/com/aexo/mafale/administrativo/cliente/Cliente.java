package br.com.aexo.mafale.administrativo.cliente;

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

import org.hibernate.Session;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

import br.com.aexo.mafale.administrativo.servicos.Servico;
import br.com.aexo.util.dominio.Entidade;
import br.com.aexo.util.exceptions.DominioException;
import br.com.caelum.stella.bean.validation.CNPJ;

@Entity
public class Cliente extends Entidade {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Informe a razão Social")
	private String razaoSocial;
	@NotBlank(message = "Informe o CNPJ")
	@CNPJ(message = "CNPJ inválido")
	private String cnpj;
	@NotBlank(message = "Informe o endereço")
	private String endereco;
	@NotBlank(message = "Informe o bairro")
	private String bairro;
	@NotBlank(message = "Informe a cidade")
	private String cidade;
	@NotBlank(message = "Informe o estado")
	private String estado;
	@NotBlank(message = "Informe o cep")
	private String cep;
	@NotBlank(message = "Informe o contato")
	private String contato;
	@NotBlank(message = "Informe o telefone")
	private String telefone1;
	private String telefone2;

	@NotBlank(message = "Informe o email")
	@Email(message = "Email informado está inválido")
	private String email;

	@ManyToOne
	@JoinColumn(name = "porte_id")
	private PorteCliente porte;

	@ManyToOne
	@JoinColumn(name = "tipo_id")
	private TipoCliente tipo;

	@XStreamOmitField
	@OneToMany
	@JoinColumn(name = "cliente_id")
	private List<Servico> servicos = new ArrayList<Servico>();

	@Transient
	private transient final Session session;

	public Cliente(Session session) {
		this.session = session;
	}

	@Override
	public void remover() {
		if (!servicos.isEmpty())
			throw new DominioException("Há serviços vinculados a este client, não sendo possível remover");
		
		session.delete(this);
	}

	@Override
	public void salvar() {
		session.saveOrUpdate(this);
	}

	@Override
	public Entidade carregar() {
		return (Entidade) session.get(Cliente.class, id);
	}

	public void preencherCom(Entidade entidade) {
		Cliente me = (Cliente) entidade;
		razaoSocial = me.getRazaoSocial();
		cnpj = me.getCnpj();
		endereco = me.getEndereco();
		bairro = me.getBairro();
		cidade = me.getCidade();
		estado = me.getEstado();
		cep = me.getCep();
		contato = me.getContato();
		telefone1 = me.getTelefone1();
		telefone2 = me.getTelefone2();
		email = me.getEmail();
		porte = me.getPorte() != null && me.getPorte().getId() != null ? me.getPorte() : null;
		tipo = me.getTipo() != null && me.getTipo().getId() != null ? me.getTipo() : null;
	}

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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}