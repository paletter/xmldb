package com.paletter.xmldb.test;

import java.util.List;

import com.paletter.xmldb.context.XmlDBContext;
import com.paletter.xmldb.dao.XmlDBDao;
import com.paletter.xmldb.generator.XmlGenerator;

public class TestMain {

	public static void main(String[] args) {
		
		try {
			
			XmlDBContext.init(System.getProperty("user.dir") +  "/src/com/paletter/xmldb/test/");
			
//			XmlGenerator.generateXml("user", "name");
			
			User upd = new User();
			upd.setId("002");
			upd.setName("Angle1");
			Integer result = XmlDBDao.update("user.xml", upd);
			System.out.println(result);
			
	//		User user = new User();
	//		user.setId("001");
	//		List<User> userList = BaseDao.query("user.xml", user, User.class);
			
//			User us = new User();
//			us.setId("001");
//			us.setName("Angle1");
//			us.setAge(1);
//			Integer result = XmlDBDao.insert("user.xml", us);
//			System.out.println(result);
			
			List<User> userList = XmlDBDao.queryAll("user.xml", User.class);
			
			for(User u : userList) {
				System.out.println(u.getId());
				System.out.println(u.getName());
				System.out.println(u.getAge());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
