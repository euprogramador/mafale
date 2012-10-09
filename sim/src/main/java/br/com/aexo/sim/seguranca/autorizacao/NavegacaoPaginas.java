package br.com.aexo.sim.seguranca.autorizacao;

import org.jboss.seam.faces.rewrite.FacesRedirect;
import org.jboss.seam.faces.security.AccessDeniedView;
import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;
import org.jboss.seam.security.annotations.LoggedIn;

import br.com.aexo.sim.seguranca.autenticacao.Autenticado;

/**
 * Representa o controle de acesso as páginas
 * 
 * @author carlosr
 * 
 */
@ViewConfig
public interface NavegacaoPaginas {

	static enum Pages {
		
		@ViewPattern("/admin/*")
		@FacesRedirect
		@AccessDeniedView("/login.xhtml?faces-redirect=true")
		@LoginView("/login.xhtml?faces-redirect=true")
		@Autenticado
		@LoggedIn
		SECURED;
	}
}
