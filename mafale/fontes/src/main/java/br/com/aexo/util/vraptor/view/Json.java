package br.com.aexo.util.vraptor.view;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.serialization.JSONSerialization;
import br.com.caelum.vraptor.serialization.NoRootSerialization;
import br.com.caelum.vraptor.serialization.ProxyInitializer;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.serialization.SerializerBuilder;
import br.com.caelum.vraptor.serialization.xstream.XStreamBuilder;
import br.com.caelum.vraptor.view.ResultException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;

/**
 * classe customizada para processar a annotação @OmitField recursivamente
 * 
 * @author carlosr
 * 
 */
@Component
public class Json implements JSONSerialization {

	protected final HttpServletResponse response;
	protected final TypeNameExtractor extractor;
	protected final ProxyInitializer initializer;
	protected final XStreamBuilder builder;

	public Json(HttpServletResponse response, TypeNameExtractor extractor, ProxyInitializer initializer, XStreamBuilder builder) {
		this.response = response;
		this.extractor = extractor;
		this.initializer = initializer;
		this.builder = builder;
	}

	public boolean accepts(String format) {
		return "json".equals(format);
	}

	public <T> Serializer from(T object) {
		return from(object, null);
	}

	public <T> Serializer from(T object, String alias) {
		response.setContentType("application/json");
		return getSerializer().from(object, alias);
	}

	protected SerializerBuilder getSerializer() {
		try {
			return new XStreamSerializer(getXStream(), response.getWriter(), extractor, initializer);
		} catch (IOException e) {
			throw new ResultException("Unable to serialize data", e);
		}
	}

	/**
	 * You can override this method for configuring Driver before serialization
	 */
	public <T> NoRootSerialization withoutRoot() {
		builder.withoutRoot();
		return this;
	}

	public JSONSerialization indented() {
		builder.indented();
		return this;
	}

	/**
	 * You can override this method for configuring XStream before serialization
	 * 
	 * @deprecated prefer overwriting XStreamBuilder
	 * @return a configured instance of xstream
	 */
	@Deprecated
	protected XStream getXStream() {
		return builder.jsonInstance();
	}

	/**
	 * You can override this method for configuring Driver before serialization
	 * 
	 * @deprecated Override this method on XStreamBuilderImpl instead. WARN:
	 *             this method will be ignored!
	 * @return configured hierarchical driver
	 */
	@Deprecated
	protected HierarchicalStreamDriver getHierarchicalStreamDriver() {
		return null;
	}

}