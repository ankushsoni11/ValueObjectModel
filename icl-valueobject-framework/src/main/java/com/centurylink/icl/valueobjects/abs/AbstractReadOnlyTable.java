package com.centurylink.dsp.valueobjects.abs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurylink.dsp.valueobjects.dataaccess.ValueObjectDataAccessUtil;
import com.centurylink.dsp.valueobjects.dataaccess.ValueObjectDataAccessUtilFactory;
import com.centurylink.dsp.valueobjects.impl.Field;
import com.centurylink.dsp.valueobjects.impl.LinkedTable;
import com.centurylink.dsp.valueobjects.impl.PrimaryKey;
import com.centurylink.dsp.valueobjects.model.Table;
import com.centurylink.icl.common.util.StringHelper;


public abstract class AbstractReadOnlyTable implements Table {
	
	private static final long serialVersionUID = 1L;

	private static final Log LOG = LogFactory.getLog(AbstractReadOnlyTable.class);

	protected HashMap<String, Field> fields = new HashMap<String, Field>();
	protected PrimaryKey primaryKey;
	protected String tableName;

	protected boolean instanciated = false;

	protected ValueObjectDataAccessUtil valueObjectDataAccessUtil;
	
	protected List<LinkedTable> linkedTables = new ArrayList<LinkedTable>();
	
	public AbstractReadOnlyTable()
	{
		valueObjectDataAccessUtil = ValueObjectDataAccessUtilFactory.getValueObjectDataAccessUtil(this.getClass().getName());
		
		populateModel();
	}
	
	public Object getField(String key)
	{
		return fields.get(key).getValue();
	}
	
	public String getFieldAsString(String key)
	{
		return fields.get(key).getValueAsString();
	}
	
	public void setField(String key, Object value)
	{
		fields.get(key).setValue(value);
	}
	
	public void setFieldByRef(String key, Object value)
	{
		if (null == value || value.toString().trim().equals(""))
			fields.get(key).setValue(value);
		else
			fields.get(key).setValueByRef(value);
	}
	
	public String getPrimaryKeyAsString()
	{
		return this.primaryKey.toString();
	}
	
	public String getPrimaryKeyValueAsString()
	{
		return this.getFieldAsString(this.primaryKey.getName());
	}
	
	public HashMap<String, Field> getFields()
	{
		return this.fields;
	}
	
	protected void getRecordByPrimaryKey()
	{
		getRecordByPrimaryKey(false);
	}

	protected void getRecordByPrimaryKey(boolean cacheOveride)
	{
		String statement = "SELECT * FROM " + this.tableName + " WHERE " + primaryKey.getQueryValue();
		getRecordAsMap(statement, cacheOveride);
		resetChangedFlag();
	}
	
	protected void getRecordByTemplate(AbstractReadOnlyTable abstractTable)
	{
		String statement = "SELECT * FROM " + abstractTable.tableName + buildWhere(abstractTable.fields);
		
		getRecordAsMap(statement, false);
		resetChangedFlag();
	}
		
	public List<Map<String,Object>> getRecordsByTemplate()
	{
		String statement = "SELECT * FROM " + this.tableName + buildWhere(this.fields);
		List<Map<String,Object>> records = valueObjectDataAccessUtil.getRecordsAsList(statement);
		return records;
	}
	
	@Deprecated
	/**
	 * Returns a distinct list of Primary Keys that can be used to pull fully instantiated objects. 
	 * Using this method to pull records based on the query proved to be slow as it required rehitting the database 
	 * for each key that was returned. getFullRecordsByQuery proved to be much faster as it returned all the records in a
	 * single database call. 
	 * @param query
	 * @return
	 * @deprecated use {@link #getFullRecordsByQuery(String)} instead
	 */
	protected List<Map<String,Object>> getRecordsByQuery(String query)
	{
		String statement = "SELECT distinct(" + this.tableName +"." + this.primaryKey.getName() + ") FROM " + this.tableName;
		
		for (LinkedTable linkedTable:linkedTables)
		{
			statement += ", " + linkedTable.getChildTableName();
		}
		
		statement += " WHERE 1=1 ";

		for (LinkedTable linkedTable:linkedTables)
		{
			statement += " AND " + linkedTable.getWhereClause() + " ";
		}
				
		statement += "AND " + query;
		
		LOG.debug("AbstractTable.getRecordsByQuery - statement: " + statement);
		
		List<Map<String,Object>> records = valueObjectDataAccessUtil.getRecordsAsList(statement);
		return records;
	}
	
	protected List<Map<String,Object>> getFullRecordsByQuery(String query)
	{
		return getFullRecordsByQuery(query, -1, 1, null);
	}

	protected List<Map<String,Object>> getFullRecordsByQuery(String query, int pageSize, int pageNumber, String orderByOverride)
	{
		String statement = buildFullRecordsStatement(query, orderByOverride);
		
		if (pageNumber < 1)
			pageNumber = 1;
		
		if (pageSize > 0)
		{
			int maxRowToFetch = pageSize * pageNumber;
			int minRowToFetch = (pageSize * (pageNumber - 1) + 1);
			
			LOG.debug("AbstractTable.getRecordsByQuery - Fetching Rows " + minRowToFetch + " to " + maxRowToFetch);
			
			//I'm not sure that the "FIRST_ROWS(N)" hint does any good here as the underlying select has a distinct in it but worst case the optimizer will ignore it
			statement = "select * from ( select /*+ FIRST_ROWS(" + maxRowToFetch + ") */ a.*, ROWNUM rnum from (" + statement;
			statement += ") a where ROWNUM <= " + maxRowToFetch + " ) where rnum  >= " + minRowToFetch;
		}
		
		LOG.debug("AbstractTable.getRecordsByQuery - statement: " + statement);
		
		List<Map<String,Object>> records = valueObjectDataAccessUtil.getRecordsAsList(statement);

		return records;
	}
	
	protected int getRecordCountByQuery(String query)
	{
		String statement = "select count(*) from " + this.tableName + " where " + this.primaryKey.getName() + " in (";
		statement += buildDistinctStatement(query) + ")";
		
		return valueObjectDataAccessUtil.getIntFromStatement(statement);
	}
	
	private String buildFullRecordsStatement(String query, String orderByOverride)
	{
		if (StringHelper.isEmpty(orderByOverride) && this.primaryKey != null)
			orderByOverride = this.primaryKey.getName();
		
		String statement = "select * from " + this.tableName + " where " + this.primaryKey.getName() + " in (";
		statement += buildDistinctStatement(query) + ")";
		
		if (!StringHelper.isEmpty(orderByOverride))
		{
			statement += " ORDER BY " + orderByOverride;
		}
		
		return statement;
	}
	
	private String buildDistinctStatement(String query)
	{
		String statement = "SELECT distinct(" + this.tableName +"." + this.primaryKey.getName() + ") FROM " + this.tableName;
		
		for (LinkedTable linkedTable:linkedTables)
		{
			statement += ", " + linkedTable.getChildTableName();
		}
		
		statement += " WHERE 1=1 ";

		for (LinkedTable linkedTable:linkedTables)
		{
			statement += " AND " + linkedTable.getWhereClause() + " ";
		}
				
		statement += "AND " + query + "";
		
		return statement;
	}
	
	private void getRecordAsMap(String statement, boolean cacheOveride)
	{
		LOG.debug("Statement: " + statement);
		
		Map<String, Object> recordMap = valueObjectDataAccessUtil.getRecordAsMap(statement, cacheOveride);
		populateFields(recordMap);
	}
	
	//TODO: Mickey - This should be protected but it breaks the cache code in CLC... 
	public void populateFields(Map<String, Object> recordMap)
	{
		if (recordMap != null)
		{
			for (String key:recordMap.keySet())
			{
				if (fields.containsKey(key))
				{
					fields.get(key).setValue(recordMap.get(key));
				}
			}
			this.instanciated = true;
		}
	}
	
	protected String buildWhere(HashMap<String, Field> fields)
	{
		String where = " WHERE 1=1 ";
		
		for (Field field:fields.values())
		{
			if (field.getValue() != null)
				where += " AND " + field.getName() + " = " + field.getQueryValue();
		}
		
		return where;
	}
		
	public void resetChangedFlag()
	{
		for (Field field:fields.values())
		{
			field.setChanged(false);
		}
	}
	
	public boolean isInstanciated()
	{
		return this.instanciated;
	}

	public String getModelAsString()
	{
		String response = "";
		
		response = response + "PRI: " + primaryKey.toString() + "\n";
		
		for (Field field:fields.values())
		{
			response = response + field.toString() + " \n";
		}
		
		return response;
	}
	
}
