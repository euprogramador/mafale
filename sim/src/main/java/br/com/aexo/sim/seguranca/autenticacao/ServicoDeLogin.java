package br.com.aexo.sim.seguranca.autenticacao;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.seam.security.Identity;

@Named
@RequestScoped
public class ServicoDeLogin {

	@Inject
	private Identity identity;
	
	public String login(){
		String result = identity.login();
		if (result.equals(Identity.RESPONSE_LOGIN_SUCCESS)){
			return "/admin/home.xhtml?faces-redirect=true";
		}
		return "";
	}
	
	
	public String logout(){
		FacesContext context = FacesContext.getCurrentInstance();
	    ExternalContext externalContext = context.getExternalContext();

	    // invalidate session
	    Object session = externalContext.getSession(false);
	    HttpSession httpSession = (HttpSession) session;
	    httpSession.invalidate();

	    return "/login.xhtml?faces-redirect=true";
	}
	
}
