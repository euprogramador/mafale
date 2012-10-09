package br.com.aexo.sim.core.paginacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListaPaginada<T> implements List<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private List<T> list = new ArrayList<T>();
	private Long totalRegistros = 0L;

	private Integer firstResult = 0;

	private Integer maxResults = 0;

	private ArrayList<Integer> pages = new ArrayList<Integer>();

	private Integer totalPaginas = 0;

	public ListaPaginada() {
	}

	public ListaPaginada(List<T> list, Long totalRegistros, Integer firstResult, Integer maxResults) {
		super();
		this.list = list;
		this.setFirstResult(firstResult);
		this.setMaxResults(maxResults);
		this.setTotalRegistros(totalRegistros);
		configurarPaginacao();

	}

	public Integer getTotalPaginas() {
		return totalPaginas;
	}

	private void configurarPaginacao() {
		pages = new ArrayList<Integer>();
		Integer startPage = getFirstResult() / getMaxResults();

		Integer rc = totalRegistros.intValue();
		int mr = getMaxResults().intValue();
		int paginas = rc / mr;
		totalPaginas = rc % mr == 0 ? paginas : paginas + 1;
		startPage++;
		Integer endPage = startPage;
		Integer totalPaginasExibir = 10;
		Integer contadorPaginas = 0;
		for (contadorPaginas = 1; contadorPaginas < totalPaginasExibir && startPage > 1; contadorPaginas++) {
			startPage--;
		}
		for (contadorPaginas = 1; contadorPaginas < totalPaginasExibir && endPage < totalPaginas; contadorPaginas++) {
			endPage++;
		}

		for (int x = startPage; x <= endPage; x++) {
			getPages().add(x);
		}
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public Iterator<T> iterator() {
		return list.iterator();
	}

	public Object[] toArray() {
		return list.toArray();
	}

	@SuppressWarnings("hiding")
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	public boolean add(T e) {
		return list.add(e);
	}

	public boolean remove(Object o) {
		return list.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	public boolean addAll(Collection<? extends T> c) {
		return list.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		return list.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	public void clear() {
		list.clear();
	}

	public boolean equals(Object o) {
		return list.equals(o);
	}

	public int hashCode() {
		return list.hashCode();
	}

	public T get(int index) {
		return list.get(index);
	}

	public T set(int index, T element) {
		return list.set(index, element);
	}

	public void add(int index, T element) {
		list.add(index, element);
	}

	public T remove(int index) {
		return list.remove(index);
	}

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return list.listIterator(index);
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	public Long getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(Long totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public ArrayList<Integer> getPages() {
		return pages;
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public Integer getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

}
