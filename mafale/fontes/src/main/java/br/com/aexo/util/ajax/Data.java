package br.com.aexo.util.ajax;

import java.util.List;

import br.com.aexo.util.vraptor.xstream.XstreamProcessAnnotation;

public class Data<E> {
	
	/**
	 * criado apenas para fazer a recursão das anotações da listagem
	 */
	
	@XstreamProcessAnnotation
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
