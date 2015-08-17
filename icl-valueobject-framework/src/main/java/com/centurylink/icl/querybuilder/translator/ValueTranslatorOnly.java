package com.centurylink.dsp.querybuilder.translator;

import com.centurylink.dsp.querybuilder.interfaces.VariableAndValueProcessor;
import com.centurylink.dsp.querybuilder.interfaces.VariableValueTranslator;

public class ValueTranslatorOnly implements VariableAndValueProcessor {

	private VariableValueTranslator valueTranslator;
	private String qualifiedField;
	
	public ValueTranslatorOnly(String qualifiedField, VariableValueTranslator valueTranslator)
	{
		this.valueTranslator = valueTranslator;
		this.qualifiedField = qualifiedField;
	}
	
	@Override
	public String buildWhereStatement(String varialbe, String operator,	String value) {

		return qualifiedField + " " + operator + " '" + valueTranslator.translateValue(value) + "'";
		
	}

}
