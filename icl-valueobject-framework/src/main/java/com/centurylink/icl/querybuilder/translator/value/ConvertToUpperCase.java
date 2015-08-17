package com.centurylink.dsp.querybuilder.translator.value;

import com.centurylink.dsp.querybuilder.interfaces.VariableValueTranslator;

public class ConvertToUpperCase implements VariableValueTranslator {

	@Override
	public String translateValue(String value) {
		
		return value.toUpperCase();
	}

}
