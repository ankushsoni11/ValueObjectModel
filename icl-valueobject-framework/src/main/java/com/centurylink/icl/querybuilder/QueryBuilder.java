package com.centurylink.dsp.querybuilder;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centurylink.dsp.querybuilder.interfaces.VariableAndValueProcessor;
import com.centurylink.icl.common.util.StringHelper;
import com.centurylink.icl.exceptions.ICLRequestValidationException;
import com.iclnbi.iclnbiV200.ValidationCondition;
import com.iclnbi.iclnbiV200.Condition;
import com.iclnbi.iclnbiV200.ValidationRule;

public class QueryBuilder {
	
	private Map<String, VariableAndValueProcessor> variableAndValueProcessorMap;
	
	private static final Log LOG = LogFactory.getLog(QueryBuilder.class);
	
	public QueryBuilder(Map<String, VariableAndValueProcessor> variableAndValueProcessorMap)
	{
		this.variableAndValueProcessorMap = variableAndValueProcessorMap;
	}
	
	public void setVariableAndValueProcessorMap(Map<String, VariableAndValueProcessor> variableAndValueProcessorMap)
	{
		this.variableAndValueProcessorMap = variableAndValueProcessorMap;
	}
	
	public String buildQuery(ValidationRule rule)
	{
		String query = "1=1 ";
		
		if (null == rule)
		{
			throw new ICLRequestValidationException("No Filter Rules passed in on request");
		}
		
		query += " AND (" + processValidationConditionList(rule.getValidationConditionList(), "AND", false) + ")";
		
		LOG.info("buildQuery response: " + query);
		
		return query;
	}
	
	private String processValidationConditionList(List<ValidationCondition> conditions, String conjunctionIn, boolean innerCondition)
	{
		String query = "";
		boolean firstTime = true;
		String conjunction = "";
		
		if (innerCondition)
		{
			firstTime = false;
			conjunction = " " + conjunctionIn + " ";
		}
		
		if (null == conditions || conditions.size() < 1)
		{
			throw new ICLRequestValidationException("Atleast one ValidationCondition must exist on a FilterCriteria Element");
		}
		
		for (ValidationCondition validationCondition:conditions)
		{
			query += conjunction + "(" + addCondition(validationCondition) + ")";
			if (firstTime)
			{
				firstTime = false;
				conjunction = " " + conjunctionIn + " ";
			}
		}
		
		return query;
	}
	
	private String addCondition(ValidationCondition validationCondition)
	{
		String validationConditionString = "";
		boolean multiple = false;
		
		String conjunction = StringHelper.isEmpty(validationCondition.getOperator())?"AND":validationCondition.getOperator();
		
		if (validationCondition.getEqualConditionList().size() < 1 &&
			validationCondition.getLikeConditionList().size() < 1)
		{
			throw new ICLRequestValidationException("Atleast one condition must be present in a ValidationCondition Block");
		}
		
		if (validationCondition.getEqualConditionList().size() > 0)
		{
			validationConditionString += processCondition(validationCondition.getEqualConditionList(), "=", conjunction, multiple);
			multiple = true;
		}
		
		if (validationCondition.getLikeConditionList().size() > 0)
		{
			validationConditionString += processCondition(validationCondition.getLikeConditionList(), "LIKE", conjunction, multiple);
			multiple = true;
		}
		
		if (validationCondition.getNotEqualConditionList().size() > 0)
		{
			//validationConditionString += processCondition(validationCondition.getLikeConditionList(), "!=", conjunction, multiple);
			//multiple = true;
		}

		if (validationCondition.getInnerConditionList().size() > 0)
			validationConditionString += processValidationConditionList(validationCondition.getInnerConditionList(), conjunction, true);
		
		return validationConditionString;
	}
	
	private String processCondition(List<Condition> conditions, String operator, String conjunctionIn, boolean multiple)
	{
		String conditionString = "";
		boolean firstTime = true;
		String conjunction = "";
		
		if (multiple)
		{
			conjunction = " " + conjunctionIn + " ";
		}
		
		for (Condition condition:conditions)
		{
			if (StringHelper.isEmpty(condition.getVariableName()) || StringHelper.isEmpty(condition.getValue()))
			{
				throw new ICLRequestValidationException("Both VariableName and Value are required for a Filter Condition");
			}
			
			String currentCondition = buildConditionStatement(operator, condition.getVariableName().toUpperCase(), condition.getValue(), condition.getOtherValue());
			if (!StringHelper.isEmpty(currentCondition))
			{
				conditionString += conjunction + currentCondition;
				if (firstTime)
				{
					firstTime = false; 
					conjunction = " " + conjunctionIn + " ";
				}
			}
		}
		
		return conditionString;
	}
	
	private String buildConditionStatement(String operator, String fieldName, String fieldValue, String otherFieldValue)
	{
		
		if (variableAndValueProcessorMap.containsKey(fieldName))
			return variableAndValueProcessorMap.get(fieldName).buildWhereStatement(fieldName, operator, fieldValue);
		else
			throw new ICLRequestValidationException("Field " + fieldName + " not defined");
		
	}
	
}
