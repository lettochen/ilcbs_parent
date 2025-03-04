package cn.itcast.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.domain.Dept;

public interface DeptDao extends JpaRepository<Dept, String>,JpaSpecificationExecutor<Dept>{

	//JPQL的方式
	@Query("from Dept where deptName = ?1")  //jpql
  	public List<Dept> findDeptByName(String name);
	
	//符合命名规范的   findDeptByState
	public List<Dept> findDeptByState(Integer state);
	
	//from Dept where deptName like (?)
	public List<Dept> findDeptByDeptNameLike(String name);
	
	//sql原生语句
	@Query(value="select * from dept_p where dept_Name = ?1 and state = ?2",nativeQuery=true)
	public List<Dept> findNameAndState(String name,Integer state);
}
