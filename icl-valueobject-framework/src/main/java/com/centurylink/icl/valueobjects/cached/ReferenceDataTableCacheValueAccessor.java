package com.centurylink.dsp.valueobjects.cached;

public interface ReferenceDataTableCacheValueAccessor {
	
	public String getReferenceKeyByValue(String refValue, String valueColumnName);
	public String getReferenceValue(String cacheKey, String valueColumnName);

}
