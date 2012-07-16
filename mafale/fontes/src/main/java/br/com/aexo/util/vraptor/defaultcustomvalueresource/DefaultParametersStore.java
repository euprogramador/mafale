package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class DefaultParametersStore {

	Map<Route, Object[]> store = new HashMap<Route, Object[]>();

	public void storeDefaultParameters(Route route, Object[] args) {
		store.put(route, args);
	}

	public Object[] get(Route route) {
		return store.get(route);
	}

}
