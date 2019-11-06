package test;

import java.util.List;

import com.paletter.xmldb.context.XmlDBContext;
import com.paletter.xmldb.dao.XmlDBDao;
import com.paletter.xmldb.generator.XmlGenerator;

public class TestMain {

	public static void main(String[] args) {
		
		try {
			
			XmlDBContext.init(System.getProperty("user.dir") +  "/src/test/java/test/");
//			XmlDBContext.init("D:/01.ProgramFiles/XmlDB/resource");
			
//			XmlGenerator.generateXml("session", "session");
			
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
//			us.setName("Angle1哈哈哈哈");
//			us.setAge(1);
//			Integer result = XmlDBDao.insert("user.xml", us);
//			System.out.println(result);
			
//			System.out.println(XmlDBDao.delete("user.xml", "002"));
			
			Session s2 = new Session();
			s2.setSession("shop_spd_ticket-shop-rls");
			s2.setKeyPath("D:/03.WorkFiles/01.个人信息相关/服务器登录私钥");
			XmlDBDao.insert("session.xml", s2);
			
			Session s = XmlDBDao.queryByKey("session.xml", "shop_spd_ticket-shop-rls", Session.class);
			
//			List<User> userList = XmlDBDao.queryAll("user.xml", User.class);
//			
//			for(User u : userList) {
//				System.out.println(u.getId());
//				System.out.println(u.getName());
//				System.out.println(u.getAge());
//			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
