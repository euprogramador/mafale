package br.com.aexo.util.compiler;

import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.ToolProvider;

public class Compiler {

	/**
	 * Compile sourceCodes in classpath writing out compile.
	 * 
	 * @param sourceCodes
	 * @param classpath
	 * @param outResultCompile
	 */
	public ClassLoader compile(List<SourceCode> sourceCodes, String classpath, Writer outResultCompile) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DynamicClassLoader classLoader = new DynamicClassLoader(new URL[]{},Thread.currentThread().getContextClassLoader());
		JavaFileManager fileManager = new ManagerCompiledClasses(compiler.getStandardFileManager(null, null, null), classLoader);

		List<String> options = new ArrayList<String>();
		options.add("-g");
		options.add("-classpath");
		options.add(classpath);

		compiler.getTask(outResultCompile, fileManager, null, options, null, sourceCodes).call();

		return fileManager.getClassLoader(null);
	}

}
