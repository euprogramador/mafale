package br.com.aexo.sim.clientes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.jboss.solder.core.Veto;

import br.com.aexo.sim.core.exceptions.EntidadeRelacionadaException;
import br.com.aexo.sim.core.validator.CNPJ;
import br.com.aexo.sim.servicos.Servico;
import br.com.aexo.sim.tiposcliente.TipoCliente;

@Entity
@Veto
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Informe a razão social")
	private String razaoSocial;
	@NotBlank(message = "Informe o CNPJ")
	@CNPJ(message="Informe um cnpj válido")
	private String cnpj;
	private String endereco;
	private String bairro;
	private String cidade;
	private String estado;
	private String cep;
	private String contato;
	private String telefone1;
	private String telefone2;
	
	@Lob
	@org.hibernate.annotations.Type(type="org.hibernate.type.TextType")
	private String observacao;

	@Email(message = "Informe um email válido")
	private String email;

	@ManyToOne
	@JoinColumn(name="tipo_id")
	@NotNull(message = "Informe o tipo")
	private TipoCliente tipo;

	@OneToMany
	@JoinColumn(name="cliente_id")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<Servico> servicos = new ArrayList<Servico>();
	
	@PreRemove
	@SuppressWarnings("unused")
	private void checaPossibilidadeDeExclusao(){
		if (servicos.size()!=0)
			throw new EntidadeRelacionadaException("Há serviços vinculados a este cliente não sendo possível remover");
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

	public TipoCliente getTipo() {
		return tipo;
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo;
	}


	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}
