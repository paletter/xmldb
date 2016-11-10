package com.paletter.xmldb.dao;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
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
			String keyName = XmlDBUtil.getKey(root);
			
			for(Element data : dataList) {
				
				if(XmlDBUtil.isKeyMatch(data, keyName, queryParamVoList)) {

					Field[] fields = clazz.getDeclaredFields();
					
					List<Field> tmpFieldListToRemove = new ArrayList<Field>();
					Collections.addAll(tmpFieldListToRemove, fields);
					
					for(Iterator<?> iterator = data.elementIterator(); iterator.hasNext(); ) {
						Element column = (Element) iterator.next();
						String columnName = column.getName();
						
						for(Field f : fields) {
							if(columnName.equals(f.getName())) {
								
								tmpFieldListToRemove.remove(f);
								
								try {
									Method method = clazz.getMethod("get" + XmlDBUtil.upperFirst(columnName));
									Object valueObj = method.invoke(obj);
									if(valueObj != null) {
										column.setText(valueObj.toString());
									}
								} catch (Exception e) {
									
								}
							}
						}
					}
					
					if(tmpFieldListToRemove.size() > 0) {
						for(Field f : tmpFieldListToRemove) {
							try {
								Method method = clazz.getMethod("get" + XmlDBUtil.upperFirst(f.getName()));
								Object valueObj = method.invoke(obj);
								Element column = data.addElement(f.getName());
								if(valueObj != null) {
									column.setText(valueObj.toString());
								} else {
									column.setText("");
								}
							} catch (Exception e) {
								
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
	
}
