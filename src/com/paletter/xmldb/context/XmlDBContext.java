package com.paletter.xmldb.context;

import com.paletter.xmldb.util.XmlDBUtil;

public class XmlDBContext {

	private static String xmlPath;

	public static void init(String xmlPath) {
		if(!xmlPath.endsWith("/")) {
			xmlPath = xmlPath + "/";
		}
		
		XmlDBContext.xmlPath = xmlPath;
	}

	public static String getXmlPath() {
		return xmlPath;
	}
	
	public static String getXmlFilePath(String xmlName) throws Exception {
		if(XmlDBUtil.isNullOrEmpty(xmlPath)) {
			throw new Exception("Base xmlPath is empty or null");
		}
		return xmlPath + xmlName;
	}
}
