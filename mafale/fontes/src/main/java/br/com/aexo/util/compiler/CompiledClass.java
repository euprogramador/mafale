package br.com.aexo.util.compiler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;


/**
 * Represent of compiled class
 * 
 * @author carlosr
 * 
 */
class CompiledClass extends SimpleJavaFileObject {

	private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public CompiledClass(String name, Kind kind) {
		super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
	}

	public byte[] getBytes() {
		return bos.toByteArray();
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return bos;
	}
}