package br.com.aexo.util.jstl;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class CustomComponentsInterceptor implements Interceptor{

	private final Util util;
	private final Result result;

	public CustomComponentsInterceptor(Result result,Util util){
		this.result = result;
		this.util = util;
	}
	
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		result.include("util",util);
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

}
