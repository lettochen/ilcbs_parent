package cn.itcast.web.action.sysadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.domain.Module;
import cn.itcast.domain.Role;
import cn.itcast.exception.SysException;
import cn.itcast.service.ModuleService;
import cn.itcast.service.RoleService;
import cn.itcast.utils.Page;
import cn.itcast.utils.UtilFuns;
import cn.itcast.web.action.BaseAction;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Namespace("/sysadmin")
@Result(name = "alist", type = "redirectAction", location = "roleAction_list")
public class RoleAction extends BaseAction implements ModelDriven<Role> {

	@Autowired
	private JedisPool pool;
	
	private String moduleIds;

	public String getModuleIds() {
		return moduleIds;
	}

	public void setModuleIds(String moduleIds) {
		this.moduleIds = moduleIds;
	}

	@Autowired
	private ModuleService moduleService;
	
	@Autowired
	private RoleService roleService;

	private Role model = new Role();

	@Override
	public Role getModel() {
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

	@Action(value = "roleAction_list", results = {
			@Result(name = "list", location = "/WEB-INF/pages/sysadmin/role/jRoleList.jsp") })
	public String list() throws Exception {
		// TODO Auto-generated method stub

		org.springframework.data.domain.Page<Role> page2 = roleService.findPage(null,
				new PageRequest(page.getPageNo() - 1, page.getPageSize()));

		page.setTotalPage(page2.getTotalPages()); // 总页数
		page.setTotalRecord(page2.getTotalElements()); // 总记录数
		page.setResults(page2.getContent()); // 查询的记录
		page.setUrl("roleAction_list"); // 上下页跳转的地址(相对路径地址)

		super.push(page);

		return "list";
	}

	/**
	 * 查看单个角色
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "roleAction_toview", results = {
			@Result(name = "toview", location = "/WEB-INF/pages/sysadmin/role/jRoleView.jsp") })
	public String toview() throws Exception {
		// TODO Auto-generated method stub
		// 先根据id从数据库中查询
		Role role = roleService.get(model.getId());

		super.push(role);

		return "toview";
	}

	/**
	 * 去新增页面
	 */
	@Action(value = "roleAction_tocreate", results = {
			@Result(name = "tocreate", location = "/WEB-INF/pages/sysadmin/role/jRoleCreate.jsp") })
	public String tocreate() throws Exception {
		

		return "tocreate";
	}

	/**
	 * 新增方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "roleAction_insert")
	public String insert() throws Exception {
		// TODO Auto-generated method stub
		roleService.saveOrUpdate(model); // 查部门的默认状态

		return "alist";
	}

	/**
	 * 去修改页面
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "roleAction_toupdate", results = {
			@Result(name = "toupdate", location = "/WEB-INF/pages/sysadmin/role/jRoleUpdate.jsp") })
	public String toupdate() throws Exception {
		// TODO Auto-generated method stub
		// 1.根据id查询部门对象，压入值栈中
		Role role = roleService.get(model.getId());

		super.push(role);

		return "toupdate";
	}
	
	
	/**
	 * 删除修改方法
	 * @return
	 * @throws Exception
	 */
	@Action(value="roleAction_update")
	public String update() throws Exception {
		// TODO Auto-generated method stub
		// 根据id查询数据库的对象
		Role role = roleService.get(model.getId());
		role.setName(model.getName());
		role.setRemark(model.getRemark());
		
		roleService.saveOrUpdate(role);
		
		return "alist";
	}
	
	/**
	 * 角色删除
	 */
	@Action(value="roleAction_delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		System.out.println(model.getId());
		String[] ids = model.getId().split(", ");  //通过逗号空格切割成string数组
		
		roleService.delete(ids);
		
		return "alist";
	}
	
	@Action(value="roleAction_tomodule",results={@Result(name="tomodule",location="/WEB-INF/pages/sysadmin/role/jRoleModule.jsp")})
	public String tomodule() throws Exception {
		// TODO Auto-generated method stub
		// 根据id将角色压倒栈顶
		try {
			Role role = roleService.get(model.getId());
			super.push(role);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SysException("请您勾选一个角色后再点击模块按钮");
		}
		
		return "tomodule";
	}
	
	
	@Action("roleAction_genzTreeNodes")
	public String genzTreeNodes() throws Exception {
		// TODO Auto-generated method stub
		
		// 返回ajax请求后的json字符串满足ztree的数据
		
		// 1。根据id获取角色对象
		Role role = roleService.get(model.getId());
		
		Jedis jedis = pool.getResource();
		String returnStr = jedis.get("genzTreeNodes_" + role.getId());
		
		if(UtilFuns.isEmpty(returnStr)){ // 如果redis数据库中没有当前的模块数据，从数据库中查询
			Set<Module> roleModules = role.getModules(); //用户所拥有的所有模块
			
			// 2.查询所有的模块
			Specification<Module> spec = new Specification<Module>() {

				@Override
				public Predicate toPredicate(Root<Module> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					// TODO Auto-generated method stub
					return cb.equal(root.get("state").as(Integer.class), 1);
				}
			};
			List<Module> moduleList = moduleService.find(spec); //所有未停用的模块
			
			ArrayList list = new ArrayList();
			for (Module module : moduleList) {
				HashMap map = new HashMap();
				map.put("id", module.getId());
				map.put("pId", module.getParentId());
				map.put("name", module.getName());
				if(roleModules.contains(module)){
					map.put("checked", true);
				}
				
				list.add(map);
			}
			
			returnStr = JSON.toJSONString(list);
			
			// 存到redis中
			jedis.set("genzTreeNodes_" + role.getId(), returnStr);
			
			System.out.println("从数据库中获取================");
		}else{
			System.out.println("从redis中获取===============");
		}

		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(returnStr);
		return NONE;
	}
	
	/*
	 * [{"id": "11","pId": "1","name": "随意勾选 1-1"}, {"id": "111","pId": "11","name": "随意勾选 1-1-1","checked": "true"}]
	 * 
	 * */
	@Action("roleAction_oldGenzTreeNodes")
	public String oldGenzTreeNodes() throws Exception {
		// TODO Auto-generated method stub
		
		// 返回ajax请求后的json字符串满足ztree的数据
		
		// 1。根据id获取角色对象
		Role role = roleService.get(model.getId());
		Set<Module> roleModules = role.getModules(); //用户所拥有的所有模块
		
		// 2.查询所有的模块
		Specification<Module> spec = new Specification<Module>() {

			@Override
			public Predicate toPredicate(Root<Module> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};
		List<Module> moduleList = moduleService.find(spec); //所有未停用的模块
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		//[{"id": "11","pId": "1","name": "随意勾选 1-1"}, {"id": "111","pId": "11","name": "随意勾选 1-1-1","checked": "true"}]
		
		int size = moduleList.size();
		for (Module module:moduleList) {
			size--;
			sb.append("{");
			sb.append("\"id\": \"").append(module.getId()).append("\"");
			sb.append(",\"pId\": \"").append(module.getParentId()).append("\"");
			sb.append(",\"name\": \"").append(module.getName()).append("\"");
			//是否当前角色拥有该模块
			if(roleModules.contains(module)){
				sb.append(",\"checked\": \"true\"");
			}
			
			sb.append("}");
			if(size > 0){
				sb.append(",");
			}
		}
		sb.append("]");
		
		System.out.println("=========="+sb.toString());
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(sb.toString());
		return NONE;
	}
	
	@Action(value="roleAction_module")
	public String module() throws Exception {
		// TODO Auto-generated method stub
		
		// 1.根据id获取当前role对象
		Role role = roleService.get(model.getId());
		
		// 2.获取用户选择的所有模块对象
		String[] mids = moduleIds.split(",");
		HashSet<Module> modules = new HashSet<Module>();
		for (String mid : mids) {
			Module module = moduleService.get(mid);
			modules.add(module);
		}
		
		// 3.设置role和module的关系
		role.setModules(modules);
		
		// 4.保存数据库
		roleService.saveOrUpdate(role);
		
		// 清除redis缓存
		Jedis jedis = pool.getResource();
		jedis.del("genzTreeNodes_"+role.getId());
		
		return "alist";
	}
}
