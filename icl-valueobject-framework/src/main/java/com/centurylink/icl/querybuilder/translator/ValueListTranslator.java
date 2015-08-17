package com.centurylink.dsp.querybuilder.translator;

import java.util.List;
import java.util.Map;

import com.centurylink.dsp.querybuilder.interfaces.VariableAndValueProcessor;
import com.centurylink.dsp.querybuilder.interfaces.VariableValueTranslator;
import com.centurylink.icl.exceptions.ICLRequestValidationException;

public class ValueListTranslator implements VariableAndValueProcessor {

	private String qualifiedField;
	private Map<String, List<String>> valueList;
	private boolean passthruUnknown = false;
	
	public ValueListTranslator(String qualifiedField, Map<String, List<String>> valueList)
	{
		this(qualifiedField, valueList, false);
	}

	public ValueListTranslator(String qualifiedField, Map<String, List<String>> valueList, boolean passthruUnknown)
	{
		this.qualifiedField = qualifiedField;
		this.valueList = valueList;
		this.passthruUnknown = passthruUnknown;
	}
	
	@Override
	public String buildWhereStatement(String varialbe, String operator,	String value) {

		String statement = "";
		
		String comma = "";
		
		if (valueList.containsKey(value.toUpperCase()))
		{
			statement += qualifiedField + " IN (";
			for (String inValue:valueList.get(value.toUpperCase()))
			{
				statement += comma + "'" + inValue + "'";
				comma = ", ";
			}
			statement += ")";
		} else {
			if (passthruUnknown)
			{
				statement += qualifiedField + " = '" + value + "'"; 
			} else {
				throw new ICLRequestValidationException("Value '" + value + "' not valid for Field '" + varialbe + "'");
			}
		}

		return statement;
	}

}
