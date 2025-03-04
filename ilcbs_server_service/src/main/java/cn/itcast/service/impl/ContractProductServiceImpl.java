package cn.itcast.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.itcast.dao.ContractDao;
import cn.itcast.dao.ContractProductDao;
import cn.itcast.domain.Contract;
import cn.itcast.domain.ContractProduct;
import cn.itcast.domain.ExtCproduct;
import cn.itcast.service.ContractProductService;
import cn.itcast.utils.UtilFuns;

@Service
public class ContractProductServiceImpl implements ContractProductService{

	@Autowired
	private ContractProductDao contractProductDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Override
	public List<ContractProduct> find(Specification<ContractProduct> spec) {
		// TODO Auto-generated method stub
		return contractProductDao.findAll(spec);
	}

	@Override
	public ContractProduct get(String id) {
		// TODO Auto-generated method stub
		return contractProductDao.findOne(id);
	}

	@Override
	public Page<ContractProduct> findPage(Specification<ContractProduct> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return contractProductDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(ContractProduct entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			//分散计算
			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getCnumber()) && UtilFuns.isNotEmpty(entity.getPrice())){
				amount = entity.getCnumber() * entity.getPrice();
			}
			// 货物的总金额
			entity.setAmount(amount);
			
			// 获取购销合同对象
			Contract contract = contractDao.findOne(entity.getContract().getId());
			contract.setTotalAmount(contract.getTotalAmount() + amount); //分散计算的新增
			contractDao.save(contract);
		}else{
			Double oldAmount = entity.getAmount();  // 保存旧的总金额
			
			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getCnumber()) && UtilFuns.isNotEmpty(entity.getPrice())){
				amount = entity.getCnumber() * entity.getPrice();
			}
			// 货物的总金额
			if(oldAmount != amount){
				entity.setAmount(amount);  //修改后的总金额设置到商品上
				Contract contract = contractDao.findOne(entity.getContract().getId());
				contract.setTotalAmount(contract.getTotalAmount() + amount - oldAmount); //修改的差值
				contractDao.save(contract);
			}
		}
		
		contractProductDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<ContractProduct> entitys) {
		// TODO Auto-generated method stub
		contractProductDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		// 分散计算
		ContractProduct cp = contractProductDao.findOne(id);
		Contract contract = cp.getContract();
		contract.setTotalAmount(contract.getTotalAmount() - cp.getAmount()); //购销合同总金额减去货物总金额
		
		Set<ExtCproduct> extCproducts = cp.getExtCproducts(); //循环货物下的所有附件
		for (ExtCproduct extCproduct : extCproducts) {
			contract.setTotalAmount(contract.getTotalAmount() - extCproduct.getAmount()); //购销合同总金额减去附件总金额
		}
		
		contractDao.save(contract);
		
		contractProductDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			contractProductDao.delete(id);
		}
	}

	@Override
	public List<ContractProduct> findCpByShipTime(String shipTime) {
		// TODO Auto-generated method stub
		return contractProductDao.findCpByShipTime(shipTime);
	}

	@Override
	public List<ContractProduct> findCpList(String contracid) {
		// TODO Auto-generated method stub
		return contractProductDao.findCpList(contracid);
	}

}
