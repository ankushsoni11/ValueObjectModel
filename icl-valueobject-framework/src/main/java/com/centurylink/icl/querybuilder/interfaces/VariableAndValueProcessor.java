package com.centurylink.dsp.querybuilder.interfaces;

public interface VariableAndValueProcessor {

	public String buildWhereStatement(String varialbe, String operator, String value);
	
}
