package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.domain.Role;

public interface RoleDao extends JpaRepository<Role, String>,JpaSpecificationExecutor<Role>{

}
