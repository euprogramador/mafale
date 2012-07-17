package br.com.aexo.util.compiler;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * dinamic classloader
 * 
 * @author carlosr
 * 
 */
class DynamicClassLoader extends URLClassLoader {

	private final Map<String, CompiledClass> compiledClasses = new HashMap<String, CompiledClass>();

	
	
	public DynamicClassLoader(URL[] urls, ClassLoader parent,
			URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	public DynamicClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	public DynamicClassLoader(URL[] urls) {
		super(urls);
	}


	public void addClass(String name, CompiledClass jco) {
		compiledClasses.put(name, jco);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		CompiledClass jclassObject = compiledClasses.get(name);
		if (jclassObject != null) {
			return super.defineClass(name, jclassObject.getBytes(), 0, jclassObject.getBytes().length);
		} else {
			throw new ClassNotFoundException(name);
		}
	}

}
