package com.centurylink.dsp.valueobjects.model;

import java.io.Serializable;
import java.util.HashMap;

import com.centurylink.dsp.valueobjects.impl.Field;

public interface Table extends Serializable {

	void populateModel();
	
	public Object getField(String key);
	
	public String getFieldAsString(String key);
	
	public void setField(String key, Object value);
	
	public String getModelAsString();
	
	public HashMap<String, Field> getFields();

}
