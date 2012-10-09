package br.com.aexo.sim.seguranca;

import org.jboss.solder.core.Veto;
import org.joda.time.LocalDateTime;

/**
 * classe que efetivamente guardará o usuário logado na sessão
 * 
 * @author carlosr
 * 
 */
@Veto
public class UsuarioLogado {

	private Usuario usuario;
	private String ip;
	private LocalDateTime ultimoLogin;

	public UsuarioLogado(){}
	
	public UsuarioLogado(Usuario usuario, String ip) {
		super();
		this.usuario = usuario;
		this.ip = ip;
		this.ultimoLogin = new LocalDateTime();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public LocalDateTime getUltimoLogin() {
		return ultimoLogin;
	}

	public void setUltimoLogin(LocalDateTime ultimoLogin) {
		this.ultimoLogin = ultimoLogin;
	}

	public void checarSeLogado() {
		System.out.println("checa se Logado");
	}

}
