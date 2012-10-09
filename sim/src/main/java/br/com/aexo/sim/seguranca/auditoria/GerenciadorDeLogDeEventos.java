package br.com.aexo.sim.seguranca.auditoria;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.aexo.sim.core.util.StringTools;
import br.com.aexo.sim.seguranca.Usuario;
import br.com.aexo.sim.seguranca.UsuarioLogado;


@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class GerenciadorDeLogDeEventos {

	@Inject
    private Instance<EntityManager> entityManagerInstance;

    @Inject
    private Instance<UsuarioLogado> usuarioLogadoInstance;

    public void log(@Observes LogEvento logEvento) {
        if (!StringTools.isNullOrBlank(logEvento.getMensagem())) {
            try {
                EntityManager em = entityManagerInstance.get();
                UsuarioLogado usuarioLogado = usuarioLogadoInstance.get();
                logEvento.setUsuario(em.getReference(Usuario.class, usuarioLogado.getUsuario().getId()));
                logEvento.setIp(usuarioLogado.getIp());
                em.persist(logEvento);
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
    }
}
