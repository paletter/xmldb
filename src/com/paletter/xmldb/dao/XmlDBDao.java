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
	
	public static <T> Integer update(String xmlName, T obj) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		return UpdateDao.update(xmlName, obj);
	}
	
	public static <T> Integer insert(String xmlName, T obj) {
		xmlName = XmlDBUtil.formatXmlName(xmlName);
		return InsertDao.insert(xmlName, obj);
	}
}
