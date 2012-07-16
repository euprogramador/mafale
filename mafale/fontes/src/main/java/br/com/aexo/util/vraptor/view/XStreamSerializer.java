package br.com.aexo.util.vraptor.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.vidageek.mirror.dsl.Matcher;
import net.vidageek.mirror.dsl.Mirror;
import br.com.aexo.util.vraptor.xstream.XstreamProcessAnnotation;
import br.com.caelum.vraptor.interceptor.TypeNameExtractor;
import br.com.caelum.vraptor.serialization.ProxyInitializer;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.serialization.SerializerBuilder;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * A SerializerBuilder based on XStream
 * @author Lucas Cavalcanti
 * @since 3.0.2
 */
public class XStreamSerializer implements SerializerBuilder {

	private final XStream xstream;
	private final Writer writer;
	private Object root;
	private Class<?> rootClass;
	private final Multimap<Class<?>, String> excludes = LinkedListMultimap.create();
	private Set<Class<?>> elementTypes;
	private final TypeNameExtractor extractor;
	private final ProxyInitializer initializer;

	public XStreamSerializer(XStream xstream, Writer writer, TypeNameExtractor extractor, ProxyInitializer initializer) {
		this.xstream = xstream;
		this.writer = writer;
		this.extractor = extractor;
		this.initializer = initializer;
	}

	private boolean isPrimitive(Class<?> type) {
		return type.isPrimitive()
			|| type.isEnum()
			|| Number.class.isAssignableFrom(type)
			|| type.equals(String.class)
			|| Date.class.isAssignableFrom(type)
			|| Calendar.class.isAssignableFrom(type)
			|| Boolean.class.equals(type)
			|| Character.class.equals(type);
	}

	public Serializer exclude(String... names) {
		for (String name : names) {
			Set<Class<?>> parentTypes = getParentTypesFor(name);
			for (Class<?> type : parentTypes) {
				xstream.omitField(type, getNameFor(name));
			}
		}
		return this;
	}

	private String getNameFor(String name) {
		String[] path = name.split("\\.");
		return path[path.length-1];
	}

	private Set<Class<?>> getParentTypesFor(String name) {
		if (elementTypes == null) {
			Class<?> type = rootClass;
			return getParentTypes(name, type);
		} else {
			Set<Class<?>> result = new HashSet<Class<?>>();
			for (Class<?> type : elementTypes) {
				result.addAll(getParentTypes(name, type));
			}
			return result;
		}
	}

	private Set<Class<?>> getParentTypes(String name, Class<?> type) {
		String[] path = name.split("\\.");
		for (int i = 0; i < path.length - 1; i++) {
			type = getActualType(new Mirror().on(type).reflect().field(path[i]).getGenericType());
		}
		Set<Class<?>> types = Sets.newHashSet();
		while(type != Object.class) {
			types.add(type);
			type = type.getSuperclass();
		}
		return types;
	}

	private void preConfigure(Object obj,String alias) {
		checkNotNull(obj, "You can't serialize null objects");

		xstream.processAnnotations(obj.getClass());

		processRecursiveAnnotations(obj);
		
		rootClass = initializer.getActualClass(obj);
		if (alias == null && initializer.isProxy(obj.getClass())) {
			alias = extractor.nameFor(rootClass);
		}

		setRoot(obj);

		setAlias(obj, alias);
	}

	private void processRecursiveAnnotations(Object obj) {

		List<Field> fields = new Mirror().on(obj.getClass()).reflectAll().fieldsMatching(new Matcher<Field>() {
			
			@Override
			public boolean accepts(Field field) {
				return field.isAnnotationPresent(XstreamProcessAnnotation.class);
			}
		});
		
		for (Field field : fields){
			Class type = field.getType();
			if (Collection.class.isAssignableFrom(field.getType())){
				Object value = new Mirror().on(obj).get().field(field);
				List list = new ArrayList((Collection)value);
				if (!list.isEmpty()){
					type = list.get(0).getClass();
				}
			}
			xstream.processAnnotations(type);
			processRecursiveAnnotations(type);
		}
	
	}

	private void setRoot(Object obj) {
		if (Collection.class.isInstance(obj)) {
			this.root = normalizeList(obj);
		} else {
			Class<?> type = rootClass;
			excludeNonPrimitiveFields(type);
			this.root = obj;
		}
	}

	@SuppressWarnings("unchecked")
	private Collection<Object> normalizeList(Object obj) {
		Collection<Object> list;
		if (hasDefaultConverter()) {
			list = new ArrayList<Object>((Collection<?>)obj);
		} else {
			list = (Collection<Object>) obj;
		}
		elementTypes = findElementTypes(list);
		for (Class<?> type : elementTypes) {
			excludeNonPrimitiveFields(type);
		}
		return list;
	}

	private boolean hasDefaultConverter() {
		return xstream.getConverterLookup().lookupConverterForType(rootClass).equals(xstream.getConverterLookup().lookupConverterForType(Object.class));
	}

	private void setAlias(Object obj, String alias) {
		if (alias != null) {
			if (Collection.class.isInstance(obj) && (List.class.isInstance(obj) || hasDefaultConverter())) {
				xstream.alias(alias, List.class);
			}
			xstream.alias(alias, obj.getClass());
		}
	}

	public <T> Serializer from(T object, String alias) {
		preConfigure(object, alias);
		return this;
	}

	
	public <T> Serializer from(T object) {
		preConfigure(object, null);
		return this;
	}

	private Set<Class<?>> findElementTypes(Collection<Object> list) {
		Set<Class<?>> set = new HashSet<Class<?>>();
		for (Object element : list) {
			if (element != null && !isPrimitive(element.getClass())) {
				set.add(initializer.getActualClass(element));
			}
		}
		return set;
	}

	private void excludeNonPrimitiveFields(Class<?> type) {
		for (Field field : new Mirror().on(type).reflectAll().fields()) {
			if (!isPrimitive(field.getType())) {
				excludes.put(field.getDeclaringClass(), field.getName());
			}
		}
	}

	public Serializer include(String... fields) {
		for (String field : fields) {
			try {
				Set<Class<?>> parentTypes = getParentTypesFor(field);
				String fieldName = getNameFor(field);
				for (Class<?> parentType : parentTypes) {
					Type genericType = new Mirror().on(parentType).reflect().field(fieldName).getGenericType();
					Class<?> fieldType = getActualType(genericType);

					if (!excludes.containsKey(fieldType)) {
						excludeNonPrimitiveFields(fieldType);
					}
					excludes.remove(parentType, fieldName);
				}
			} catch (NullPointerException e) {
				throw new IllegalArgumentException("Field path " + field + " doesn't exist");
			}
		}
		return this;
	}

	private Class<?> getActualType(Type genericType) {
		if (genericType instanceof ParameterizedType) {
			ParameterizedType type = (ParameterizedType) genericType;

			if (isCollection(type)) {
				Type actualType = type.getActualTypeArguments()[0];

				if (actualType instanceof TypeVariable<?>) {
					return (Class<?>) type.getRawType();
				}

				return (Class<?>) actualType;
			}
		}

		return (Class<?>) genericType;
	}

	private boolean isCollection(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType ptype = (ParameterizedType) type;
			return Collection.class.isAssignableFrom((Class<?>) ptype.getRawType())
			  || Map.class.isAssignableFrom((Class<?>) ptype.getRawType());
		}
		return Collection.class.isAssignableFrom((Class<?>) type);
	}

	public void serialize() {
		for (Entry<Class<?>, String> exclude : excludes.entries()) {
			xstream.omitField(exclude.getKey(), exclude.getValue());
		}
		registerProxyInitializer();
		xstream.toXML(root, writer);
	}

	public Serializer recursive() {
		excludes.clear();
		return this;
	}

	private void registerProxyInitializer() {
		xstream.registerConverter(new Converter() {

			public boolean canConvert(@SuppressWarnings("rawtypes") Class clazz) {
				return initializer.isProxy(clazz);
			}

			public Object unmarshal(HierarchicalStreamReader reader,
					UnmarshallingContext context) {
				throw new AssertionError();
			}

			public void marshal(Object value, HierarchicalStreamWriter writer,
					MarshallingContext context) {
				Converter converter = xstream.getConverterLookup().lookupConverterForType(initializer.getActualClass(value));
				initializer.initialize(value);
				converter.marshal(value, writer, context);
			}
		});
	}
}