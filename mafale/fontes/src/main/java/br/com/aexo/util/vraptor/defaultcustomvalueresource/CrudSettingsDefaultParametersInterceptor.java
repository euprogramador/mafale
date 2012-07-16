package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts(after=ParametersInstantiatorInterceptor.class, before={})
public class CrudSettingsDefaultParametersInterceptor implements Interceptor {

	private final DefaultParametersStore store;
	private final RequestInfo requestInfo;
	private final DefaultRouter router;
	private final MethodInfo info;

	public CrudSettingsDefaultParametersInterceptor(DefaultParametersStore store, DefaultRouter router, RequestInfo requestInfo,MethodInfo info) {
		this.store = store;
		this.router = router;
		this.requestInfo = requestInfo;
		this.info = info;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		if (!CrudResource.class.isInstance(resourceInstance)) {
			stack.next(method, resourceInstance);
			return;
		}

		MutableRequest request = requestInfo.getRequest();
		String resourceName = requestInfo.getRequestedUri();

		HttpMethod httpMethod = HttpMethod.of(request);
		Route route = router.parseRoute(resourceName, httpMethod, request);
		
		Object[] defaultParameters = store.get(route);

		Object[] parameters = info.getParameters();
		
		
		for (int x=0;x<defaultParameters.length;x++){
			if (defaultParameters[x]!=null)
				parameters[x] = defaultParameters[x];
		}
		
		info.setParameters(parameters);
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

}
