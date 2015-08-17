package com.centurylink.dsp.valueobjects.abs;

import com.centurylink.dsp.valueobjects.impl.Field;
import com.centurylink.dsp.valueobjects.impl.SequenceField;
import com.centurylink.dsp.valueobjects.model.ReferenceDataTable;

public abstract class AbstractReferenceDataTable extends AbstractReadWriteTable implements ReferenceDataTable {

	private static final long serialVersionUID = 1L;
	
	protected String objectIdField;
	protected String codeField;
	protected String descriptionField;
	
	public AbstractReferenceDataTable()
	{
		super();
	}

	public String getObjectId() {
		if(null == objectIdField)
			return null;
		else
			return getFieldAsString(objectIdField);
	}

	public String getCode() {
		if(null == codeField)
			return null;
		else
			return getFieldAsString(codeField);
	}

	public String getDescription() {
		if(null == descriptionField)
			return null;
		else
			return getFieldAsString(descriptionField);
	}
	
	public String getCodeFieldName()
	{
		return codeField;
	}
	
	public String getDescriptionFieldName()
	{
		return descriptionField;
	}
	
	public String getObjectIdFieldName()
	{
		return objectIdField;
	}
	public void populateReferenceDataModel(String iobjectIdField, String icodeField, String idescriptionField)
	{
		this.populateReferenceDataModel(iobjectIdField, "NUMBER", icodeField, "STRING", idescriptionField, "STRING");
	}
	
	public void populateReferenceDataModel(String iobjectIdField, String iobjectIdFieldType, String icodeField, String idescriptionField)
	{
		this.populateReferenceDataModelUsingSequence(iobjectIdField, iobjectIdFieldType, icodeField, "STRING", idescriptionField, "STRING");
	}
	
	public void populateReferenceDataModel(String iobjectIdField, String objectIdFieldType, String icodeField, String codeFieldType,  String idescriptionField, String descriptionFieldType)
	{
		objectIdField = iobjectIdField;
		codeField = icodeField;
		descriptionField = idescriptionField;
		
		if(null != objectIdField)
			fields.put(objectIdField, new Field(objectIdField, objectIdFieldType));
		if(null != codeField)
			fields.put(codeField, new Field(codeField, codeFieldType));
		if(null != descriptionField)
			fields.put(descriptionField, new Field(descriptionField, descriptionFieldType));
	}
	
	public void populateReferenceDataModelUsingSequence(String iobjectIdField, String objectIdFieldType, String icodeField, String codeFieldType,  String idescriptionField, String descriptionFieldType)
	{
		objectIdField = iobjectIdField;
		codeField = icodeField;
		descriptionField = idescriptionField;
		
		if(null != objectIdField)
			fields.put(objectIdField, new SequenceField(objectIdField, objectIdFieldType));
		if(null != codeField)
			fields.put(codeField, new Field(codeField, codeFieldType));
		if(null != descriptionField)
			fields.put(descriptionField, new Field(descriptionField, descriptionFieldType));
	}
	

	
	public void save() throws Exception {
		if (!this.instanciated)
		{
			this.doInsert();
		}else {
			this.doUpdate();
		}
	}
}