package com.centurylink.dsp.valueobjects.model;

public interface ValueObject extends Table {
	
	abstract void attachType(Type type);
	
	abstract void attachExtension(Extension extension);

}
