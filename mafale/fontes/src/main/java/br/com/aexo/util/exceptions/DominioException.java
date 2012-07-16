package br.com.aexo.util.exceptions;


/**
 * representa os erros de dominio em que o message pode ser apresentado para o
 * usuário final
 * 
 * @author carlosr
 * 
 */
public class DominioException extends RuntimeException  {

	private static final long serialVersionUID = 1L;

	/**
	 * mensagem amigavel com erro
	 * 
	 * @param message
	 * @param cause
	 */
	public DominioException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * mensagem amigavel
	 * 
	 * @param message
	 */
	public DominioException(String message) {
		super(message);
	}

}
