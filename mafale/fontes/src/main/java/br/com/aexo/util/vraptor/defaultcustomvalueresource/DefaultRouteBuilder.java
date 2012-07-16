package br.com.aexo.util.vraptor.defaultcustomvalueresource;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.http.route.DefaultParametersControl;
import br.com.caelum.vraptor.http.route.Evaluator;
import br.com.caelum.vraptor.http.route.FixedMethodStrategy;
import br.com.caelum.vraptor.http.route.IllegalRouteException;
import br.com.caelum.vraptor.http.route.NoStrategy;
import br.com.caelum.vraptor.http.route.ParametersControl;
import br.com.caelum.vraptor.http.route.PatternBasedStrategy;
import br.com.caelum.vraptor.http.route.PatternBasedType;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.http.route.RouteBuilder;
import br.com.caelum.vraptor.http.route.TypeFinder;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.SuperMethod;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.util.StringUtils;
import br.com.caelum.vraptor.util.Stringnifier;

import com.google.common.base.Joiner;

/**
 * Should be used in one of two ways, either configure the type and invoke the
 * method or pass the method (java reflection) object.
 *
 * If not specified, the built route will have the lowest priority (higher value
 * of priority), so will be the last to be used.
 *
 * @author Guilherme Silveira
 */
@SuppressWarnings("deprecation")
public class DefaultRouteBuilder implements RouteBuilder {
	private final Set<HttpMethod> supportedMethods = EnumSet.noneOf(HttpMethod.class);

	private final Proxifier proxifier;
	private static final Logger logger = LoggerFactory.getLogger(DefaultRouteBuilder.class);

	private final String originalUri;

	private Route strategy = new NoStrategy();

	private int priority = Path.LOWEST;

	private final DefaultParameterControlBuilder builder;

	private final TypeFinder finder;

	private final Converters converters;

	private final ParameterNameProvider nameProvider;
    private final Evaluator evaluator;
    private Object[] defaultArguments;
	private final DefaultParametersStore parametersStore;

    public DefaultRouteBuilder(DefaultParametersStore parametersStore, Proxifier proxifier, TypeFinder finder, Converters converters, ParameterNameProvider nameProvider, Evaluator evaluator, String uri) {
		this.parametersStore = parametersStore;
		this.proxifier = proxifier;
		this.finder = finder;
		this.converters = converters;
		this.nameProvider = nameProvider;
        this.evaluator = evaluator;
		this.originalUri = uri;
		builder = new DefaultParameterControlBuilder();
	}

	public class DefaultParameterControlBuilder implements ParameterControlBuilder {
		private final Map<String, String> parameters = new HashMap<String, String>();
		private String name;

		private DefaultParameterControlBuilder withParameter(String name) {
			this.name = name;
			return this;
		}

		public DefaultRouteBuilder ofType(Class<?> type) {
			parameters.put(name, regexFor(type));
			return DefaultRouteBuilder.this;
		}

		@SuppressWarnings("unchecked")
		private String regexFor(Class<?> type) {
			if (Arrays.asList(Integer.class, Long.class, int.class, long.class, BigInteger.class,
					Short.class, short.class).contains(type)) {
				return "-?\\d+";
			} else if (Arrays.asList(char.class, Character.class).contains(type)){
				return ".";
			} else if (Arrays.asList(Double.class, BigDecimal.class, double.class, Float.class, float.class).contains(
					type)) {
				return "-?\\d*\\.?\\d+";
			} else if (Arrays.asList(Boolean.class, boolean.class).contains(type)) {
				return "true|false";
			} else if (Enum.class.isAssignableFrom(type)) {
				return Joiner.on("|").join(type.getEnumConstants());
			}
			return "[^/]+";
		}

		public DefaultRouteBuilder matching(String regex) {
			parameters.put(name, regex);
			return DefaultRouteBuilder.this;
		}

		private ParametersControl build() {
			return new DefaultParametersControl(originalUri, parameters, converters, evaluator);
		}
	}

	public DefaultParameterControlBuilder withParameter(String name) {
		return builder.withParameter(name);
	}

	public <T> T is(final Class<T> type) {
		MethodInvocation<T> handler = new MethodInvocation<T>() {
			

			public Object intercept(Object proxy, Method method, Object[] args, SuperMethod superMethod) {
				defaultArguments = args;
				boolean alreadySetTheStrategy = !strategy.getClass().equals(NoStrategy.class);
				if (alreadySetTheStrategy) {
					// the virtual machine might be invoking the finalize
					return null;
				}
				is(type, method);
				return null;
			}
		};
		return proxifier.proxify(type, handler);
	}

	/**
	 * @deprecated Use @Path or override RoutesConfiguration behavior
	 */
	@Deprecated
	public void is(PatternBasedType type, PatternBasedType method) {
		this.strategy = new PatternBasedStrategy(builder.build(), type, method, this.supportedMethods, priority);

	}

	public void is(Class<?> type, Method method) {
		addParametersInfo(method);
		ResourceMethod resourceMethod = DefaultResourceMethod.instanceFor(type, method);
		String[] parameterNames = nameProvider.parameterNamesFor(method);
		this.strategy = new FixedMethodStrategy(originalUri, resourceMethod, this.supportedMethods, builder.build(), priority, parameterNames);
		parametersStore.storeDefaultParameters(strategy,defaultArguments);
		logger.info(String.format("%-50s%s -> %10s", originalUri,
				this.supportedMethods.isEmpty() ? "[ALL]" : this.supportedMethods,
				Stringnifier.simpleNameFor(method)));
	}
	


	private void addParametersInfo(Method method) {
		String[] parameters = StringUtils.extractParameters(originalUri);
		Map<String, Class<?>> types = finder.getParameterTypes(method, sanitize(parameters));
		for (Entry<String, Class<?>> entry : types.entrySet()) {
			if (!builder.parameters.containsKey(entry.getKey())) {
				builder.withParameter(entry.getKey()).ofType(entry.getValue());
			}
		}
		for (String parameter : parameters) {
			String[] split = parameter.split(":");
			if (split.length >= 2 && !builder.parameters.containsKey(parameter)) {
				builder.withParameter(parameter).matching(split[1]);
			}
		}
	}

	private String[] sanitize(String[] parameters) {
		String[] sanitized = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			sanitized[i] = parameters[i].replaceAll("(\\:.*|\\*)$", "");
		}
		return sanitized;
	}

	/**
	 * Accepts also this http method request. If this method is not invoked, any
	 * http method is supported, otherwise all parameters passed are supported.
	 *
	 * @param method
	 * @return
	 */
	public DefaultRouteBuilder with(HttpMethod method) {
		this.supportedMethods.add(method);
		return this;
	}

	public DefaultRouteBuilder with(Set<HttpMethod> methods) {
		this.supportedMethods.addAll(methods);
		return this;
	}

	/**
	 * Changes Route priority
	 *
	 * @param priority
	 * @return
	 */
	public DefaultRouteBuilder withPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public Route build() {
		if (strategy instanceof NoStrategy) {
			throw new IllegalRouteException("You have created a route, but did not specify any method to be invoked: "
					+ originalUri);
		}
		
		
		
		return strategy;
	}

	@Override
	public String toString() {
		if (supportedMethods.isEmpty()) {
			return String.format("<< Route: %s => %s >>", originalUri, this.strategy.toString());
		}
		return String.format("<< Route: %s %s=> %s >>", originalUri, supportedMethods, this.strategy.toString());
	}

}
