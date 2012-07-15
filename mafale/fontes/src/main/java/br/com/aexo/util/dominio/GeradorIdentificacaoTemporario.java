package br.com.aexo.util.dominio;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * classe responsavel por gerar um identificador temporario para suporte em
 * listas e mapas
 * 
 * @author carlosr
 * 
 */
public class GeradorIdentificacaoTemporario {

	private static final int BASE = 999999999;

	private Long id;

	private GeradorIdentificacaoTemporario(Long id) {
		this.id = id;
	}

	private GeradorIdentificacaoTemporario(Entidade max) {
		if (max == null) {
			id = new Long(0);
		} else {
			id = max.getId();
		}
	}

	public static GeradorIdentificacaoTemporario para(List<? extends Entidade> entidades) {
		if (entidades.size() == 0) {
			return new GeradorIdentificacaoTemporario(new Long(0));
		}

		return new GeradorIdentificacaoTemporario(Collections.max(entidades, new Comparator<Entidade>() {

			@Override
			public int compare(Entidade o1, Entidade o2) {
				Long v1 = o1.getId() != null ? o1.getId() : 0L;
				Long v2 = o2.getId() != null ? o2.getId() : 0L;
				return v1.compareTo(v2);
			}
		}));
	}

	public Long obterProximoId() {
		id++;
		return new Long(BASE) + id;
	}

}