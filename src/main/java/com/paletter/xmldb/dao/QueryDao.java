package com.paletter.xmldb.dao;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
			try {
				Method method = clazz.getMethod("get" + XmlDBUtil.upperFirst(proName));
				Object valueObj = method.invoke(obj);
				if(valueObj != null) {
					QueryParamVo queryParamVo = new QueryParamVo();
					queryParamVo.setName(proName);
					queryParamVo.setValue(valueObj.toString());
					queryParamVoList.add(queryParamVo);
				}
			} catch (Exception e) {
				
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
				
				SAXReader reader = new SAXReader();
		
				Document doc = reader.read(xml);
				Element root = doc.getRootElement();
				
				Element datas = root.element("datas");
				List<Element> dataList = datas.elements("data");
				String keyName = XmlDBUtil.getKey(root);
				
				for(Element data : dataList) {
					
					if(XmlDBUtil.isKeyMatch(data, keyName, queryParamVoList)) {
	
						result = entityClazz.newInstance();
						
						for(Iterator<?> iterator = data.elementIterator(); iterator.hasNext(); ) {
							Element column = (Element) iterator.next();
							
							String columnName = column.getName();
							String columnVal = column.getText();
							if(XmlDBUtil.isNotNullOrEmpty(columnVal)) {
								
								Class<?> propertyTypeClass = XmlDBUtil.getPropertyTypeClass(entityClazz, column.getName());
								try {
									Method setMethod = entityClazz.getMethod("set" + XmlDBUtil.upperFirst(columnName), propertyTypeClass);
									
									if(propertyTypeClass.getName().equals("int") || propertyTypeClass.getName().equals(Integer.class.getName())) {
										setMethod.invoke(result, Integer.valueOf(columnVal));
									} else if(propertyTypeClass.getName().equals("double") || propertyTypeClass.getName().equals(Double.class.getName())) {
										setMethod.invoke(result, Double.valueOf(columnVal));
									} else {
										setMethod.invoke(result, columnVal);
									}
								} catch (Exception e) {
									
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
	
	public static <T> T queryByKey(String xmlName, String keyValue, Class<T> entityClazz) {
		
		if(keyValue == null) {
			return null;
		}
		
		T result = null;
		
		try {
				
			xmlName = XmlDBUtil.formatXmlName(xmlName);
			File xml = new File(XmlDBContext.getXmlPath() + xmlName);
			
			if(!xml.isFile()) {
				return null;
			}
			
			SAXReader reader = new SAXReader();
			
			Document doc = reader.read(xml);
			Element root = doc.getRootElement();
			
			Element datas = root.element("datas");
			List<Element> dataList = datas.elements("data");
			String keyName = XmlDBUtil.getKey(root);
			
			for(Element data : dataList) {
				
				if(XmlDBUtil.isKeyMatch(data, keyName, keyValue)) {
					
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
				String keyName = XmlDBUtil.getKey(xml);
				for(Element data : dataList) {
					
					if(XmlDBUtil.isKeyMatch(data, keyName, queryParamVoList)) {
	
						T result = entityClazz.newInstance();
						
						for(Iterator<?> iterator = data.elementIterator(); iterator.hasNext(); ) {
							Element column = (Element) iterator.next();
							
							String columnName = column.getName();
							String columnVal = column.getText();
							if(XmlDBUtil.isNotNullOrEmpty(columnVal)) {
								
								Class<?> propertyTypeClass = XmlDBUtil.getPropertyTypeClass(entityClazz, column.getName());
								try {
									Method setMethod = entityClazz.getMethod("set" + XmlDBUtil.upperFirst(columnName), propertyTypeClass);
									
									if(propertyTypeClass.getName().equals("int") || propertyTypeClass.getName().equals(Integer.class.getName())) {
										setMethod.invoke(result, Integer.valueOf(columnVal));
									} else if(propertyTypeClass.getName().equals("double") || propertyTypeClass.getName().equals(Double.class.getName())) {
										setMethod.invoke(result, Double.valueOf(columnVal));
									} else {
										setMethod.invoke(result, columnVal);
									}
								} catch (Exception e) {
									
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
						try {
							Method setMethod = entityClazz.getMethod("set" + XmlDBUtil.upperFirst(columnName), propertyTypeClass);
							
							if(propertyTypeClass.getName().equals("int") || propertyTypeClass.getName().equals(Integer.class.getName())) {
								setMethod.invoke(result, Integer.valueOf(columnVal));
							} else if(propertyTypeClass.getName().equals("double") || propertyTypeClass.getName().equals(Double.class.getName())) {
								setMethod.invoke(result, Double.valueOf(columnVal));
							} else {
								setMethod.invoke(result, columnVal);
							}
						} catch (Exception e) {
							
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
