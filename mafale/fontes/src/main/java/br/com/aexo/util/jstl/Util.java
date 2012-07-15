package br.com.aexo.util.jstl;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class Util {

	public String urlEncode(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, "ISO-8859-1");
	}

	public String md5(String valor) {
		if (valor == null)
			return null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		BigInteger hash = new BigInteger(1, md.digest(valor.getBytes()));
		String md5 = hash.toString(16);
		return md5;

	}

}
