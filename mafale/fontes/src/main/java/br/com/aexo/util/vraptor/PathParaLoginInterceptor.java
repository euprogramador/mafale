package br.com.aexo.util.vraptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

/**
 * classe responsavel por montar o path para o login da aplicação
 * 
 * @author carlosr
 * 
 */
@Intercepts
public class PathParaLoginInterceptor implements Interceptor {

	private Result result;
	private ServletContext servletContext;
	private HttpServletRequest request;

	public PathParaLoginInterceptor(Result result, ServletContext servletContext, HttpServletRequest request) {
		this.result = result;
		this.servletContext = servletContext;
		this.request = request;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {

		String pathParaLogin = servletContext.getInitParameter("seguranca.urlParaLogin");
		pathParaLogin = pathParaLogin.replace("{serverName}", request.getServerName());
		pathParaLogin = pathParaLogin.replace("{serverPort}", Integer.toString(request.getServerPort()));
		result.include("pathParaLogin", pathParaLogin);

		String pathParaLogout = servletContext.getInitParameter("seguranca.urlParaLogout");
		if (pathParaLogout != null) {
			pathParaLogout = pathParaLogout.replace("{serverName}", request.getServerName());
			pathParaLogout = pathParaLogout.replace("{serverPort}", Integer.toString(request.getServerPort()));
			result.include("pathParaLogout", pathParaLogout);
		}

		String pathParaAlterarSenha = servletContext.getInitParameter("seguranca.urlParaAlterarSenha");
		if (pathParaAlterarSenha != null) {
			pathParaAlterarSenha = pathParaAlterarSenha.replace("{serverName}", request.getServerName());
			pathParaAlterarSenha = pathParaAlterarSenha.replace("{serverPort}", Integer.toString(request.getServerPort()));
			result.include("pathParaAlterarSenha", pathParaAlterarSenha);
		}

		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

}
