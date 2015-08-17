package com.centurylink.dsp.valueobjects.impl;

public class PrimaryKey {

	private Field field;
	
	public PrimaryKey()
	{
	}
	
	public PrimaryKey(Field field)
	{
		this.field = field;
	}
	
	public String getName()
	{
		return this.field.getName();
	}
	
	public void setValue(Object value)
	{
		field.setValue(value);
	}
	
	public String getQueryValue()
	{
		return field.getName() + " = " + field.getQueryValue();
	}
	
	@Override
	public String toString()
	{
		return "PrimaryKey [" + field.getName() + ", " + field.getValueAsString() + "]";
	}
}
