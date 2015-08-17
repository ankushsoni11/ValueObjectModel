package com.centurylink.dsp.valueobjects.impl;

import java.util.ArrayList;
import java.util.List;


public class CompositePrimaryKeyField extends PrimaryKey {
	
	private List<Field> fields;
	
	public CompositePrimaryKeyField(List<Field> fields)
	{
		super();
		this.fields = fields;
	}
	
	public CompositePrimaryKeyField()
	{
		fields = new ArrayList<Field>();
	}
	
	public void addField(Field field)
	{
		fields.add(field);
	}

	@Override
	public String getQueryValue()
	{
		String whereClause = "";
		boolean firstTime = true;
		
		for (Field field:fields)
		{
			if (firstTime)
				firstTime = false; 
			else 
				whereClause += " AND ";
				
			whereClause += field.getName() + " = " + field.getQueryValue();
		}
		
		return whereClause;
	}
	
	@Override
	public String toString()
	{
		String stringValue = "PrimaryKey [";
		boolean firstTime = true;
		
		for (Field field:fields)
		{
			if (firstTime)
				firstTime = false; 
			else 
				stringValue += ",";
				
			stringValue += "{" + field.getName() + ", " + field.getQueryValue() + "}";
		}
		
		stringValue += "]";
		return stringValue;
	}

}
