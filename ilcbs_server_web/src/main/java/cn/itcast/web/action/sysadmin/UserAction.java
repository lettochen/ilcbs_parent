package cn.itcast.web.action.sysadmin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import cn.itcast.domain.Role;
import cn.itcast.domain.User;
import cn.itcast.service.DeptService;
import cn.itcast.service.RoleService;
import cn.itcast.service.UserService;
import cn.itcast.utils.Page;
import cn.itcast.web.action.BaseAction;

@Namespace("/sysadmin")
@Result(name = "alist", type = "redirectAction", location = "userAction_list")
public class UserAction extends BaseAction implements ModelDriven<User> {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DeptService deptService;

	private User model = new User();

	private String[] roleIds;
	
	public String[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	@Override
	public User getModel() {
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

	@Action(value = "userAction_list", results = {
			@Result(name = "list", location = "/WEB-INF/pages/sysadmin/user/jUserList.jsp") })
	public String list() throws Exception {
		// TODO Auto-generated method stub

		org.springframework.data.domain.Page<User> page2 = userService.findPage(null,
				new PageRequest(page.getPageNo() - 1, page.getPageSize()));

		page.setTotalPage(page2.getTotalPages()); // 总页数
		page.setTotalRecord(page2.getTotalElements()); // 总记录数
		page.setResults(page2.getContent()); // 查询的记录
		page.setUrl("userAction_list"); // 上下页跳转的地址(相对路径地址)

		super.push(page);

		return "list";
	}

	/**
	 * 查看单个用户
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "userAction_toview", results = {
			@Result(name = "toview", location = "/WEB-INF/pages/sysadmin/user/jUserView.jsp") })
	public String toview() throws Exception {
		// TODO Auto-generated method stub
		// 先根据id从数据库中查询
		User user = userService.get(model.getId());

		super.push(user);

		return "toview";
	}

	/**
	 * 去新增页面
	 */
	@Action(value = "userAction_tocreate", results = {
			@Result(name = "tocreate", location = "/WEB-INF/pages/sysadmin/user/jUserCreate.jsp") })
	public String tocreate() throws Exception {
		// TODO Auto-generated method stub

		// userList,0停用的就不要了，该用户的直属领导的userList
		Specification<User> spec = new Specification<User>() {

			// root:取当前对象的属性.as(属性中的类型) query：order，group cb:or and equles from
			// User where state = 1
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};

		List<User> userList = userService.find(spec);

		super.put("userList", userList);

		// deptList 该用户所在部门的部门列表
		Specification<Dept> deptSpec = new Specification<Dept>() {

			@Override
			public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};
		
		List<Dept> deptList = deptService.find(deptSpec);
		
		super.put("deptList", deptList);
		
		return "tocreate";
	}

	/**
	 * 新增方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "userAction_insert")
	public String insert() throws Exception {
		// TODO Auto-generated method stub
		userService.saveOrUpdate(model); // 查部门的默认状态

		return "alist";
	}

	/**
	 * 去修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "userAction_toupdate", results = {
			@Result(name = "toupdate", location = "/WEB-INF/pages/sysadmin/user/jUserUpdate.jsp") })
	public String toupdate() throws Exception {
		// TODO Auto-generated method stub
		// 1.根据id查询部门对象，压入值栈中
		User user = userService.get(model.getId());

		super.push(user);

		// 2.查询部门列表，压入值栈中
		Specification<Dept> spec = new Specification<Dept>() {

			// root:取当前对象的属性.as(属性中的类型) query：order，group cb:or and equles from
			// User where state = 1
			@Override
			public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};
		
		List<Dept> deptList = deptService.find(spec);
		
		super.put("deptList", deptList);

		return "toupdate";
	}
	
	
	/**
	 * 部门修改方法
	 * @return
	 * @throws Exception
	 */
	@Action(value="userAction_update")
	public String update() throws Exception {
		// TODO Auto-generated method stub
		// 根据id查询数据库的对象
		User user = userService.get(model.getId());
		user.setDept(model.getDept());
		user.getUserinfo().setName(model.getUserinfo().getName());
		user.setState(model.getState());
		userService.saveOrUpdate(user);
		
		return "alist";
	}
	
	/**
	 * 部门删除
	 */
	@Action(value="userAction_delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(model.getId());
		String[] ids = model.getId().split(", ");  //通过逗号空格切割成string数组
		
		userService.delete(ids);
		
		return "alist";
	}
	
	/**
	 * 跳转用户角色分配页面
	 * @return
	 * @throws Exception
	 */
	@Action(value="userAction_torole",results={@Result(name="torole",location="/WEB-INF/pages/sysadmin/user/jUserRole.jsp")})
	public String torole() throws Exception {
		// TODO Auto-generated method stub
		// 1.用户对象
		User user = userService.get(model.getId());
		super.push(user);
		
		// 2.角色列表
		List<Role> roleList = roleService.find(null);
		super.put("roleList", roleList);
		
		// 3.该用户所拥有的角色字符串
		Set<Role> roles = user.getRoles();
		StringBuilder sb = new StringBuilder();
		for (Role role : roles) {
			sb.append(role.getName()).append(",");
		}
		super.put("roleStr", sb.toString());
		
		
		return "torole";
	}
	
	@Action(value="userAction_role")
	public String role() throws Exception {
		// TODO Auto-generated method stub
		// 接收用户选择的所有角色id
		HashSet<Role> roles = new HashSet<Role>();
		for (String rid : roleIds) {
			roles.add(roleService.get(rid));
		}
		
		// 获取当前用户对象
		User user = userService.get(model.getId());
		
		// 保存用户角色关系
		user.setRoles(roles);
		userService.saveOrUpdate(user);
		
		return "alist";
	}
}
