package cn.itcast.web.action.sysadmin;

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

import cn.itcast.domain.Dept;
import cn.itcast.service.DeptService;
import cn.itcast.utils.Page;
import cn.itcast.web.action.BaseAction;

@Namespace("/sysadmin")
@Result(name = "alist", type = "redirectAction", location = "deptAction_list")
public class DeptAction extends BaseAction implements ModelDriven<Dept> {

	@Autowired
	private DeptService deptService;

	private Dept model = new Dept();

	@Override
	public Dept getModel() {
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

	@Action(value = "deptAction_list", results = {
			@Result(name = "list", location = "/WEB-INF/pages/sysadmin/dept/jDeptList.jsp") })
	public String list() throws Exception {
		// TODO Auto-generated method stub

		org.springframework.data.domain.Page<Dept> page2 = deptService.findPage(null,
				new PageRequest(page.getPageNo() - 1, page.getPageSize()));

		page.setTotalPage(page2.getTotalPages()); // 总页数
		page.setTotalRecord(page2.getTotalElements()); // 总记录数
		page.setResults(page2.getContent()); // 查询的记录
		page.setUrl("deptAction_list"); // 上下页跳转的地址(相对路径地址)

		super.push(page);

		return "list";
	}

	/**
	 * 查看单个部门
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "deptAction_toview", results = {
			@Result(name = "toview", location = "/WEB-INF/pages/sysadmin/dept/jDeptView.jsp") })
	public String toview() throws Exception {
		// TODO Auto-generated method stub
		// 先根据id从数据库中查询
		Dept dept = deptService.get(model.getId());

		super.push(dept);

		return "toview";
	}

	/**
	 * 去新增页面
	 */
	@Action(value = "deptAction_tocreate", results = {
			@Result(name = "tocreate", location = "/WEB-INF/pages/sysadmin/dept/jDeptCreate.jsp") })
	public String tocreate() throws Exception {
		// TODO Auto-generated method stub

		// deptList,0停用的就不要了
		Specification<Dept> spec = new Specification<Dept>() {

			// root:取当前对象的属性.as(属性中的类型) query：order，group cb:or and equles from
			// Dept where state = 1
			@Override
			public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};

		List<Dept> deptList = deptService.find(spec);

		super.put("deptList", deptList);

		return "tocreate";
	}

	/**
	 * 新增方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "deptAction_insert")
	public String insert() throws Exception {
		// TODO Auto-generated method stub
		deptService.saveOrUpdate(model); // 查部门的默认状态

		return "alist";
	}

	/**
	 * 去修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "deptAction_toupdate", results = {
			@Result(name = "toupdate", location = "/WEB-INF/pages/sysadmin/dept/jDeptUpdate.jsp") })
	public String toupdate() throws Exception {
		// TODO Auto-generated method stub
		// 1.根据id查询部门对象，压入值栈中
		Dept dept = deptService.get(model.getId());

		super.push(dept);

		// 2.查询部门列表，压入值栈中
		Specification<Dept> spec = new Specification<Dept>() {

			// root:取当前对象的属性.as(属性中的类型) query：order，group cb:or and equles from
			// Dept where state = 1
			@Override
			public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};
		
		List<Dept> deptList = deptService.find(spec);
		
		//deptList中有当前部门，要去掉
		deptList.remove(dept);
		
		super.put("deptList", deptList);

		return "toupdate";
	}
	
	
	/**
	 * 部门修改方法
	 * @return
	 * @throws Exception
	 */
	@Action(value="deptAction_update")
	public String update() throws Exception {
		// TODO Auto-generated method stub
		// 根据id查询数据库的对象
		Dept dept = deptService.get(model.getId());
		dept.setDeptName(model.getDeptName());
		dept.setParent(model.getParent());
		
		deptService.saveOrUpdate(dept);
		
		return "alist";
	}
	
	/**
	 * 部门删除
	 */
	@Action(value="deptAction_delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(model.getId());
		String[] ids = model.getId().split(", ");  //通过逗号空格切割成string数组
		
		deptService.delete(ids);
		
		return "alist";
	}
}
