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

public class UpdateDao {

	public static <T> Integer update(String xmlName, T obj) {
		
		Integer result = 0;
		
		try {
			
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
			
			xmlName = XmlDBUtil.formatXmlName(xmlName);
			File xml = new File(XmlDBContext.getXmlFilePath(xmlName));
			
			if(!xml.isFile()) {
				return 0;
			}
			
			SAXReader reader = new SAXReader();
	
			Document doc = reader.read(xml);
			Element root = doc.getRootElement();
			
			Element datas = root.element("datas");
			List<Element> dataList = datas.elements("data");
			String key = getKey(root);
			
			for(Element data : dataList) {
				
				if(isKeyMatch(data, key, queryParamVoList)) {

					for(Iterator<?> iterator = data.elementIterator(); iterator.hasNext(); ) {
						Element column = (Element) iterator.next();
						
						String columnName = column.getName();
						
						Field[] fields = clazz.getDeclaredFields();
						for(Field f : fields) {
							if(columnName.equals(f.getName())) {
								Method method = clazz.getMethod("get" + XmlDBUtil.upperFirst(columnName));
								Object valueObj = method.invoke(obj);
								if(valueObj != null) {
									column.setText(valueObj.toString());
								}
							}
						}
					}
					
					result ++;
				}
			}
			
			XmlDBUtil.outPutXmlFile(XmlDBContext.getXmlFilePath(xmlName), doc);
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static String getKey(Element root) throws Exception {
		
		try {
			
			Element property = root.element("property");
			Element key = property.element("key");
			if(XmlDBUtil.isNullOrEmpty(key.getText())) {
				throw new Exception();
			}
			
			return key.getText();
		} catch (Exception e) {
			throw new Exception("Get key fail for update xml!");
		}
	}
	
	public static boolean isKeyMatch(Element e, String key, List<QueryParamVo> queryParamVoList) {
		
		String keyValue = null;
		for(QueryParamVo queryParamVo : queryParamVoList) {
			if(queryParamVo.getName().equals(key)) {
				keyValue = queryParamVo.getValue();
			}
		}
		
		if(keyValue == null) {
			return false;
		}
		
		for(Iterator<?> iterator = e.elementIterator(); iterator.hasNext(); ) {
			Element column = (Element) iterator.next();
			
			String columnName = column.getName();
			String columnVal = column.getText();
			
			if(columnName.equals(key) && columnVal.equals(keyValue)) {
				return true;
			}
		}
		
		return false;
	}
}
