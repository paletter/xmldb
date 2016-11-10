package test;

import java.util.List;

import com.paletter.xmldb.context.XmlDBContext;
import com.paletter.xmldb.dao.XmlDBDao;
import com.paletter.xmldb.generator.XmlGenerator;

public class TestMain {

	public static void main(String[] args) {
		
		try {
			
			XmlDBContext.init(System.getProperty("user.dir") +  "/src/main/java/com/paletter/xmldb/test/");
			
//			XmlGenerator.generateXml("user", "name");
			
//			User upd = new User();
//			upd.setId("003");
//			upd.setName("Angle2");
//			upd.setIq("100");
//			Integer result = XmlDBDao.update("user.xml", upd);
//			System.out.println(result);
			
//			User user = new User();
//			user.setId("003");
//			List<User> userList = XmlDBDao.query("user.xml", user, User.class);
			
//			User us = new User();
//			us.setId("001");
//			us.setName("Angle1");
//			us.setAge(1);
//			Integer result = XmlDBDao.insert("user.xml", us);
//			System.out.println(result);
			
//			System.out.println(XmlDBDao.delete("user.xml", "002"));
			
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
