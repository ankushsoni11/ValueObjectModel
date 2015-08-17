package com.centurylink.dsp.valueobjects.impl;

public class LinkedTable {
	
	protected String parentTable;
	protected String childTable;
	protected String parentLinkField;
	protected String childLinkField;
	protected String childTableAlias;
	
	protected boolean leftOuterJoin;
	
	public LinkedTable(String parentTable, String childTable, String parentLinkField, boolean leftOuterJoin)
	{
		this.parentTable = parentTable;
		this.childTable = childTable;
		this.parentLinkField = parentLinkField;
		this.childLinkField = parentLinkField;
		this.leftOuterJoin = leftOuterJoin;
		this.childTableAlias = null;
	}
	
	public LinkedTable(String parentTable, String childTable, String parentLinkField, String childLinkField, boolean leftOuterJoin)
	{
		this(parentTable, childTable, parentLinkField, leftOuterJoin);
		this.childLinkField = childLinkField;
	}

	public LinkedTable(String parentTable, String childTable, String childTableAlias, String parentLinkField, String childLinkField, boolean leftOuterJoin)
	{
		this(parentTable, childTable, parentLinkField, childLinkField, leftOuterJoin);
		this.childTableAlias = childTableAlias;
	}
	
	public String getWhereClause()
	{
		String join = leftOuterJoin?"(+)":""; 
		String tableName = childTableAlias != null?childTableAlias:childTable;
		
		return parentTable + "." + parentLinkField + " = " + tableName + "." + childLinkField + join;
	}
	
	public String getChildTableName()
	{
		String tableName = childTable;
		
		if (childTableAlias != null)
		{
			tableName += " " + childTableAlias;
		}
		
		return tableName;
	}
	
	public String toString()
	{
		String response = "";
		
		response += "Tables " + parentTable + " & " + childTable;
		response += " Linked by fields " + parentLinkField + " & " + childLinkField;
		
		return response;
	}

}
