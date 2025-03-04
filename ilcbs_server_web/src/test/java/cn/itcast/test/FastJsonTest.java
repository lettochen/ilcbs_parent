package cn.itcast.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import cn.itcast.domain.Role;
import cn.itcast.domain.User;

public class FastJsonTest {

	@Test
	public void testOjbectTOjson(){
		User user = new User();
		user.setUserName("cgx");
		user.setPassword("123456");
		String jsonString = JSON.toJSONString(user);
		System.out.println(jsonString);
	}
	
	//{"password":"123456","roles":[],"userName":"cgx"}
	@Test
	public void testMapToJson(){
		HashMap map = new HashMap();
		map.put("userName", "cgx");
		map.put("password", "123456");
		String jsonString = JSON.toJSONString(map);
		System.out.println(jsonString);
	}
	
	@Test
	public void testList(){
		User user = new User();
		user.setUserName("cgx");
		user.setPassword("123456");
		
		User user1 = new User();
		user1.setUserName("zbz");
		user1.setPassword("123456");
		
		
		ArrayList list = new ArrayList();
		list.add(user);
		list.add(user1);
		list.add(user);
		//SerializerFeature.DisableCircularReferenceDetect关闭循环引用
		String jsonString = JSON.toJSONString(list,SerializerFeature.DisableCircularReferenceDetect);
		
		System.out.println(jsonString);
	}
	
	@Test
	public void testList1(){
		
		HashSet<User> users = new HashSet<User>();
		HashSet<Role> roles = new HashSet<Role>();
		
		
		User user = new User();
		user.setUserName("cgx");
		user.setPassword("123456");
		
		users.add(user);
		
		Role role = new Role();
		role.setName("测试人员");
		role.setUsers(users);
		roles.add(role);
		
		user.setRoles(roles);
		//{"password":"123456","roles":[{"modules":[],"name":"测试人员"}],"userName":"cgx"}

		//SerializerFeature.DisableCircularReferenceDetect关闭循环引用
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter("password","roles","name","userName");
		
		String jsonString = JSON.toJSONString(user,filter,SerializerFeature.DisableCircularReferenceDetect);
		
		System.out.println(jsonString);
	}
	
	
}
