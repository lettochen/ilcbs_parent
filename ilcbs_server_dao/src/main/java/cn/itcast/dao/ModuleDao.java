package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.domain.Module;

public interface ModuleDao extends JpaRepository<Module, String>,JpaSpecificationExecutor<Module>{

}
