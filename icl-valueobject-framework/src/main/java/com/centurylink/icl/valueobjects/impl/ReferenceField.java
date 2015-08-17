package com.centurylink.dsp.valueobjects.impl;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurylink.dsp.valueobjects.dataaccess.ValueObjectDataAccessUtil;
import com.centurylink.dsp.valueobjects.dataaccess.ValueObjectDataAccessUtilFactory;
import com.centurylink.icl.exceptions.ICLException;

public class ReferenceField extends Field {

	private static final Log LOG = LogFactory.getLog(ReferenceField.class);

	private String refTableName;
	private String refValueColumn;
	private String refKeyColumn;
	private String refValue;
	
	private String REF_SELECT_PRE; 
	
	private ValueObjectDataAccessUtil valueObjectDataAccessUtil;
	
	public ReferenceField (String name, String type, String refTableName, String refValueColumn)
	{
		this(name, type, refTableName, refValueColumn, name);
	}
	
	public ReferenceField (String name, String type, String refTableName, String refValueColumn, String refKeyColumn)
	{
		this(name, type, refTableName, refValueColumn, name, "com.centurylink.icl.clcnew");
	}
	
	public ReferenceField (String name, String type, String refTableName, String refValueColumn, String refKeyColumn, String classKey)
	{
		super(name, type);
		this.refTableName = refTableName;
		this.refValueColumn = refValueColumn;
		this.refKeyColumn = name;
		this.refKeyColumn = refKeyColumn;

		valueObjectDataAccessUtil = ValueObjectDataAccessUtilFactory.getValueObjectDataAccessUtil(classKey);

		REF_SELECT_PRE = "SELECT " + refKeyColumn + ", " + refValueColumn + " FROM " + refTableName + " WHERE ";
	}
	
	@Override
	public void setValue (Object value)
	{
		if(value!=null){// this is case when we don't have the Priority Id, in that case value is null 
			String statement = buildSelectByKey(value.toString());
			
			LOG.debug(statement);
			
			Map<String, Object> map = valueObjectDataAccessUtil.getRecordAsMap(statement);
			
			super.setValue(value);
	
			this.refValue = map.get(refValueColumn).toString();
		}
	}

	@Override
	public void setValueByRef (Object value)
	{
			String statement = buildSelectByValue(value.toString());
			
			LOG.debug(statement);
			
			Map<String, Object> map = valueObjectDataAccessUtil.getRecordAsMap(statement);
			
			this.refValue = value.toString();
			
			// Pushkin - 13-Sep-13-Fix to prevent null pointer exception if ref data in request
			// is not found in Db
			if(map == null) {
				throw new ICLException("Incorrect value for reference data column "+refKeyColumn+". Value "+refValue+" is not supported");
			}
			
			super.setValue(map.get(refKeyColumn));
	}
	
	@Override
	public Object getValue()
	{
		return super.getValue();
	}

	@Override
	public String getValueAsString()
	{
		return this.refValue;
	}
	
	private String buildSelectByValue(String searchValue)
	{
		String statement = REF_SELECT_PRE + refValueColumn + " = '" + searchValue + "'";
		return statement;
	}
	
	private String buildSelectByKey(String keyValue)
	{
		String statement = REF_SELECT_PRE + refKeyColumn + " = '" + keyValue + "'";
		return statement;
	}
	
}
