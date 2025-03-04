package cn.itcast.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.domain.Contract;

public interface ContractDao extends JpaRepository<Contract, String>,JpaSpecificationExecutor<Contract>{


}
