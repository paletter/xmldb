package com.paletter.xmldb.context;

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
	
	public static String getXmlFilePath(String xmlName) {
		return xmlPath + xmlName;
	}
}
