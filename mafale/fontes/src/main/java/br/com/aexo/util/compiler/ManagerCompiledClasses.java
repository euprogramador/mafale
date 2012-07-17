package br.com.aexo.util.compiler;

import java.io.IOException;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;

@SuppressWarnings("rawtypes")
class ManagerCompiledClasses extends ForwardingJavaFileManager {
	
	private DynamicClassLoader classLoader;

	@SuppressWarnings("unchecked")
	public ManagerCompiledClasses(JavaFileManager fileManager, DynamicClassLoader classLoader) {
		super(fileManager);
		this.classLoader = classLoader;
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
		return classLoader;
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind, FileObject sibling) throws IOException {
		String name = sibling.getName().substring(1, sibling.getName().length() - 5).replace("/", ".");
		CompiledClass jclassObject = new CompiledClass(className, kind);
		classLoader.addClass(name, jclassObject);
		return jclassObject;
	}

}
