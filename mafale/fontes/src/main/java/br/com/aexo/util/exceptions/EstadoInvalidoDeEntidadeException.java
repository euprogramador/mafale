package br.com.aexo.util.exceptions;

public class EstadoInvalidoDeEntidadeException extends DominioException {
	private static final long serialVersionUID = 1L;

	public EstadoInvalidoDeEntidadeException() {
		super("Alerta o registro foi modificado por outro usuário, por favor reveja antes de enviar suas mudanças.");
	}
}
