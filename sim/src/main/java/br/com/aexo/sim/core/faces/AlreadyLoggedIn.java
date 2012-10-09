package br.com.aexo.sim.core.faces;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.security.Identity;

@RequestScoped
@Named
public class AlreadyLoggedIn {

    @Inject
    private Identity identity;

    public String alreadyLoggedIn() {

        if (identity.isLoggedIn()) {
            return "/admin/home.xhtml";
        }
        else
        {
            return null;
        }

    }

}
