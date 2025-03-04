package cn.itcast.web.action.cargo;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.domain.Contract;
import cn.itcast.domain.ContractProduct;
import cn.itcast.domain.User;
import cn.itcast.service.ContractProductService;
import cn.itcast.service.ContractService;
import cn.itcast.utils.Page;
import cn.itcast.utils.SysConstant;
import cn.itcast.web.action.BaseAction;

@Result(name = "alist", location = "contractAction_list", type = "redirectAction")
public class ContractAction extends BaseAction implements ModelDriven<Contract> {

	private Contract model = new Contract();

	@Override
	public Contract getModel() {
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

	/**
	 * 购销合同列表查询
	 */
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private ContractProductService contractProductService;

	@Action(value = "contractAction_list", results = {
			@Result(name = "list", location = "/WEB-INF/pages/cargo/contract/jContractList.jsp") })
	public String list() throws Exception {
		// TODO Auto-generated method stub
		// 从session获取当前用户
		final User user = (User) session.get(SysConstant.CURRENT_USER_INFO);

		Specification<Contract> spec = new Specification<Contract>() {

			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				Predicate p = null;
				switch (user.getUserinfo().getDegree()) {
				case 0:  // 管理员

					break;
				case 1: // 副总

					break;
				case 2:  // 部门总经理
					p = cb.like(root.get("createDept").as(String.class), user.getDept().getId() + "%");
					break;
				case 3:  // 部门经理
					p = cb.equal(root.get("createDept").as(String.class), user.getDept().getId());
					break;
				default: // 4.普通员工
					//select * from Contract_c where createBy = '自己的id'
					p = cb.equal(root.get("createBy").as(String.class), user.getId());
					break;
				}
				return p;
			}
		};

		org.springframework.data.domain.Page<Contract> page2 = contractService.findPage(spec,
				new PageRequest(page.getPageNo() - 1, page.getPageSize()));

		page.setTotalPage(page2.getTotalPages());
		page.setResults(page2.getContent());
		page.setTotalRecord(page2.getTotalElements());
		page.setUrl("contractAction_list");

		super.push(page);

		return "list";
	}

	/**
	 * 查看单个
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_toview", results = {
			@Result(name = "toview", location = "/WEB-INF/pages/cargo/contract/jContractView.jsp") })
	public String toview() throws Exception {
		// TODO Auto-generated method stub
		Contract contract = contractService.get(model.getId());
		super.push(contract);

		return "toview";
	}

	/**
	 * 去修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_toupdate", results = {
			@Result(name = "toupdate", location = "/WEB-INF/pages/cargo/contract/jContractUpdate.jsp") })
	public String toupdate() throws Exception {
		// TODO Auto-generated method stub
		Contract contract = contractService.get(model.getId());
		super.push(contract);

		return "toupdate";
	}

	/**
	 * 修改功能
	 */
	@Action(value = "contractAction_update")
	public String update() throws Exception {
		// TODO Auto-generated method stub
		Contract contract = contractService.get(model.getId());
		contract.setCustomName(model.getCustomName());
		contract.setPrintStyle(model.getPrintStyle());
		contract.setContractNo(model.getContractNo());
		contract.setOfferor(model.getOfferor());
		contract.setInputBy(model.getInputBy());
		contract.setCheckBy(model.getCheckBy());
		contract.setInspector(model.getInspector());
		contract.setSigningDate(model.getSigningDate());
		contract.setImportNum(model.getImportNum());
		contract.setShipTime(model.getShipTime());
		contract.setTradeTerms(model.getTradeTerms());
		contract.setDeliveryPeriod(model.getDeliveryPeriod());
		contract.setCrequest(model.getCrequest());
		contract.setRemark(model.getRemark());

		contractService.saveOrUpdate(contract);

		return "alist";
	}

	/**
	 * 到达新增页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_tocreate", results = {
			@Result(name = "tocreate", location = "/WEB-INF/pages/cargo/contract/jContractCreate.jsp") })
	public String tocreate() throws Exception {
		// TODO Auto-generated method stub
		return "tocreate";
	}

	/**
	 * 购销合同新增
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_insert")
	public String insert() throws Exception {
		// TODO Auto-generated method stub
		// 新增的时候增加createBy
		User user = (User) session.get(SysConstant.CURRENT_USER_INFO);
		model.setCreateBy(user.getId());
		model.setCreateDept(user.getDept().getId());
		contractService.saveOrUpdate(model);
		return "alist";
	}

	/**
	 * 购销合同删除
	 */
	@Action(value = "contractAction_delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		String[] ids = model.getId().split(", ");

		contractService.delete(ids);
		return "alist";
	}

	/**
	 * 修改购销合同状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "contractAction_submit")
	public String submit() throws Exception {
		// TODO Auto-generated method stub
		String[] ids = model.getId().split(", ");
		for (String cid : ids) {
			Contract contract = contractService.get(cid);
			contract.setState(1);
			contractService.saveOrUpdate(contract);
		}

		return "alist";
	}

	@Action(value = "contractAction_cancel")
	public String cancel() throws Exception {
		// TODO Auto-generated method stub
		String[] ids = model.getId().split(", ");
		for (String cid : ids) {
			Contract contract = contractService.get(cid);
			contract.setState(0);
			contractService.saveOrUpdate(contract);
		}

		return "alist";
	}

	@Action(value="contractAction_print")
	public String print() throws Exception {
		// TODO Auto-generated method stub
		Contract contract = contractService.get(model.getId());
		
		HttpServletResponse response = ServletActionContext.getResponse();
		
		ContractPrint contractPrint = new ContractPrint();
		
		String path = ServletActionContext.getServletContext().getRealPath("/");
		
		List<ContractProduct> cplist = contractProductService.findCpList(model.getId()); //根据购销合同货物直接货物按工厂名称排序后的货物列表
		
		contractPrint.print(contract,cplist, path, response);
		
		return NONE;
	}
}
