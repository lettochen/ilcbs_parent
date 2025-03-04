package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.domain.ExportProduct;

public interface ExportProductDao extends JpaRepository<ExportProduct, String>,JpaSpecificationExecutor<ExportProduct>{


}
