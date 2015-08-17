package com.centurylink.dsp.querybuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;

import com.centurylink.dsp.querybuilder.QueryBuilder;
import com.centurylink.dsp.querybuilder.interfaces.VariableAndValueProcessor;
import com.centurylink.dsp.querybuilder.translator.MultipleFieldTranslator;
import com.centurylink.dsp.querybuilder.translator.ValueListTranslator;
import com.centurylink.dsp.querybuilder.translator.ValueTranslatorOnly;
import com.centurylink.dsp.querybuilder.translator.value.ConvertToUpperCase;
import com.centurylink.dsp.querybuilder.translator.value.ValueMapper;
import com.iclnbi.iclnbiV200.Condition;
import com.iclnbi.iclnbiV200.CreateCircuitRequestDocument;
import com.iclnbi.iclnbiV200.SearchResourceDetails;
import com.iclnbi.iclnbiV200.SearchResourceRequestDocument;
import com.iclnbi.iclnbiV200.ValidationCondition;
import com.iclnbi.iclnbiV200.ValidationRule;

public class QueryBuilderSimpleTest {
	
	@Test
	public void testQuery()
	{
		
		SearchResourceDetails srd = SearchResourceDetails.Factory.newInstance();
		
		srd.getFilterCriteriaList();

		Condition c1 = Condition.Factory.newInstance();
		c1.setVariableName("TESTNAME1");
		c1.setValue("testvalue1");

		Condition c2 = Condition.Factory.newInstance();
		c2.setVariableName("TESTNAME2");
		c2.setValue("TESTVALUE2A");
		
		Condition c3 = Condition.Factory.newInstance();
		c3.setVariableName("TESTNAME3");
		c3.setValue("TESTVALUE3B");
		
		Condition c4 = Condition.Factory.newInstance();
		c4.setVariableName("TESTNAME4");
		c4.setValue("TestValue4");
		
		ValidationRule vr = ValidationRule.Factory.newInstance();
		
		ValidationCondition vc = ValidationCondition.Factory.newInstance();
		
		vc.getEqualConditionList().add(c1);
		vc.getEqualConditionList().add(c2);
		vc.getEqualConditionList().add(c3);
		vc.getEqualConditionList().add(c4);
		
		vr.getValidationConditionList().add(vc);

		srd.getFilterCriteriaList().add(vr);

		System.out.println(srd.toString());
		
		Map<String, String> translate = new HashMap<String, String>();
		translate.put("TESTNAME1", "TABLE1.TESTNAME1");
		translate.put("TESTNAME2", "TABLE2.TESTNAME2");
		
		Map<String, VariableAndValueProcessor> variableAndValueProcessorMap = new HashMap<String, VariableAndValueProcessor>();
		variableAndValueProcessorMap.put("TESTNAME1", new ValueTranslatorOnly("TABLE1.TESTNAME1", new ConvertToUpperCase()));
		
		Map<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("TESTVALUE2A", "VALUEA");
		valueMap.put("TESTVALUE2B", "VALUEB");
		
		variableAndValueProcessorMap.put("TESTNAME2", new ValueTranslatorOnly("TABLE1.TESTNAME2", new ValueMapper(valueMap)));
		
		Map<String, List<String>> valueList = new HashMap<String, List<String>>();
		valueList.put("TESTVALUE3A", Arrays.asList("VALUE1","VALUE2","VALUE3"));
		valueList.put("TESTVALUE3B", Arrays.asList("VALUE4","VALUE5","VALUE6"));
		
		variableAndValueProcessorMap.put("TESTNAME3", new ValueListTranslator("TABLE1.TESTNAME3", valueList));

		variableAndValueProcessorMap.put("TESTNAME4", new MultipleFieldTranslator(Arrays.asList("TABLE1.FIELD1","TABLE1.FIELD2","TABLE1.FIELD3"), new ConvertToUpperCase()));
		
		QueryBuilder qb = new QueryBuilder(variableAndValueProcessorMap);
		
		System.out.println(qb.buildQuery(srd.getFilterCriteriaList().get(0)));
	}
	
	@Test
	public void SmartInnerConditionTest() throws Exception
	{
		SearchResourceRequestDocument requestDocument = SearchResourceRequestDocument.Factory.parse(new File("src/test/resources/SmartInnerConditionRequest.xml"));
	}

}
