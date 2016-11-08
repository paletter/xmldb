package com.paletter.xmldb.dao;

import java.util.List;

public class XmlDBDao {

	public static <T> List<T> queryAll(String xmlName, Class<T> entityClazz) {
		return QueryDao.queryAll(xmlName, entityClazz);
	}
	
	public static <T> List<T> query(String xmlName, T obj, Class<T> entityClazz) {
		return QueryDao.query(xmlName, obj, entityClazz);
	}
	
	public static <T> Integer update(String xmlName, T obj) {
		return UpdateDao.update(xmlName, obj);
	}
	
	public static <T> Integer insert(String xmlName, T obj) {
		return InsertDao.insert(xmlName, obj);
	}
}
