package br.com.aexo.util.vraptor.deserializer;

import java.io.InputStream;
import java.lang.reflect.Method;

import net.vidageek.mirror.dsl.Mirror;

import br.com.caelum.iogi.Instantiator;
import br.com.caelum.iogi.parameters.Parameters;
import br.com.caelum.iogi.reflection.Target;
import br.com.caelum.vraptor.deserialization.Deserializer;
import br.com.caelum.vraptor.deserialization.Deserializes;
import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.serialization.xstream.VRaptorClassMapper;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * 
 * @author Guilherme Silveira
 * @since 3.1.3
 */
@Deserializes({"application/json","json"})
public class JsonDeserializer implements Deserializer{

	private final ParameterNameProvider provider;
	private final Instantiator<Object> instatiator;

	public JsonDeserializer(ParameterNameProvider provider,TypeNameExtractor extractor,Instantiator<Object> instantiator) {
		this.provider = provider;
		this.extractor = extractor;
		this.instatiator = instantiator;
	}

	private final TypeNameExtractor extractor;
	private HierarchicalStreamDriver driver =  new JettisonMappedXmlDriver();

	public Object[] deserialize(InputStream inputStream, ResourceMethod method) {
		Method javaMethod = method.getMethod();
		Class<?>[] types = javaMethod.getParameterTypes();
		if (types.length == 0) {
			throw new IllegalArgumentException("Methods that consumes representations must receive just one argument: the root element");
		}
		XStream xStream = getConfiguredXStream(javaMethod, types);

		Object[] params = new Object[types.length];

		chooseParam(types, params, xStream.fromXML(inputStream));

		return params;
	}

	/**
	 * Returns an xstream instance already configured.
	 */
	public XStream getConfiguredXStream(Method javaMethod, Class<?>[] types) {
		XStream xStream = getXStream();
		aliasParams(javaMethod, types, xStream);
		return xStream;
	}

	private void chooseParam(Class<?>[] types, Object[] params, Object deserialized) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].isInstance(deserialized)) {
				params[i] = deserialized;
			}
		}
	}

	private void aliasParams(Method method, Class<?>[] types, XStream deserializer) {
		String[] names = provider.parameterNamesFor(method);
		for (int i = 0; i < names.length; i++) {
			deserializer.alias(names[i], types[i]);
		}
	}

	/**
	 * Extension point to configure your xstream instance.
	 * @return the configured xstream instance
	 */
	protected XStream getXStream() {
		ReflectionProvider reflectionProvider = new Sun14ReflectionProvider(){
			@Override
			public Object newInstance(Class type) {
				return instatiator.instantiate(new Target(new Mirror().reflectClass(type.getName()), ""), new Parameters());
			}
		};

		return new XStream(reflectionProvider,getHierarchicalStreamDriver()) {
			{setMode(NO_REFERENCES);}
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new VRaptorClassMapper(next, extractor);
			}
		};
	}

	/**
	 * You can override this method for configuring Driver before serialization
	 */
	protected HierarchicalStreamDriver getHierarchicalStreamDriver() {
		return this.driver;
	}

}
