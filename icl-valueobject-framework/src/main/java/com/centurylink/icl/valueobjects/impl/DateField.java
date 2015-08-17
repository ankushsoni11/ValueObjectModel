package com.centurylink.dsp.valueobjects.impl;


/*
 * Currently this class is created so it can be expanded on by SystemDate but
 * should be built out in the future to handle Date formats both in and out
 */
public class DateField extends Field {
	
	public DateField(String name)
	{
		super(name,"DATE");
	}
	
	public DateField(String name, boolean changeable)
	{
		super(name, "DATA", changeable);
	}

}
