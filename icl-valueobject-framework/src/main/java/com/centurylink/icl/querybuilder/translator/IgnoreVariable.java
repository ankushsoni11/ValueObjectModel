package com.centurylink.dsp.querybuilder.translator;

import com.centurylink.dsp.querybuilder.interfaces.VariableAndValueProcessor;

public class IgnoreVariable implements VariableAndValueProcessor {

	@Override
	public String buildWhereStatement(String varialbe, String operator,	String value) {
		return null;
	}

}
