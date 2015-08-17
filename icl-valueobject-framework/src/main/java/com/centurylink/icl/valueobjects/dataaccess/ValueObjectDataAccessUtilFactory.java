package com.centurylink.dsp.valueobjects.dataaccess;

import java.util.HashMap;
import java.util.Map;

public class ValueObjectDataAccessUtilFactory {
	
	private static Map<String, ValueObjectDataAccessUtil> valueObjectDataAccessUtilMap = new HashMap<String, ValueObjectDataAccessUtil>();

	public static ValueObjectDataAccessUtil getValueObjectDataAccessUtil(String className) {
		ValueObjectDataAccessUtil valueObjectDataAccessUtil = null;
		for (String key:valueObjectDataAccessUtilMap.keySet())
		{
			if (className.startsWith(key))
				valueObjectDataAccessUtil = valueObjectDataAccessUtilMap.get(key);
		}
		return valueObjectDataAccessUtil;
	}

	public void setValueObjectDataAccessUtil(ValueObjectDataAccessUtil valueObjectDataAccessUtil) {
		ValueObjectDataAccessUtilFactory.valueObjectDataAccessUtilMap.put(valueObjectDataAccessUtil.getClassKey(), valueObjectDataAccessUtil);
	}

}
