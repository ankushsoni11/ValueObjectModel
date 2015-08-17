package com.centurylink.dsp.valueobjects.dataaccess;

import java.util.Map;

import com.centurylink.dsp.valueobjects.cached.ValueObjectCache;

public class ValueObjectCacheAccessorFactory {
	
	private static Map<String, ValueObjectCache> cacheMap;
	
	public static ValueObjectCache getValueObjectCache(String key)
	{
		if (cacheMap.containsKey(key))
			return cacheMap.get(key);
		else
			return null;
	}
	
	public void setCacheMap(Map<String, ValueObjectCache> cacheMap)
	{
		ValueObjectCacheAccessorFactory.cacheMap = cacheMap;
	}

}
