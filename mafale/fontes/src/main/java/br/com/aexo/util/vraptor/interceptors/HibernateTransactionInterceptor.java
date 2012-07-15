package br.com.aexo.util.vraptor.interceptors;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

/**
 * interceptor do vraptor responsavel por abrir a transação para cada request e
 * fechar assim que o resultado for enviado para o cliente
 * 
 * @author carlosr
 * 
 */
@Intercepts
public class HibernateTransactionInterceptor implements Interceptor {

	private final Session session;
	private Validator validator;
	private MutableResponse response;

	/**
	 * 
	 * @param session
	 *            sessão da qual será feita a transação
	 * @param validator
	 *            validador para verificar se não há nenhum erro de validação
	 * @param response
	 */
	public HibernateTransactionInterceptor(Session session, Validator validator, MutableResponse response) {
		this.session = session;
		this.validator = validator;
		this.response = response;
	}

	/**
	 * aceita todos os métodos de todos os controllers
	 */
	public boolean accepts(ResourceMethod method) {
		return true;
	}

	/**
	 * interceptador que abre a transação e ao final commita
	 */
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) {
		// este listener é necessário pois sem ele faz o redirecionamento antes
		// do commit criando leituras sujas (registros não commitados)
		addRedirectListener();

		// inicia a transação
		Transaction transaction = session.beginTransaction();
		try {
			stack.next(method, resourceInstance);

			// se a transação ainda estiver ativa commita
			if (session.getTransaction().isActive()) {
				session.getTransaction().commit();
			}

		} catch (Exception e) {
			// se algum erro ocorreu e a transação ainda está ativa faz rollback
			if (session.getTransaction().isActive()) {
				transaction.rollback();
			}
			throw new InterceptionException(e);
		}

	}

	/**
	 * registra o listener no response
	 */
	private void addRedirectListener() {
		// adiciona o listener no response
		response.addRedirectListener(new MutableResponse.RedirectListener() {
			// antes de fazer o redirect se não haver erros no modelo e a
			// transação ainda não foi commitada comita
			@Override
			public void beforeRedirect() {
				if (!validator.hasErrors() && session.getTransaction().isActive()) {
					session.getTransaction().commit();
				}
			}
		});
	}

}