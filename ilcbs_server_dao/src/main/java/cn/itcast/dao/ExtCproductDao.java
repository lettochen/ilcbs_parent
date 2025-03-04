package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.domain.ExtCproduct;

public interface ExtCproductDao extends JpaRepository<ExtCproduct, String>,JpaSpecificationExecutor<ExtCproduct>{

	
}
