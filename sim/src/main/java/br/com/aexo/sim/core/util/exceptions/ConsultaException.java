package br.com.aexo.sim.core.util.exceptions;

public class ConsultaException extends RuntimeException {

	public ConsultaException(String erro) {
		super(erro);
	}

	public ConsultaException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConsultaException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 1L;

}
