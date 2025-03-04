package cn.itcast.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.domain.Dept;

public interface DeptService {
	//查询所有，带条件查询
	public List<Dept> find(Specification<Dept> spec);
	//获取一条记录
	public Dept get(String id);
	//分页查询，将数据封装到一个page分页工具类对象
	public  Page<Dept> findPage(Specification<Dept> spec, Pageable pageable);
	
	//新增和修改保存
	public  void saveOrUpdate(Dept entity);
	//批量新增和修改保存
	public  void saveOrUpdateAll(Collection<Dept> entitys);
	
	//单条删除，按id
	public  void deleteById( String id);
	//批量删除
	public  void delete(String[] ids);
}
