package br.com.aexo.util.hibernate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.isEmpty;
import static java.util.Arrays.asList;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Session;

import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Lazy;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.interceptor.ParametersInstantiatorInterceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.FlashScope;
import br.com.caelum.vraptor.view.Results;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * Interceptor que carrega a entidade do banco
 * 
 * @author Lucas Cavalcanti
 * @author Cecilia Fernandes
 * @since 3.4.0
 * 
 */
@Intercepts(before = ParametersInstantiatorInterceptor.class)
@Lazy
public class ParameterLoaderInterceptor implements Interceptor {

	private final Session session;
	private final HttpServletRequest request;
	private final ParameterNameProvider provider;
	private final Result result;
	private final Converters converters;
	private final Localization localization;
	private final FlashScope flash;

	/**
	 * 
	 * @param session
	 * @param request
	 * @param provider
	 * @param result
	 * @param converters
	 * @param localization
	 * @param flash
	 */
	public ParameterLoaderInterceptor(Session session, HttpServletRequest request, ParameterNameProvider provider, Result result, Converters converters, Localization localization, FlashScope flash) {
		this.session = session;
		this.request = request;
		this.provider = provider;
		this.result = result;
		this.converters = converters;
		this.localization = localization;
		this.flash = flash;
	}

	/**
	 * aceito quando o metódo possui um parametro anotado com load
	 */
	public boolean accepts(ResourceMethod method) {
		return any(asList(method.getMethod().getParameterAnnotations()), hasLoadAnnotation());
	}

	/**
	 * intercepta fazendo a busca no banco e injetando no controller
	 */
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		Annotation[][] annotations = method.getMethod().getParameterAnnotations();

		String[] names = provider.parameterNamesFor(method.getMethod());

		Class<?>[] types = method.getMethod().getParameterTypes();

		Object[] args = flash.consumeParameters(method);

		for (int i = 0; i < names.length; i++) {
			Iterable<Load> loads = Iterables.filter(asList(annotations[i]), Load.class);
			if (!isEmpty(loads)) {
				Object loaded = load(names[i], types[i]);

				if (loaded == null) {
					
					result.use(Results.page()).forwardTo("/WEB-INF/template/notfound.jsp");
					
					return;
				}

				if (args != null)
					args[i] = loaded;
				else
					request.setAttribute(names[i], loaded);
			}
		}
		flash.includeParameters(method, args);

		stack.next(method, resourceInstance);
	}

	private Object load(String name, @SuppressWarnings("rawtypes") Class type) {
		String parameter = request.getParameter(name + ".id");
		if (parameter == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		Field field = new Mirror().on(type).reflect().field("id");
		checkArgument(field != null, "Entity " + type.getSimpleName() + " must have an id property for @Load.");

		Class<?> idType = field.getType();
		Converter<?> converter = converters.to(idType);
		checkArgument(converter != null, "Entity " + type.getSimpleName() + " id type " + idType + " must have a converter");

		@SuppressWarnings("unchecked")
		Serializable id = (Serializable) converter.convert(parameter, type, localization.getBundle());
		return session.get(type, id);
	}

	private Predicate<Annotation[]> hasLoadAnnotation() {
		return new Predicate<Annotation[]>() {
			public boolean apply(Annotation[] param) {
				return any(asList(param), instanceOf(Load.class));
			}
		};
	}

}
