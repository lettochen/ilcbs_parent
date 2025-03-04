package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.domain.Export;

public interface ExportDao extends JpaRepository<Export, String>,JpaSpecificationExecutor<Export>{


}
