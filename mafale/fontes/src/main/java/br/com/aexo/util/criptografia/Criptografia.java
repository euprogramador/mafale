package br.com.aexo.util.criptografia;

import java.io.Serializable;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import br.com.aexo.util.exceptions.InfraestruturaException;
import br.com.caelum.vraptor.ioc.Component;

/**
 * Classe que faz a criptografia em MD5
 * 
 * @author carlosr
 * 
 */
@Component
public class Criptografia implements Serializable {

	private static final long serialVersionUID = 1L;

	private static SecretKey skey;
	private static KeySpec ks;
	private static PBEParameterSpec ps;
	private static final String algorithm = "PBEWithMD5AndDES";

	public Criptografia() {
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
			ps = new PBEParameterSpec(new byte[] { 3, 1, 4, 1, 5, 9, 2, 6 }, 20);

			ks = new PBEKeySpec("EAlGeEen3/m8/YkO".toCharArray());

			skey = skf.generateSecret(ks);
		} catch (java.security.NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (java.security.spec.InvalidKeySpecException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Criptografa um texto em MD5
	 * 
	 * @param texto
	 * @return
	 */

	public String criptografa(final String text) {
		try {
			final Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, skey, ps);
			return new String(Base64Coder.encode(cipher.doFinal(text.getBytes())));
		} catch (Exception e) {
			throw new InfraestruturaException("Não foi possível criptografar");
		}
	}

	public String descriptografa(final String text) {
		try {
			final Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, skey, ps);
			String ret = null;
			try {
				ret = new String(cipher.doFinal(Base64Coder.decode(text)));
			} catch (Exception ex) {
			}
			return ret;
		} catch (Exception e) {
			throw new InfraestruturaException("Não foi possível descriptografar");
		}
	}

}