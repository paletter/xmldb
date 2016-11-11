package com.paletter.xmldb.util;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.paletter.xmldb.context.XmlDBContext;
import com.paletter.xmldb.vo.QueryParamVo;

public class XmlDBUtil {

	public static String formatXmlName(String xmlName) {
		if(xmlName.indexOf(".xml") <= 0 && xmlName.length() > 0) {
			xmlName = xmlName + ".xml";
		}
		return xmlName;
	}
	
	public static String upperFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	public static Class<?> getPropertyTypeClass(Class<? extends Object> clazz, String name) {
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields) {
			if(f.getName().equals(name)) {
				return f.getType();
			}
		}
		return null;
	}
	
	public static void outPutXmlFile(String filePath, Document doc) throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setNewlines(true);
		
		PrintWriter pw = new PrintWriter(filePath, "UTF-8");
		XMLWriter xw = new XMLWriter(pw, format);
		xw.write(doc);
		xw.flush();
		xw.close();
	}
	
	public static boolean isNullOrEmpty(String str) {
		return str == null || str.trim().equals("");
	}
	
	public static boolean isNotNullOrEmpty(String str) {
		return str != null && !str.trim().equals("");
	}
	
	public static boolean isXmlExist(String xmlName) throws Exception {
		File xml = new File(XmlDBContext.getXmlFilePath(xmlName));
		return xml.isFile();
	}

	public static String getKey(File xml) throws Exception {
		try {
			
			SAXReader reader = new SAXReader();
			Document doc = reader.read(xml);
			Element root = doc.getRootElement();
			
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
	
	public static boolean isKeyMatch(Element e, String keyName, List<QueryParamVo> queryParamVoList) {
		String keyValue = null;
		for(QueryParamVo queryParamVo : queryParamVoList) {
			if(queryParamVo.getName().equals(keyName)) {
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
			
			if(columnName.equals(keyName) && columnVal.equals(keyValue)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isKeyMatch(Element e, String keyName, String keyValue) {
		if(keyValue == null) {
			return false;
		}
		
		for(Iterator<?> iterator = e.elementIterator(); iterator.hasNext(); ) {
			Element column = (Element) iterator.next();
			
			String columnName = column.getName();
			String columnVal = column.getText();
			
			if(columnName.equals(keyName) && columnVal.equals(keyValue)) {
				return true;
			}
		}
		
		return false;
	}

	public static boolean isQueryParamMatch(Element e, List<QueryParamVo> queryParamVoList) {
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
	
	public static List<Element> getDataElementList(File xml) throws Exception {
		SAXReader reader = new SAXReader();

		Document doc = reader.read(xml);
		Element root = doc.getRootElement();
		
		Element datas = root.element("datas");
		List<Element> dataList = datas.elements("data");
		
		return dataList;
	}
}
