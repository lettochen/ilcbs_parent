package cn.itcast.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.itcast.dao.UserDao;
import cn.itcast.domain.User;
import cn.itcast.service.UserService;
import cn.itcast.utils.MailUtil;
import cn.itcast.utils.SysConstant;
import cn.itcast.utils.UtilFuns;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Override
	public List<User> find(Specification<User> spec) {
		// TODO Auto-generated method stub
		return userDao.findAll(spec);
	}

	@Override
	public User get(String id) {
		// TODO Auto-generated method stub
		return userDao.findOne(id);
	}

	@Override
	public Page<User> findPage(Specification<User> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return userDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(User entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			 // user和userinfo的主键一致  为了一对一主键关联
			String uid = UUID.randomUUID().toString();
			
			entity.setId(uid);
			entity.getUserinfo().setId(uid);
			
			
			// 准备用户md5密码
			Md5Hash hash = new Md5Hash(SysConstant.DEFAULT_PASS, entity.getUserName(), 2);
			entity.setPassword(hash.toString());
			// 发送欢迎邮件
			String email = entity.getUserinfo().getEmail();
			String subject = "itcast297欢迎您";
			String content = "您的密码是：" + SysConstant.DEFAULT_PASS;
			
			try {
				MailUtil.sendMsg(email, subject, content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else{
			
		}
		userDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<User> entitys) {
		// TODO Auto-generated method stub
		userDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		userDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			userDao.delete(id);
		}
	}

	@Override
	public List<Object[]> getLoginData() {
		// TODO Auto-generated method stub
		return userDao.getLoginData();
	}

}
