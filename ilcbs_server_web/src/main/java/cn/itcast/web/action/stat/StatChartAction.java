package cn.itcast.web.action.stat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.itcast.service.SqlService;
import cn.itcast.service.UserService;
import cn.itcast.web.action.BaseAction;

@Namespace("/stat")
public class StatChartAction extends BaseAction{

	@Autowired
	private SqlService sqlService;
	
	@Autowired
	private UserService userService;
	
	@Action(value="statChartAction_factorysale",results={@Result(name="factorysale",location="/WEB-INF/pages/stat/pieSimple.html")})
	public String factorysale() throws Exception {
		// TODO Auto-generated method stub
		return "factorysale";
	}
	
	@Action(value="statChartAction_getFactorysaleData")
	public String getFactorysaleData() throws Exception {
		// TODO Auto-generated method stub
		String sql = "select factory_name,sum(amount) from contract_product_c group by factory_name order by sum(amount) desc";
		List executeSQL = sqlService.executeSQL(sql);
		
		//[{factoryName: "Czech Republic",amount: 301.90},{factoryName: "Czech Republic",amount: 301.90}]
		ArrayList returnArr = new ArrayList();
		for (int i = 0; i < executeSQL.size() ; i++) {
			HashMap returnMap = new HashMap();
			returnMap.put("factoryName", executeSQL.get(i++));
			returnMap.put("amount", executeSQL.get(i));
			
			returnArr.add(returnMap);
		}
		
		String jsonString = JSON.toJSONString(returnArr);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");;
		response.getWriter().write(jsonString);
		return NONE;
	}
	
	@Action(value="statChartAction_productsale",results={@Result(name="productsale",location="/WEB-INF/pages/stat/column-3d.htm")})
	public String productsale() throws Exception {
		// TODO Auto-generated method stub
		return "productsale";
	}
	
	@Action(value="statChartAction_getProductsaleData")
	public String getProductsaleData() throws Exception {
		// TODO Auto-generated method stub
		String sql = "select * from (select product_no,sum(amount) from contract_product_c group by product_no order by sum(amount) desc) where rownum <= 15";
		List executeSQL = sqlService.executeSQL(sql);
		
		ArrayList productNos = new ArrayList();
		ArrayList productAoumnts = new ArrayList();
		for (int i= 0 ;i < executeSQL.size(); i++) {
			productNos.add(executeSQL.get(i++));
			productAoumnts.add(Double.parseDouble(executeSQL.get(i).toString()));
		}
		
		HashMap map = new HashMap();
		map.put("productNo", productNos);
		map.put("amounts", productAoumnts);
		
		String jsonString = JSON.toJSONString(map);
		
		System.out.println(jsonString);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(jsonString);
		return NONE;
	}
	
	@Action(value="statChartAction_onlineinfo",results={@Result(name="onlineinfo",location="/WEB-INF/pages/stat/lineDemo.htm")})
	public String onlineinfo() throws Exception {
		// TODO Auto-generated method stub
		return "onlineinfo";
	}
	
	@Action(value="statChartAction_getOnlineinfoData")
	public String getOnlineinfoData() throws Exception {
		// TODO Auto-generated method stub
		List<Object[]> loginData = userService.getLoginData();
		
		ArrayList times = new ArrayList();
		ArrayList counts = new ArrayList();
		for (int i= 0 ;i < loginData.size(); i++ ) {
			Object[] objects = loginData.get(i);
			times.add(objects[0]);
			counts.add(Double.parseDouble(objects[1].toString()));
		}
		
		HashMap map = new HashMap();
		map.put("times", times);
		map.put("counts", counts);
		
		String jsonString = JSON.toJSONString(map);
		
		System.out.println(jsonString);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(jsonString);
		
		return NONE;
	}
	
	/*@Action(value="statChartAction_getOnlineinfoData")
	public String getOnlineinfoData() throws Exception {
		// TODO Auto-generated method stub
		String sql = "select a1,nvl(p_count,0) from (select to_char(login_time,'HH24') p_time,count(*) p_count from login_log_p group by to_char(login_time,'HH24')) p,online_info_t where p.p_time(+) = online_info_t.a1 order by a1";
		List executeSQL = sqlService.executeSQL(sql);
		
		ArrayList times = new ArrayList();
		ArrayList counts = new ArrayList();
		for (int i= 0 ;i < executeSQL.size(); i++) {
			times.add(executeSQL.get(i++));
			counts.add(Double.parseDouble(executeSQL.get(i).toString()));
		}
		
		HashMap map = new HashMap();
		map.put("times", times);
		map.put("counts", counts);
		
		String jsonString = JSON.toJSONString(map);
		
		System.out.println(jsonString);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(jsonString);
		
		return NONE;
	}*/
}
