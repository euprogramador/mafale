package br.com.aexo.sim.core.exceptions;

public class RegistroInexistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String pagina;

	public RegistroInexistenteException(String mensagem, String pagina) {
		super(mensagem);
		this.pagina = pagina;
	}

	public String getPagina() {
		return pagina;
	}


}
