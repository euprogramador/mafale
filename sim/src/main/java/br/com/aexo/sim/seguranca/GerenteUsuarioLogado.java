package br.com.aexo.sim.seguranca;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.aexo.sim.seguranca.autenticacao.Autenticado;

/**
 * 
 * Gerente de usuário 
 * 
 * @author carlosr
 *
 */
@Stateful
@SessionScoped
public class GerenteUsuarioLogado implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private UsuarioLogado usuarioLogado;
    
    @Inject
    private HttpServletRequest httpServletRequest;

    @Produces
    @RequestScoped
    @Named
    public UsuarioLogado usuarioLogado() {
        return usuarioLogado;
    }

    public void onLogin(@Observes @Autenticado Usuario usuario, ExternalContext externalContext) {
        usuarioLogado = new UsuarioLogado(usuario, httpServletRequest.getRemoteAddr());
        httpServletRequest.getSession().setMaxInactiveInterval(3600);
    }

}
