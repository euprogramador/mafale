package br.com.aexo.util.vraptor.view;

import static br.com.caelum.vraptor.view.Results.representation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.config.Configuration;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.proxy.MethodInvocation;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.proxy.SuperMethod;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.view.ResultException;
import br.com.caelum.vraptor.view.Results;

@Component
public class Status implements br.com.caelum.vraptor.view.Status {

	private final HttpServletResponse response;
	private final Result result;
	private final Configuration config;
	private final Router router;
	private final Proxifier proxifier;

	public Status(HttpServletResponse response, Result result, Configuration config, Proxifier proxifier, Router router) {
		this.response = response;
		this.result = result;
		this.config = config;
		this.proxifier = proxifier;
		this.router = router;
	}

	public void notFound() {
		sendError(HttpServletResponse.SC_NOT_FOUND);
	}
	
	
	private void sendError(int error) {
		try {
			response.sendError(error);
		} catch (IOException e) {
			throw new ResultException(e);
		}
	}

	private void sendError(int error, String message) {
		try {
			response.sendError(error, message);
		} catch (IOException e) {
			throw new ResultException(e);
		}
	}

	
	public void header(String key, String value) {
		response.addHeader(key, value);
	}

	public void created() {
		response.setStatus(HttpServletResponse.SC_CREATED);
		result.use(Results.nothing());
	}

	public void created(String location) {
		header("Location", fixLocation(location));
		created();
	}

	public void ok() {
		response.setStatus(HttpServletResponse.SC_OK);
		result.use(Results.nothing());
	}

	public void conflict() {
		sendError(HttpServletResponse.SC_CONFLICT);
	}

	public void methodNotAllowed(EnumSet<HttpMethod> allowedMethods) {
		header("Allow", allowedMethods.toString().replaceAll("\\[|\\]", ""));

		sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	public void movedPermanentlyTo(String location) {
		this.response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		header("Location", fixLocation(location));

		this.response.addIntHeader("Content-length", 0);
		this.response.addDateHeader("Date", System.currentTimeMillis());
	}

	private String fixLocation(String location) {
		if (location.startsWith("/")) {
			return config.getApplicationPath() + location;
		}
		return location;
	}

	public <T> T movedPermanentlyTo(final Class<T> controller) {
		return proxifier.proxify(controller, new MethodInvocation<T>() {
			public Object intercept(T proxy, Method method, Object[] args, SuperMethod superMethod) {
				String uri = router.urlFor(controller, method, args);
				movedPermanentlyTo(uri);
				return null;
			}
		});
	}

	public void unsupportedMediaType(String message) {
		sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, message);
	}

	public void badRequest(String message) {
		try {
			response.addHeader("mensagem", new String(message.getBytes(),"ISO-8859-1"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			response.getOutputStream().write(message.getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	public void badRequest(List<?> errors) {
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		result.use(representation()).from(errors, "errors").serialize();
	}

	public void forbidden(String message) {
		sendError(HttpServletResponse.SC_FORBIDDEN, message);
	}

	public void noContent() {
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}

	public void notAcceptable() {
		sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
	}

	public void accepted() {
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		result.use(Results.nothing());
	}

	public void notModified() {
		response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
	}

}
