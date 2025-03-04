package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.domain.ExtEproduct;

public interface ExtEproductDao extends JpaRepository<ExtEproduct, String>,JpaSpecificationExecutor<ExtEproduct>{


}
