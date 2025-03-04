package cn.itcast.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.itcast.dao.DeptDao;
import cn.itcast.domain.Dept;
import cn.itcast.service.DeptService;
import cn.itcast.utils.UtilFuns;

@Service
public class DeptServiceImpl implements DeptService{

	@Autowired
	private DeptDao deptDao;
	
	@Override
	public List<Dept> find(Specification<Dept> spec) {
		// TODO Auto-generated method stub
		return deptDao.findAll(spec);
	}

	@Override
	public Dept get(String id) {
		// TODO Auto-generated method stub
		return deptDao.findOne(id);
	}

	@Override
	public Page<Dept> findPage(Specification<Dept> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return deptDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Dept entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			entity.setState(1);  //如果是新增的部门状态设置为1 0:停用  1：启用
		}else{
			
		}
		
		deptDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<Dept> entitys) {
		// TODO Auto-generated method stub
		deptDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		deptDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			deptDao.delete(id);
		}
	}

}
