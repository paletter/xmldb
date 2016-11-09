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
import com.paletter.xmldb.util.CommonUtil;
import com.paletter.xmldb.vo.QueryParamVo;

public class QueryDao {

	public static <T> List<T> query(String xmlName, T obj, Class<T> entityClazz) {

		if(obj == null) {
			return queryAll(xmlName, entityClazz);
		}
		
		List<T> resultList = new ArrayList<T>();
		
		try {
			
			List<QueryParamVo> queryParamVoList = new ArrayList<QueryParamVo>();
			
			Class<? extends Object> clazz = obj.getClass();
			
			Field[] properties = clazz.getDeclaredFields();
			for(Field pro : properties) {
				String proName = pro.getName();
				
				Method method = clazz.getMethod("get" + CommonUtil.upperFirst(proName));
				Object valueObj = method.invoke(obj);
				if(valueObj != null) {
					QueryParamVo queryParamVo = new QueryParamVo();
					queryParamVo.setName(proName);
					queryParamVo.setValue(valueObj.toString());
					queryParamVoList.add(queryParamVo);
				}
			}
			
			if(queryParamVoList.size() == 0) {
				return queryAll(xmlName, entityClazz);
			}
			
			if(queryParamVoList.size() > 0) {
			
				SAXReader reader = new SAXReader();
				xmlName = CommonUtil.formatXmlName(xmlName);
				File xml = new File(XmlDBContext.getXmlPath() + xmlName);
		
				Document doc = reader.read(xml);
				Element root = doc.getRootElement();
				
				Element datas = root.element("datas");
				List<Element> dataList = datas.elements("data");
				
				for(Element data : dataList) {
					
					if(isMatch(data, queryParamVoList)) {
	
						T result = entityClazz.newInstance();
						
						for(Iterator<?> iterator = data.elementIterator(); iterator.hasNext(); ) {
							Element column = (Element) iterator.next();
							
							String columnName = column.getName();
							String columnVal = column.getText();
							if(CommonUtil.isNotNullOrEmpty(columnVal)) {
								
								Class<?> propertyTypeClass = CommonUtil.getPropertyTypeClass(entityClazz, column.getName());
								Method setMethod = entityClazz.getMethod("set" + CommonUtil.upperFirst(columnName), propertyTypeClass);
								
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
			
			SAXReader reader = new SAXReader();
			xmlName = CommonUtil.formatXmlName(xmlName);
			File xml = new File(XmlDBContext.getXmlFilePath(xmlName));
	
			Document doc = reader.read(xml);
			Element root = doc.getRootElement();
			
			Element datas = root.element("datas");
			List<Element> dataList = datas.elements("data");
			
			for(Element data : dataList) {
				
				T result = entityClazz.newInstance();
				
				for(Iterator<?> iterator = data.elementIterator(); iterator.hasNext(); ) {
					Element column = (Element) iterator.next();
					
					String columnName = column.getName();
					String columnVal = column.getText();
					if(CommonUtil.isNotNullOrEmpty(columnVal)) {
						
						Class<?> propertyTypeClass = CommonUtil.getPropertyTypeClass(entityClazz, column.getName());
						Method setMethod = entityClazz.getMethod("set" + CommonUtil.upperFirst(columnName), propertyTypeClass);
						
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

	public static boolean isMatch(Element e, List<QueryParamVo> queryParamVoList) {
		
		for(Iterator<?> iterator = e.elementIterator(); iterator.hasNext(); ) {
			Element column = (Element) iterator.next();
			
			String columnName = column.getName();
			String columnVal = column.getText();
			
			for(QueryParamVo queryParamVo : queryParamVoList) {
				if(queryParamVo.getName().equals(columnName)) {
					if(!columnVal.equals(queryParamVo.getValue())) {
						return false;
					}
				}
			}
		}
		
		return true;
	}
}
