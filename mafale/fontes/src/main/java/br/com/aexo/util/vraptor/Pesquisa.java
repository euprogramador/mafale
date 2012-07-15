package br.com.aexo.util.vraptor;

import java.util.ArrayList;
import java.util.List;

public class Pesquisa {

	private String termo;
	private List<String> campos = new ArrayList<String>();

	private Integer inicio;
	private Integer numRegistros;

	private Long totalRegistros;

	public String getTermo() {
		return termo;
	}

	public void setTermo(String termo) {
		this.termo = termo;
	}

	public List<String> getCampos() {
		return campos;
	}

	public void setCampos(List<String> campos) {
		this.campos = campos;
	}

	public Integer getInicio() {
		return inicio;
	}

	public void setInicio(Integer inicio) {
		this.inicio = inicio;
	}

	public Integer getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(Integer numRegistros) {
		this.numRegistros = numRegistros;
	}

	public Long getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(Long totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

}
