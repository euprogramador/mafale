package br.com.aexo.util.exceptions;

/***
 * erros relacionados a infrastrutura
 * 
 * @author carlosr
 * 
 */
public class InfraestruturaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * erros relacionados a infraestrutura com uma mensagem e a causa principal
	 * 
	 * @param message
	 * @param cause
	 */
	public InfraestruturaException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * erros relacionados a infraestrutura com uma mensagem
	 * 
	 * @param message
	 */
	public InfraestruturaException(String message) {
		super(message);
	}

}
