package cn.itcast.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.domain.ContractProduct;

public interface ContractProductDao extends JpaRepository<ContractProduct, String>,JpaSpecificationExecutor<ContractProduct>{

	@Query(value="from ContractProduct where to_char(contract.shipTime,'yyyy-MM') = ?1")
	public List<ContractProduct> findCpByShipTime(String shipTime);
	
	//根据购销合同id集合一次性查询购销合同货物
	@Query(value="from ContractProduct where contract.id in (?1)")
	public List<ContractProduct> findCpByContractIds(String[] ids);
	
	@Query(value="from ContractProduct where contract.id = ?1 order by factoryName")
	public List<ContractProduct> findCpList(String contracid);
 }
