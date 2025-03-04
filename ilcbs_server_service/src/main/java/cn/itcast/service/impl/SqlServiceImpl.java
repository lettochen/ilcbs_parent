package cn.itcast.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.common.dao.SqlDao;
import cn.itcast.service.SqlService;

@Service
public class SqlServiceImpl implements SqlService {

	@Autowired
	private SqlDao sqlDao;

	public String getSingleValue(String sql) {
		return sqlDao.getSingleValue(sql);
	}

	public String getSingleValue(String sql, Object[] objs) {
		return sqlDao.getSingleValue(sql, objs);
	}

	public String[] toArray(String sql) {
		return sqlDao.toArray(sql);
	}

	public List executeSQL(String sql) {
		return sqlDao.executeSQL(sql);
	}

	public List executeSQL(String sql, Object[] objs) {
		return sqlDao.executeSQL(sql, objs);
	}

	public List executeSQLForList(String sql, Object[] objs) {
		return sqlDao.executeSQLForList(sql, objs);
	}

	public int updateSQL(String sql) {
		return sqlDao.updateSQL(sql);
	}

	public int updateSQL(String sql, Object[] objs) {
		return sqlDao.updateSQL(sql, objs);
	}
	
	public int[] batchSQL(String[] sql) {
		return sqlDao.batchSQL(sql);
	}

}
