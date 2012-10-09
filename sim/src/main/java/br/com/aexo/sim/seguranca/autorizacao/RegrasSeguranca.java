package br.com.aexo.sim.seguranca.autorizacao;

import org.jboss.seam.security.Identity;
import org.jboss.seam.security.annotations.Secures;

import br.com.aexo.sim.seguranca.autenticacao.Autenticado;

public class RegrasSeguranca {
    
    @Autenticado
    @Secures
    public boolean loggedInChecker(Identity identity) {
        return identity.isLoggedIn();
    }
}
