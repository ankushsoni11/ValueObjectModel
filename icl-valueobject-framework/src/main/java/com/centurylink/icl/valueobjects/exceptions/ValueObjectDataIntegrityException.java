package com.centurylink.dsp.valueobjects.exceptions;

import com.centurylink.icl.exceptions.ICLException;

public class ValueObjectDataIntegrityException extends ICLException {
	
	private static final long serialVersionUID = 1L;
	private static final String CODE = "3001";
	private static final String TEXT = "ValueObjectDataIntegrityException";
	private static final String MESSAGE = "Exception Occured due to Data Integrity constraint in DB";


	public ValueObjectDataIntegrityException()
	{
		super(MESSAGE, TEXT, CODE);
	}

	public ValueObjectDataIntegrityException(String message)
	{
		super(message, TEXT, CODE);
	}
}
