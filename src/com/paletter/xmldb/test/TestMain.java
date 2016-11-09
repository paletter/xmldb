package com.paletter.xmldb.test;

import java.util.List;

import com.paletter.xmldb.context.XmlDBContext;
import com.paletter.xmldb.dao.XmlDBDao;
import com.paletter.xmldb.generator.XmlGenerator;

public class TestMain {

	public static void main(String[] args) {
		
		XmlDBContext.init(System.getProperty("user.dir") +  "/src/com/paletter/xmldb/test/");
		
//		XmlGenerator.generateXml("test.xml");
		
//		User upd = new User();
//		upd.setId("001");
//		upd.setName("Angle");
//		Integer result = XmlDBDao.update("user.xml", upd);
//		System.out.println(result);
		
//		User user = new User();
//		user.setId("001");
//		List<User> userList = BaseDao.query("user.xml", user, User.class);
		
//		User us = new User();
//		us.setId("003");
//		us.setName("Angle3");
//		us.setAge(1);
//		Integer result = XmlDBDao.insert("user.xml", us);
//		System.out.println(result);
		
		List<User> userList = XmlDBDao.queryAll("user.xml", User.class);
		
		for(User u : userList) {
			System.out.println(u.getId());
			System.out.println(u.getName());
			System.out.println(u.getAge());
		}
	}
}
