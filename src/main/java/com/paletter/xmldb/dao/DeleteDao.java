package com.paletter.xmldb.dao;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.paletter.xmldb.context.XmlDBContext;
import com.paletter.xmldb.util.XmlDBUtil;

public class DeleteDao {

	public static <T> Integer delete(String xmlName, String keyValue) {

		Integer result = 0;
		
		try {
			
			xmlName = XmlDBUtil.formatXmlName(xmlName);
			File xml = new File(XmlDBContext.getXmlFilePath(xmlName));
			
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
					datas.remove(data);
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
}
