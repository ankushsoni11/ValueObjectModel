package com.centurylink.dsp.valueobjects.impl;

public class SelfJoinedLinkedTable extends LinkedTable {

	private String additionalCondition;
	private String alias;
	
	public SelfJoinedLinkedTable(String parentTable, String parentLinkField, boolean leftOuterJoin, String alias, String additionalCondition)
	{
		super(parentTable, parentTable + " " + alias, parentLinkField, leftOuterJoin);
		this.alias = alias;
		this.additionalCondition = additionalCondition;
	}

	@Override
	public String getWhereClause()
	{
		String where = parentTable + "." + parentLinkField + " = " + alias + "." + childLinkField;
		
		if (additionalCondition != null && !additionalCondition.trim().equals(""))
			where += " AND " + additionalCondition;
		
		return where;
	}
	
}
