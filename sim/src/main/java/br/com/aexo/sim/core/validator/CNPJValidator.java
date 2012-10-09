package br.com.aexo.sim.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CNPJValidator implements ConstraintValidator<CNPJ, String> {

	@Override
	public void initialize(CNPJ arg0) {
	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		try {
			validaCNPJ(arg0.replace(".","").replace("/","").replace("-",""));
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public static void validaCNPJ(String cnpj) {
		if (cnpj == null || cnpj.length() != 14)
			throw new IllegalArgumentException();

		try {
			Long.parseLong(cnpj);
		} catch (NumberFormatException e) { // CNPJ não possui somente números
			throw new IllegalArgumentException();
		}

		int soma = 0;
		String cnpj_calc = cnpj.substring(0, 12);

		char chr_cnpj[] = cnpj.toCharArray();
		for (int i = 0; i < 4; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9)
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));

		int dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();
		soma = 0;
		for (int i = 0; i < 5; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));

		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9)
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));

		dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(dig != 10 && dig != 11 ? Integer.toString(dig) : "0").toString();

		if (!cnpj.equals(cnpj_calc))
			throw new IllegalArgumentException();
	}

}