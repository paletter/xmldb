package com.paletter.xmldb.generator;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.paletter.xmldb.context.XmlDBContext;
import com.paletter.xmldb.util.XmlDBUtil;

public class XmlGenerator {

	public static File generateXml(String xmlName) {
		
		try {
			
			xmlName = XmlDBUtil.formatXmlName(xmlName);
			File xml = new File(XmlDBContext.getXmlFilePath(xmlName));
			
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("xmldb");
			
			Element property = root.addElement("property");
			property.setText("");
			
			Element datas = root.addElement("datas");
			datas.setText("");
			
			XmlDBUtil.outPutXmlFile(XmlDBContext.getXmlFilePath(xmlName), doc);
			
			return xml;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
