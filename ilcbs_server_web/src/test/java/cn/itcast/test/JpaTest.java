package cn.itcast.test;

import java.util.List;

import org.aspectj.weaver.reflect.DeferredResolvedPointcutDefinition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.dao.DeptDao;
import cn.itcast.domain.Dept;
import cn.itcast.service.DeptService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class JpaTest {

	@Autowired
	private DeptDao deptDao;
	
	@Autowired
	private DeptService deptService;
	
	@Test
	public void testFindDept(){
		Dept dept = deptDao.findOne("100");
		System.out.println(dept.getDeptName());
	}
	
	@Test
	public void testAddDept(){
		Dept dept = new Dept();
		dept.setDeptName("测试部门");
		dept.setState(1);
		
		deptService.saveOrUpdate(dept);
		System.out.println("新增完成");
	}
	
	@Test
	public void testUpdateDept(){
		Dept dept = new Dept();
		dept.setId("11111");  //根据id数据库查询对象，查到更新所有属性，没查到根据本次提交的属性进行新增
		dept.setDeptName("测试3");
		
		
		deptService.saveOrUpdate(dept);
	}
	
	@Test
	public void testDeleteDept(){
		deptService.deleteById("8a7e863b60fe47c60160fe47ceb90000");
	}
	
	@Test
	public void testFindDeptByName(){
		List<Dept> findDeptByName = deptDao.findDeptByName("测试3");
		for (Dept dept : findDeptByName) {
			System.out.println(dept.getDeptName());
		}
		
	}
	
	@Test
	public void testFindDept2(){
		// 命名规范
/*		List<Dept> deptList = deptDao.findDeptByState(1);
		for (Dept dept : deptList) {
			System.out.println(dept);
		}*/
		
/*		List<Dept> deptList = deptDao.findDeptByDeptNameLike("%部%");
		for (Dept dept : deptList) {
			System.out.println(dept);
		}*/
		
		// 原生sql语句
		List<Dept> deptList = deptDao.findNameAndState("测试部门-4", 1);
		for (Dept dept : deptList) {
			System.out.println(dept.toString());
		}
	}
	
}
