package cn.itcast.web.action.cargo;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
import cn.itcast.domain.ExtCproduct;
import cn.itcast.domain.Factory;
import cn.itcast.service.ExtCproductService;
import cn.itcast.service.FactoryService;
import cn.itcast.utils.Page;
import cn.itcast.web.action.BaseAction;

@Namespace("/cargo")
@Result(name="alist",location="extCproductAction_tocreate?contractProduct.id=${contractProduct.id}",type="redirectAction")
public class ExtCproductAction extends BaseAction implements ModelDriven<ExtCproduct> {

	private ExtCproduct model = new ExtCproduct();

	@Override
	public ExtCproduct getModel() {
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
	private ExtCproductService extCproductService;

	@Autowired
	private FactoryService factoryService;
	
	@Action(value = "extCproductAction_tocreate", results = {@Result(name = "tocreate", location = "/WEB-INF/pages/cargo/contract/jExtCproductCreate.jsp") })
	public String tocreate() throws Exception {
		// TODO Auto-generated method stub

		Specification<ExtCproduct> spec = new Specification<ExtCproduct>() {

			@Override
			public Predicate toPredicate(Root<ExtCproduct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("contractProduct").get("id").as(String.class),
						model.getContractProduct().getId());
			}
		};

		org.springframework.data.domain.Page<ExtCproduct> page2 = extCproductService.findPage(spec, new PageRequest(page.getPageNo() - 1, page.getPageSize()));

		page.setResults(page2.getContent());
		page.setTotalPage(page2.getTotalPages());
		page.setTotalRecord(page2.getTotalElements());
		page.setUrl("extCproductAction_tocreate");
		
		super.push(page);
		
		
		Specification<Factory> spec2 = new Specification<Factory>() {

			@Override
			public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("ctype").as(String.class), "附件");
			}
		};
		
		List<Factory> factoryList = factoryService.find(spec2);
		
		super.put("factoryList", factoryList);
		
		return "tocreate";
	}
	
	@Action(value="extCproductAction_insert")
	public String insert() throws Exception {
		// TODO Auto-generated method stub
		extCproductService.saveOrUpdate(model);
		return "alist";
	}
	
	@Action(value="extCproductAction_toupdate",results={@Result(name="toupdate",location="/WEB-INF/pages/cargo/contract/jExtCproductUpdate.jsp")})
	public String toupdate() throws Exception {
		// TODO Auto-generated method stub
		ExtCproduct extCproduct = extCproductService.get(model.getId());
		super.push(extCproduct);
		
		Specification<Factory> spec2 = new Specification<Factory>() {

			@Override
			public Predicate toPredicate(Root<Factory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("ctype").as(String.class), "附件");
			}
		};
		
		List<Factory> factoryList = factoryService.find(spec2);
		
		super.put("factoryList", factoryList);
		
		return "toupdate";
	}
	
	@Action(value="extCproductAction_update")
	public String update() throws Exception {
		// TODO Auto-generated method stub
		
		ExtCproduct extCproduct = extCproductService.get(model.getId());
		extCproduct.setFactory(model.getFactory());
		extCproduct.setFactoryName(model.getFactoryName());
		extCproduct.setProductNo(model.getProductNo());
		extCproduct.setProductImage(model.getProductImage());
		extCproduct.setCnumber(model.getCnumber());
		extCproduct.setPackingUnit(model.getPackingUnit());
		extCproduct.setPrice(model.getPrice());
		extCproduct.setOrderNo(model.getOrderNo());
		extCproduct.setProductDesc(model.getProductDesc());
		extCproduct.setProductRequest(model.getProductRequest());
		
		extCproductService.saveOrUpdate(extCproduct);
		
		return "alist";
	}
	
	@Action(value="extCproductAction_delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		extCproductService.deleteById(model.getId());
		return "alist";
	}
}
