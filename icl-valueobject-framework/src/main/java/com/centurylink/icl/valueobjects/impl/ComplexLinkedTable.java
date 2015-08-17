package com.centurylink.dsp.valueobjects.impl;

import java.util.ArrayList;
import java.util.List;


public class ComplexLinkedTable extends LinkedTable {

	private List<LinkedTable> subLinkedTables = new ArrayList<LinkedTable>();
	
	public ComplexLinkedTable(String parentTable, String childTable, String parentLinkField, boolean leftOuterJoin) {
		super(parentTable, childTable, parentLinkField, leftOuterJoin);
	}

	public ComplexLinkedTable(String parentTable, String childTable, String parentLinkField, String childLinkField, boolean leftOuterJoin) {
		super(parentTable, childTable, parentLinkField, childLinkField, leftOuterJoin);
	}
	
	public void addSubLink(String tableName, String fieldName, boolean leftOuterJoin)
	{
		subLinkedTables.add(new LinkedTable(this.childTable, tableName, fieldName, leftOuterJoin));
	}

	public void addSubLink(String tableName, String fieldName, String subFieldName, boolean leftOuterJoin)
	{
		subLinkedTables.add(new LinkedTable(this.childTable, tableName, fieldName, subFieldName, leftOuterJoin));
	}
	
	@Override
	public String getWhereClause()
	{
		//String where = parentTable + "." + parentLinkField + " = " + childTable + "." + childLinkField;
		String where = super.getWhereClause();
		
		for (LinkedTable linkedTable:subLinkedTables)
		{
			where += " AND " + linkedTable.getWhereClause();
		}
		
		return where;
	}
	
	@Override
	public String getChildTableName()
	{
		String tableName = childTable;
		
		for (LinkedTable linkedTable:subLinkedTables)
		{
			tableName += ", " + linkedTable.getChildTableName();
		}

		return tableName;
	}
	

}
