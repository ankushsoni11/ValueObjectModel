package com.centurylink.dsp.valueobjects.cached;

import com.centurylink.dsp.valueobjects.abs.AbstractReferenceDataTable;


public interface ReferenceDataTableCacheCommon extends ReferenceDataTableCache,ReferenceDataTableCacheValueAccessor {

	public void modifyReferenceDataTables(boolean isDeleteOperation,AbstractReferenceDataTable abstractReferenceDataTable);
}
