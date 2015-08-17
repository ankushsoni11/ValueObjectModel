package com.centurylink.dsp.querybuilder.translator;

import java.util.List;
import java.util.Map;

import com.centurylink.dsp.querybuilder.interfaces.VariableAndValueProcessor;
import com.centurylink.dsp.querybuilder.interfaces.VariableValueTranslator;

public class MultipleFieldTranslator implements VariableAndValueProcessor {

	private List<String> qualifiedFields;
	private  VariableValueTranslator valueTranslator;
	private Map<String, List<String>> valueList;
	
	public MultipleFieldTranslator(List<String> qualifiedFields,  VariableValueTranslator valueTranslator)
	{
		this.qualifiedFields = qualifiedFields;
		this.valueTranslator = valueTranslator;
	}
	
	public MultipleFieldTranslator(List<String> qualifiedFields, Map<String, List<String>> valueList, VariableValueTranslator valueTranslator)
       {
              this.qualifiedFields = qualifiedFields;
              this.valueTranslator = valueTranslator;
              this.valueList=valueList;
       }
	   
	@Override
	public String buildWhereStatement(String varialbe, String operator,	String value) {

		 String statement = "(";
              
              String conjunction = "";
              

              if(valueList!=null)
              {
                     for(String qualifiedField:qualifiedFields)
                     {
                           String comma = "";
                           if (valueList.containsKey(value.toUpperCase()))
                           {
                                  statement += conjunction+qualifiedField + " IN (";
                                  for (String inValue:valueList.get(value.toUpperCase()))
                                  {
                                         statement += comma + "'" + inValue + "'";
                                         comma = ", ";
                                  }
                                  statement += ")";
                                  conjunction = " OR ";
                                  
                           } 
                     }
                     statement += ")";
              }
              else 
              {
              for (String qualifiedField:qualifiedFields)
              {
                     statement += conjunction +  qualifiedField + " " + operator + " '" + valueTranslator.translateValue(value) + "'";
                     conjunction = " OR ";
              }
              
              statement += ")";

       }
              return statement;
	}
}
