package com.paletter.xmldb.util;

import java.io.PrintWriter;
import java.lang.reflect.Field;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class CommonUtil {

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
		
		PrintWriter pw = new PrintWriter(filePath);
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
}
