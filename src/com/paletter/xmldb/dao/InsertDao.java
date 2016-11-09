package com.paletter.xmldb.dao;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.paletter.xmldb.context.XmlDBContext;
import com.paletter.xmldb.util.CommonUtil;

public class InsertDao {

	public static <T> Integer insert(String xmlName, T obj) {
		
		Integer result = 0;
		
		try {
			
			SAXReader reader = new SAXReader();
			xmlName = CommonUtil.formatXmlName(xmlName);
			File xml = new File(XmlDBContext.getXmlFilePath(xmlName));
	
			Document doc = reader.read(xml);
			Element root = doc.getRootElement();

			Element datas = root.element("datas");
			Element newData = datas.addElement("data");
			
			result ++;
			
			Class<? extends Object> clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields) {
				
				Element column = newData.addElement(f.getName());
				
				Method method = clazz.getMethod("get" + CommonUtil.upperFirst(f.getName()));
				Object valueObj = method.invoke(obj);
				if(valueObj != null) {
					column.setText(valueObj.toString());
				} else {
					column.setText("");
				}
			}
			
			CommonUtil.outPutXmlFile(XmlDBContext.getXmlFilePath(xmlName), doc);
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
