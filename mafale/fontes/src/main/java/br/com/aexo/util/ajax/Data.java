package br.com.aexo.util.ajax;

import java.util.List;

public class Data<E> {

	private List<E> listagem;
	private Long contagem;
	private Integer inicio;
	private Integer numRegistros;
	public List<E> getListagem() {
		return listagem;
	}
	public void setListagem(List<E> listagem) {
		this.listagem = listagem;
	}
	public Long getContagem() {
		return contagem;
	}
	public void setContagem(Long contagem) {
		this.contagem = contagem;
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


	
}
