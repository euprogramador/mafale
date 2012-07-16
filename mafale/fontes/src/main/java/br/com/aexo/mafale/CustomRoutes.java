package br.com.aexo.mafale;

import org.hibernate.criterion.Order;

import br.com.aexo.mafale.administrativo.cliente.TipoCliente;
import br.com.aexo.util.vraptor.defaultcustomvalueresource.CrudResource;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.http.route.RoutesConfiguration;
import br.com.caelum.vraptor.http.route.Rules;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.HttpMethod;

@Component
@ApplicationScoped
public class CustomRoutes implements RoutesConfiguration {

    public void config(Router router) {
        new Rules(router) {
            public void routes() {
                routeFor("/data/t").with(HttpMethod.GET).is(CrudResource.class).listar(TipoCliente.class.getName(), Order.asc("descricao"), null, null);
            }
        };
    }

}