package com.paletter.xmldb.dao;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;

import com.paletter.xmldb.context.XmlDBContext;
import com.paletter.xmldb.util.XmlDBUtil;
import com.paletter.xmldb.vo.QueryParamVo;

public class QueryDao {

	private static <T> List<QueryParamVo> transferClazzToParam(T obj) throws Exception {

		List<QueryParamVo> queryParamVoList = new ArrayList<QueryParamVo>();
		
		Class<? extends Object> clazz = obj.getClass();
		
		Field[] properties = clazz.getDeclaredFields();
		for(Field pro : properties) {
			String proName = pro.getName();
			
			Method method = clazz.getMethod("get" + XmlDBUtil.upperFirst(proName));
			Object valueObj = method.invoke(obj);
			if(valueObj != null) {
				QueryParamVo queryParamVo = new QueryParamVo();
				queryParamVo.setName(proName);
				queryParamVo.setValue(valueObj.toString());
				queryParamVoList.add(queryParamVo);
			}
		}
		
		return queryParamVoList;
	}
	
	public static <T> T queryByKey(String xmlName, T obj, Class<T> entityClazz) {

		if(obj == null) {
			return null;
		}
		
		T result = null;
		
		try {
			
			List<QueryParamVo> queryParamVoList = transferClazzToParam(obj);
			
			if(queryParamVoList.size() == 0) {
				return null;
			}
			
			if(queryParamVoList.size() > 0) {
			
				xmlName = XmlDBUtil.formatXmlName(xmlName);
				File xml = new File(XmlDBContext.getXmlPath() + xmlName);
				
				if(!xml.isFile()) {
					return null;
				}
				
				List<Element> dataList = XmlDBUtil.getDataElementList(xml);
				for(Element data : dataList) {
					
					if(XmlDBUtil.isQueryParamMatch(data, queryParamVoList)) {
	
						result = entityClazz.newInstance();
						
						for(Iterator<?> iterator = data.elementIterator(); iterator.hasNext(); ) {
							Element column = (Element) iterator.next();
							
							String columnName = column.getName();
							String columnVal = column.getText();
							if(XmlDBUtil.isNotNullOrEmpty(columnVal)) {
								
								Class<?> propertyTypeClass = XmlDBUtil.getPropertyTypeClass(entityClazz, column.getName());
								Method setMethod = entityClazz.getMethod("set" + XmlDBUtil.upperFirst(columnName), propertyTypeClass);
								
								if(propertyTypeClass.getName().equals("int") || propertyTypeClass.getName().equals(Integer.class.getName())) {
									setMethod.invoke(result, Integer.valueOf(columnVal));
								} else {
									setMethod.invoke(result, columnVal);
								}
							}
						}
					}
				}
			}
			
			return result;
		
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}
	
	public static <T> List<T> query(String xmlName, T obj, Class<T> entityClazz) {

		if(obj == null) {
			return queryAll(xmlName, entityClazz);
		}
		
		List<T> resultList = new ArrayList<T>();
		
		try {
			
			List<QueryParamVo> queryParamVoList = transferClazzToParam(obj);
			
			if(queryParamVoList.size() == 0) {
				return queryAll(xmlName, entityClazz);
			}
			
			if(queryParamVoList.size() > 0) {
			
				xmlName = XmlDBUtil.formatXmlName(xmlName);
				File xml = new File(XmlDBContext.getXmlPath() + xmlName);
				
				if(!xml.isFile()) {
					return resultList;
				}
				
				List<Element> dataList = XmlDBUtil.getDataElementList(xml);
				String key = XmlDBUtil.getKey(xml);
				for(Element data : dataList) {
					
					if(XmlDBUtil.isKeyMatch(data, key, queryParamVoList)) {
	
						T result = entityClazz.newInstance();
						
						for(Iterator<?> iterator = data.elementIterator(); iterator.hasNext(); ) {
							Element column = (Element) iterator.next();
							
							String columnName = column.getName();
							String columnVal = column.getText();
							if(XmlDBUtil.isNotNullOrEmpty(columnVal)) {
								
								Class<?> propertyTypeClass = XmlDBUtil.getPropertyTypeClass(entityClazz, column.getName());
								Method setMethod = entityClazz.getMethod("set" + XmlDBUtil.upperFirst(columnName), propertyTypeClass);
								
								if(propertyTypeClass.getName().equals("int") || propertyTypeClass.getName().equals(Integer.class.getName())) {
									setMethod.invoke(result, Integer.valueOf(columnVal));
								} else {
									setMethod.invoke(result, columnVal);
								}
							}
						}
						
						resultList.add(result);
					}
				}
			}
			
			return resultList;
		
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
	}

	public static <T> List<T> queryAll(String xmlName, Class<T> entityClazz) {

		List<T> resultList = new ArrayList<T>();
		
		try {
			
			xmlName = XmlDBUtil.formatXmlName(xmlName);
			File xml = new File(XmlDBContext.getXmlFilePath(xmlName));
			
			if(!xml.isFile()) {
				return resultList;
			}
			
			List<Element> dataList = XmlDBUtil.getDataElementList(xml);
			for(Element data : dataList) {
				
				T result = entityClazz.newInstance();
				
				for(Iterator<?> iterator = data.elementIterator(); iterator.hasNext(); ) {
					Element column = (Element) iterator.next();
					
					String columnName = column.getName();
					String columnVal = column.getText();
					if(XmlDBUtil.isNotNullOrEmpty(columnVal)) {
						
						Class<?> propertyTypeClass = XmlDBUtil.getPropertyTypeClass(entityClazz, column.getName());
						Method setMethod = entityClazz.getMethod("set" + XmlDBUtil.upperFirst(columnName), propertyTypeClass);
						
						if(propertyTypeClass.getName().equals("int") || propertyTypeClass.getName().equals(Integer.class.getName())) {
							setMethod.invoke(result, Integer.valueOf(columnVal));
						} else {
							setMethod.invoke(result, columnVal);
						}
					}
				}
				
				resultList.add(result);
			}
			
			return resultList;
		
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
	}

}
