package com.centurylink.dsp.querybuilder.translator.value;

import java.util.Map;

import com.centurylink.dsp.querybuilder.interfaces.VariableValueTranslator;

public class ValueMapper implements VariableValueTranslator {
	
	private Map<String, String> valueMap;
	
	public ValueMapper(Map<String, String> valueMap)
	{
		this.valueMap = valueMap;
	}

	@Override
	public String translateValue(String value) {
		
		value = value.toUpperCase();
		
		if (null != valueMap)
		{
			if (valueMap.containsKey(value))
				value = valueMap.get(value);
		}
		
		return value;
	}

}
