package com.centurylink.dsp.valueobjects.cached;

import com.centurylink.dsp.valueobjects.abs.AbstractReadOnlyTable;

public interface ValueObjectCache {

	public AbstractReadOnlyTable getCacheObject(String key);
}
