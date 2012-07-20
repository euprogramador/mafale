package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import java.util.List;

import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.http.route.RoutesConfiguration;
import br.com.caelum.vraptor.http.route.Rules;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.HttpMethod;

@Component
@ApplicationScoped
public class CrudRoutes implements RoutesConfiguration {

	private final CrudService crudService;

	public CrudRoutes(CrudService crudService) {
		this.crudService = crudService;
	}

	public void config(Router router) {
		new Rules(router) {

			@Override
			public void routes() {

				List<Crud> cruds = crudService.getCruds();

				for (Crud crud : cruds) {
					Class<? extends CrudResource<?>> resource = crudService.getCrudFor(crud.getEntidade());

					routeFor(crud.getUriBase() + "/{entidade.id}").with(HttpMethod.DELETE).is(resource).remover(null);

					routeFor(crud.getUriBase()).with(HttpMethod.GET).is(resource).listar(crud.getOrdenacaoDefault(), null, null, null);

					routeFor(crud.getUriBase() + "/{id}").with(HttpMethod.GET).is(resource).recuperar(null);

					routeFor(crud.getUriBase()).with(HttpMethod.POST).is(resource).salvar(null);

					routeFor(crud.getUriBase() + "/{entidade.id}").with(HttpMethod.POST).is(resource).salvar(null);
				}
			}
		};
	}
}
