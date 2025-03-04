package cn.itcast.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.domain.User;

public interface UserDao extends JpaRepository<User, String>,JpaSpecificationExecutor<User>{

	@Query(value="select a1,nvl(p_count,0) from (select to_char(login_time,'HH24') p_time,count(*) p_count from login_log_p group by to_char(login_time,'HH24')) p,online_info_t where p.p_time(+) = online_info_t.a1 order by a1",nativeQuery=true)
	public List<Object[]> getLoginData();
}
