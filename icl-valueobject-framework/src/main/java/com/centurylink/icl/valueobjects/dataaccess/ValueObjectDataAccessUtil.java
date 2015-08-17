package com.centurylink.dsp.valueobjects.dataaccess;

import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import com.centurylink.dsp.valueobjects.exceptions.ValueObjectDataIntegrityException;
import com.centurylink.dsp.valueobjects.impl.MaxSequenceField;
import com.centurylink.dsp.valueobjects.impl.SequenceField;
import com.centurylink.icl.exceptions.ICLException;

public class ValueObjectDataAccessUtil extends SimpleJdbcDaoSupport{
	
	private static final Log LOG = LogFactory.getLog(ValueObjectDataAccessUtil.class);
	private static final String SEQ_SELECT_PRE = "SELECT ";
	private static final String SEQ_SELECT_POST = ".nextval FROM dual";
	private static final String MAX_SEQ_SELECT_PRE = "SELECT MAX(";
	private static final String MAX_SEQ_SELECT_POST = ") FROM ";
	
	private static final String CACHE_MAP = "@MAP@";
	private static final String CACHE_LIST = "@LIST@";
	
	private String classKey = null;
	private CacheManager manager;
	private String cacheName;
	
	public ValueObjectDataAccessUtil(String classKey, String cacheConfig, String cacheName)
	{
		this.classKey = classKey;
		
		manager = CacheManager.create(cacheConfig);
		this.cacheName = cacheName;
	}
	
	public String getClassKey()
	{
		return this.classKey;
	}
	
	public Map<String, Object> getRecordAsMap(String statement)
	{
		return getRecordAsMap(statement, false);
	}
	
	public Map<String, Object> getRecordAsMap(String statement, boolean cacheOveride)
	{
		Map<String, Object> returnMap = null;
		
		if (!cacheOveride)
		{
			Element element = getCache().get(CACHE_MAP + statement);
			if (element != null)
			{
				if (LOG.isDebugEnabled())
					LOG.debug("Found entry in Cache for " + CACHE_MAP + statement);
				
				returnMap = (Map<String, Object>) element.getObjectValue();
			}
		}
		
		if (returnMap == null)
		{
			if (LOG.isDebugEnabled())
				LOG.debug("Either Entry Not Found in Cache or CacheOveride is true for " + CACHE_MAP + statement);
			
			returnMap = getRecordAsMapFromDB(statement);
			if (returnMap != null)
			{
				getCache().put(new Element(CACHE_MAP + statement, returnMap));
			}
		}
		
		return returnMap;
	}
	
	public Map<String, Object> getRecordAsMapFromDB(String statement)
	{
		Map<String, Object> returnMap = null;
		try{
			if (LOG.isDebugEnabled())
				LOG.debug("Before executing statement: " + statement);
			
			returnMap = getSimpleJdbcTemplate().getJdbcOperations().queryForMap(statement);
			
			if (LOG.isDebugEnabled())
				LOG.debug("After executing statement: " + statement);
		}
		catch(EmptyResultDataAccessException edae)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("After executing statement with edae: " + statement);
				LOG.debug("NO Data Found");
			}
		}
		catch(IncorrectResultSizeDataAccessException irsdae)
		{
			if (LOG.isDebugEnabled())
				LOG.debug("After executing statement with irsdae: " + statement);
			
			throw new ValueObjectDataIntegrityException("Multiple Rows returned but only 1 row expected");
		}
		catch(Exception e){
			if (LOG.isDebugEnabled())
				LOG.debug("After executing statement with irsdae: " + e);
			
			//Should throw this but for now I'll leave it to ensure I don't break other things
			e.printStackTrace();
		}
		
		return returnMap;
	}
	
	public List<Map<String,Object>> getRecordsAsList(String statement)
	{
		List<Map<String,Object>> records;
		
		Element element = getCache().get(CACHE_LIST + statement);
		if (element != null)
		{
			if (LOG.isDebugEnabled())
				LOG.debug("Found entry in Cache for " + CACHE_LIST + statement);
			
			records = (List<Map<String,Object>>) element.getObjectValue();
		} else {
			if (LOG.isDebugEnabled())
				LOG.debug("Entry Not Found in Cache for " + CACHE_LIST + statement);
			
			records = getSimpleJdbcTemplate().queryForList(statement);
			if (records != null && records.size()>0)
			{
				if (LOG.isDebugEnabled())
					LOG.debug("Adding Entry in Cache for " + CACHE_LIST + statement);
				
				getCache().put(new Element(CACHE_LIST + statement, records));
			}
		}
		return records;
	}
	
	public long getNextSeq(SequenceField field)
	{
		final long nextSeq = getSimpleJdbcTemplate().getJdbcOperations().queryForLong(SEQ_SELECT_PRE + field.getSeqTableName() + SEQ_SELECT_POST);
		
		return nextSeq;
	}
	
	public long getMaxSeq(MaxSequenceField field)
	{
		final long nextSeq = getSimpleJdbcTemplate().getJdbcOperations().queryForLong(MAX_SEQ_SELECT_PRE + field.getName() + MAX_SEQ_SELECT_POST + field.getMaxSeqTableName());
		
		return (nextSeq + 1);
	}

	public int getIntFromStatement(String statement)
	{
		final int intValue = getSimpleJdbcTemplate().getJdbcOperations().queryForInt(statement);
		
		return intValue;
	}

	public void executeStatement(String statement)
	{
		try{
			getSimpleJdbcTemplate().getJdbcOperations().execute(statement);
		}catch(Exception e){
				//Should throw this but for now I'll leave it to ensure I don't break other things
				throw new ICLException(e.getCause().getMessage());
		}
		
	}
	
	public void clearCache()
	{
		getCache().removeAll();
	}
	
	private Ehcache getCache()
	{
		return manager.getEhcache(cacheName);
	}
}
