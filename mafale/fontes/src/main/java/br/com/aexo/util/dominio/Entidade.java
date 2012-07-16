package br.com.aexo.util.dominio;

import java.io.Serializable;

/**
 * representa uma entidade do sistema
 * 
 * @author carlosr
 * 
 */
public abstract class Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Identificador da entidade
	 * 
	 * @return
	 */
	public abstract Long getId();

	
	public abstract void remover();
	
	public abstract void salvar();
	
	public abstract Entidade carregar();
	
	/**
	 * hashcode pelo id
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	/**
	 * equals pelo id
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Entidade other = (Entidade) obj;

		if (getId() == null) {
			if (other.getId() != null) {
				return false;
			}

		} else if (!getId().equals(other.getId())) {
			return false;
		}

		return true;
	}

}
