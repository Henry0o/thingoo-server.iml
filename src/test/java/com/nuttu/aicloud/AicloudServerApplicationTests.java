//package com.nuttu.aicloud;
//
//import com.nuttu.aicloud.model.user.Role;
//import com.nuttu.aicloud.model.user.User;
//import com.nuttu.aicloud.repository.UserRepository;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import StringParse.util.UUID;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class AicloudServerApplicationTests {

//	@Autowired
//	private UserRepository userRepository;

//	@Test
//	public void contextLoads() {
//	}
//
//	@Test
//	public void test() throws Exception {
//		userRepository.save(new User(UUID.randomUUID().toString().replace("-", ""),"demo@nuttu.com", "demo", "demo", Role.USER, ""));
////		userRepository.save(new User(UUID.randomUUID().toString().replace("-", ""),"admin@nuttu.com", "admin", "admin", Role.ADMIN, ""));
//
//		User user = userRepository.findOneByEmail("demo@nuttu.com").orElse(null);
//
//		Assert.assertNotNull(user);
//
//		Assert.assertEquals("demo", user.getName());
//
////		userRepository.delete(user);
//
//
//	}
//}
