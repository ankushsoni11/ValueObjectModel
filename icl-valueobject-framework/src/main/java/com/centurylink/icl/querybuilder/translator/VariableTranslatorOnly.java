package com.centurylink.dsp.querybuilder.translator;

import com.centurylink.dsp.querybuilder.interfaces.VariableAndValueProcessor;
import com.centurylink.dsp.querybuilder.interfaces.VariableValueTranslator;

public class VariableTranslatorOnly implements VariableAndValueProcessor {

	private String qualifiedField;
	
	public VariableTranslatorOnly(String qualifiedField)
	{
		this.qualifiedField = qualifiedField;
	}
	
	@Override
	public String buildWhereStatement(String varialbe, String operator,	String value) {

		return qualifiedField + " " + operator + " '" + value + "'";
		
	}
}
