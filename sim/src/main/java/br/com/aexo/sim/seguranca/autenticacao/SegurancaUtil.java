package br.com.aexo.sim.seguranca.autenticacao;

import java.io.Serializable;
import java.security.GeneralSecurityException;

import javax.inject.Inject;

import org.jboss.seam.security.management.PasswordHash;

import br.com.aexo.sim.seguranca.Usuario;

import com.google.common.primitives.Longs;

/**
 * gera um hash de segurança para um usuário logado
 * 
 * @author carlosr
 * 
 */
public class SegurancaUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PasswordHash passwordHash;

	public String hash(Usuario u, String password) throws GeneralSecurityException {
		return passwordHash.createPasswordKey(password.toCharArray(), Longs.toByteArray(u.hashCode()), 7);
	}

}
