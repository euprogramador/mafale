package br.com.aexo.sim.core.navegacao;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.security.Identity;

@RequestScoped
@Named
public class UsuarioJaLogado {

	@Inject
	private Identity identity;

	public String verificaSeUsuarioJaLogado() {

		if (identity.isLoggedIn()) {
			return "/admin/home.xhtml?faces-redirect=true";
		} else {
			return null;
		}

	}

}
