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
import cn.itcast.dao.ExtCproductDao;
import cn.itcast.domain.Contract;
import cn.itcast.domain.ExtCproduct;
import cn.itcast.service.ExtCproductService;
import cn.itcast.utils.UtilFuns;

@Service
public class ExtCproductServiceImpl implements ExtCproductService{

	@Autowired
	private ExtCproductDao extCproductDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Override
	public List<ExtCproduct> find(Specification<ExtCproduct> spec) {
		// TODO Auto-generated method stub
		return extCproductDao.findAll(spec);
	}

	@Override
	public ExtCproduct get(String id) {
		// TODO Auto-generated method stub
		return extCproductDao.findOne(id);
	}

	@Override
	public Page<ExtCproduct> findPage(Specification<ExtCproduct> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return extCproductDao.findAll(spec, pageable);
	}

	@Override
	public void saveOrUpdate(ExtCproduct entity) {
		// TODO Auto-generated method stub
		if(UtilFuns.isEmpty(entity.getId())){ // 判断修改或者新增
			// 附件分散计算
			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getCnumber()) && UtilFuns.isNotEmpty(entity.getPrice())){
				amount = entity.getCnumber() * entity.getPrice();
			}
			// 附件的总金额
			entity.setAmount(amount);
			
			// 获取购销合同对象
			Contract contract = contractDao.findOne(entity.getContractProduct().getContract().getId());
			contract.setTotalAmount(contract.getTotalAmount() + amount); //分散计算的新增
			contractDao.save(contract);
		}else{
			Double oldAmount = entity.getAmount();  // 保存旧的总金额
			
			Double amount = 0.0;
			if(UtilFuns.isNotEmpty(entity.getCnumber()) && UtilFuns.isNotEmpty(entity.getPrice())){
				amount = entity.getCnumber() * entity.getPrice();
			}
			// 附件的总金额
			if(oldAmount != amount){
				entity.setAmount(amount);  //修改后的总金额设置到附件上
				Contract contract = contractDao.findOne(entity.getContractProduct().getContract().getId());
				contract.setTotalAmount(contract.getTotalAmount() + amount - oldAmount); //修改的差值
				contractDao.save(contract);
			}
		}
		
		extCproductDao.save(entity);
	}

	@Override
	public void saveOrUpdateAll(Collection<ExtCproduct> entitys) {
		// TODO Auto-generated method stub
		extCproductDao.save(entitys);
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub
		// 分散计算
		ExtCproduct cp = extCproductDao.findOne(id);
		Contract contract = cp.getContractProduct().getContract();
		contract.setTotalAmount(contract.getTotalAmount() - cp.getAmount()); //购销合同总金额减去附近总金额
		
		
		contractDao.save(contract);
		
		extCproductDao.delete(id);
	}

	@Override
	public void delete(String[] ids) {
		// TODO Auto-generated method stub
		for (String id : ids) {
			extCproductDao.delete(id);
		}
	}

}
