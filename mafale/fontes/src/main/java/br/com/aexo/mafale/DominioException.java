package br.com.aexo.mafale;

public class DominioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DominioException(String message) {
		super(message);
	}

	public DominioException(Throwable cause) {
		super(cause);
	}

}
