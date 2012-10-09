package br.com.aexo.sim.seguranca.autenticacao;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.jboss.solder.logging.Logger;
import org.picketlink.idm.impl.api.PasswordCredential;
import org.picketlink.idm.impl.api.model.SimpleUser;

import br.com.aexo.sim.seguranca.Usuario;
import br.com.aexo.sim.seguranca.auditoria.LogEvento;

import com.google.common.base.Strings;

@Named
@SessionScoped
public class Autenticador extends BaseAuthenticator implements Serializable, org.jboss.seam.security.Authenticator {

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;

	@Inject
	private Credentials credentials;

	@Inject
	private Messages messages;

	@Inject
	private EntityManager em;

	@Inject
	@Autenticado
	private Event<Usuario> eventoLogin;

	@Inject
	private Event<LogEvento> eventoLog;

	// TODO corrigir implementação para criptografar senha
	// @Inject
	// private SegurancaUtil segurancaUtil;

	public void authenticate() {
		if (!Strings.isNullOrEmpty(credentials.getUsername())) {
			log.info("Autenticando: \"" + credentials.getUsername() + "\"");
			try {
				Usuario usuario = em.createNamedQuery("login", Usuario.class).setParameter("login", credentials.getUsername()).getSingleResult();
				if (usuario != null) {
					PasswordCredential password = (PasswordCredential) credentials.getCredential();
					String hashedPassword = password.getValue(); // segurancaUtil.hash(usuario,
																	// password.getValue());
					if (hashedPassword.equals(usuario.getSenha())) {
						eventoLogin.fire(usuario);
						setStatus(AuthenticationStatus.SUCCESS);
						setUser(new SimpleUser(usuario.getNome()));
						eventoLog.fire(new LogEvento(usuario, "Usuário efetuou login"));
					} else {
						messages.error("Usuário ou senha inválidos");
						setStatus(AuthenticationStatus.FAILURE);
					}
				}
			} catch (NoResultException e) {
				messages.error("Login ou senha inválidos");
				setStatus(AuthenticationStatus.FAILURE);
			} catch (NonUniqueResultException e) {
				throw new RuntimeException("Impossível, há dois usuário com o mesmo login");
			}
			// } catch (GeneralSecurityException e) {
			// throw new RuntimeException("Erro ao gerar hash para senha", e);
			// }
		}
	}

}
