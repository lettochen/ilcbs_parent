package cn.itcast.web.action.cargo;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.domain.Contract;
import cn.itcast.domain.ContractProduct;
import cn.itcast.domain.Factory;
import cn.itcast.service.ContractProductService;
import cn.itcast.service.FactoryService;
import cn.itcast.utils.Page;
import cn.itcast.web.action.BaseAction;

@Namespace("/cargo")
@Result(name = "tocreate", location = "contractProductAction_tocreate?contract.id=${contract.id}", type = "redirectAction")
public class ContractProductAction extends BaseAction implements ModelDriven<ContractProduct> {

	private ContractProduct model = new ContractProduct();

	@Override
	public ContractProduct getModel() {
		// TODO Auto-generated method stub
		return model;
	}

	private Page page = new Page();

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Autowired
	private ContractProductService contractProductService;

	@Autowired
	private FactoryService factoryService;

	/**
	 * 去修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractProductAction_toupdate", results = {
			@Result(name = "toupdate", location = "/WEB-INF/pages/cargo/contract/jContractProductUpdate.jsp") })
	public String toupdate() throws Exception {
		// TODO Auto-generated method stub
		ContractProduct contractProduct = contractProductService.get(model.getId());
		super.push(contractProduct);

		// 1.查询factoryList所有的工厂列表，只要货物的
		Specification<Factory> spec = new Specification<Factory>() {

			@Override
			public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("ctype").as(String.class), "货物");
			}
		};
		List<Factory> factoryList = factoryService.find(spec);
		super.put("factoryList", factoryList);

		return "toupdate";
	}

	/**
	 * 修改功能
	 */
	@Action(value = "contractProductAction_update")
	public String update() throws Exception {
		// TODO Auto-generated method stub
		ContractProduct contractProduct = contractProductService.get(model.getId());
		contractProduct.setFactory(model.getFactory());
		contractProduct.setFactoryName(model.getFactoryName());
		contractProduct.setProductNo(model.getProductNo());
		contractProduct.setProductImage(model.getProductImage());
		contractProduct.setCnumber(model.getCnumber());
		contractProduct.setPackingUnit(model.getPackingUnit());
		contractProduct.setLoadingRate(model.getLoadingRate());
		contractProduct.setBoxNum(model.getBoxNum());
		contractProduct.setPrice(model.getPrice());
		contractProduct.setOrderNo(model.getOrderNo());
		contractProduct.setProductDesc(model.getProductDesc());
		contractProduct.setProductRequest(model.getProductRequest());
		
		
		contractProductService.saveOrUpdate(contractProduct);

		return "tocreate";
	}

	/**
	 * 到达新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractProductAction_tocreate", results = {
			@Result(name = "tocreate", location = "/WEB-INF/pages/cargo/contract/jContractProductCreate.jsp") })
	public String tocreate() throws Exception {
		// TODO Auto-generated method stub

		// 1.查询factoryList所有的工厂列表，只要货物的
		Specification<Factory> spec = new Specification<Factory>() {

			@Override
			public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("ctype").as(String.class), "货物");
			}
		};
		List<Factory> factoryList = factoryService.find(spec);
		super.put("factoryList", factoryList);

		// 2.查询同一个购销合同下的所有货物
		Specification<ContractProduct> spec1 = new Specification<ContractProduct>() {

			@Override
			public Predicate toPredicate(Root<ContractProduct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				/*
				 * Join<Contract, ContractProduct> join = root.join("contract");
				 * cb.equal(join.get("id").as(String.class),
				 * model.getContract().getId());
				 */

				return cb.equal(root.get("contract").get("id").as(String.class), model.getContract().getId());

			}
		};

		org.springframework.data.domain.Page<ContractProduct> page2 = contractProductService.findPage(spec1,
				new PageRequest(page.getPageNo() - 1, page.getPageSize()));
		page.setTotalPage(page2.getTotalPages());
		page.setResults(page2.getContent());
		page.setTotalRecord(page2.getTotalElements());
		page.setUrl("contractProductAction_tocreate");

		super.push(page);

		return "tocreate";
	}

	/**
	 * 购销合同新增
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractProductAction_insert")
	public String insert() throws Exception {
		// TODO Auto-generated method stub
		contractProductService.saveOrUpdate(model);
		return "tocreate";
	}

	/**
	 * 购销合同删除
	 */
	@Action(value = "contractProductAction_delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		contractProductService.deleteById(model.getId());
		return "tocreate";
	}

}
