package cn.itcast.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.itcast.dao.ModuleDao;
import cn.itcast.domain.Module;
import cn.itcast.service.ModuleService;
import cn.itcast.utils.UtilFuns;

@Service
public class ModuleServiceImpl implements ModuleService{

	@Autowired
	private ModuleDao moduleDao;
	
	@Override
	public List<Module> find(Specification<Module> spec) {
		// TODO Auto-generated method stub
		return moduleDao.findAll(spec);
	}

	@Override
	public Module get(String id) {
		// TODO Auto-generated method stub
		return moduleDao.findOne(id);
	}

	@Override
	public Page<Module> findPage(Specification<Module> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return moduleDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(Module entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			
		}else{
			
		}
		
		moduleDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<Module> entitys) {
		// TODO Auto-generated method stub
		moduleDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		moduleDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			moduleDao.delete(id);
		}
	}

}
