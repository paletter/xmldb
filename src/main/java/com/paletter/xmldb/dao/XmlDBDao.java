package com.paletter.xmldb.dao;

import java.util.List;

import com.paletter.xmldb.util.XmlDBUtil;

public class XmlDBDao {

	public static <T> List<T> queryAll(String xmlName, Class<T> entityClazz) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		return QueryDao.queryAll(xmlName, entityClazz);
	}
	
	public static <T> List<T> query(String xmlName, T obj, Class<T> entityClazz) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		return QueryDao.query(xmlName, obj, entityClazz);
	}
	
	public static <T> T queryByKey(String xmlName, T obj, Class<T> entityClazz) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		return QueryDao.queryByKey(xmlName, obj, entityClazz);
	}
	
	public static <T> T queryByKey(String xmlName, String keyValue, Class<T> entityClazz) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		return QueryDao.queryByKey(xmlName, keyValue, entityClazz);
	}
	
	public static <T> Integer update(String xmlName, T obj) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		return UpdateDao.update(xmlName, obj);
	}
	
	public static <T> Integer insert(String xmlName, T obj) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		return InsertDao.insert(xmlName, obj);
	}
	
	public static <T> Integer save(String xmlName, T obj, Class<T> entityClazz) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		T queryResult = queryByKey(xmlName, obj, entityClazz);
		
		Integer result = 0;
		
		if(queryResult == null) {
			result = insert(xmlName, obj);
		}
		
		if(queryResult != null) {
			result = update(xmlName, obj);
		}
		
		return result;
	}
	
	public static Integer delete(String xmlName, String keyValue) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		return DeleteDao.delete(xmlName, keyValue);
	}
}
