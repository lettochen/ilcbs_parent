package cn.itcast.web.action.cargo;

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

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.domain.Contract;
import cn.itcast.domain.Export;
import cn.itcast.domain.ExportProduct;
import cn.itcast.export.vo.ExportProductResult;
import cn.itcast.export.vo.ExportProductVo;
import cn.itcast.export.vo.ExportResult;
import cn.itcast.export.vo.ExportVo;
import cn.itcast.export.webservice.IEpService;
import cn.itcast.service.ContractService;
import cn.itcast.service.ExportProductService;
import cn.itcast.service.ExportService;
import cn.itcast.utils.Page;
import cn.itcast.utils.UtilFuns;
import cn.itcast.web.action.BaseAction;

@Result(name="alist",location="exportAction_list",type="redirectAction")
public class ExportAction extends BaseAction implements ModelDriven<Export>{

	private Export model = new Export();
	
/*	@Autowired
	private IEpService epService;*/
	
	@Override
	public Export getModel() {
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
	private ExportService exportService;
	
	@Autowired
	private ContractService contractService;

	@Autowired
	private ExportProductService exportProductService;
	
	/**
	 * 进入购销合同列表选择后新增出口报运单
	 * @return
	 * @throws Exception
	 */
	@Action(value="exportAction_contractList",results={@Result(name="contractList",location="/WEB-INF/pages/cargo/export/jContractList.jsp")})
	public String contractList() throws Exception {
		// TODO Auto-generated method stub
		// 查询所有购销合同
		Specification<Contract> spec = new Specification<Contract>() {

			@Override
			public Predicate toPredicate(Root<Contract> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("state").as(Integer.class), 1);
			}
		};
		org.springframework.data.domain.Page<Contract> page2 = contractService.findPage(spec, new PageRequest(page.getPageNo() - 1, page.getPageSize()));
		
		page.setTotalPage(page2.getTotalPages());
		page.setTotalRecord(page2.getTotalElements());
		page.setResults(page2.getContent());
		page.setUrl("exportAction_contractList");
		
		super.push(page);
		
		return "contractList";
	}
	
	@Action(value="exportAction_tocreate",results={@Result(name="tocreate",location="/WEB-INF/pages/cargo/export/jExportCreate.jsp")})
	public String tocreate() throws Exception {
		// TODO Auto-generated method stub
		return "tocreate";
	}
	
	/**
	 * 新增出口报运单
	 * @return
	 * @throws Exception
	 */
	@Action(value="exportAction_insert")
	public String insert() throws Exception {
		// TODO Auto-generated method stub
		exportService.saveOrUpdate(model);
		return "alist";
	}
	
	/**
	 * 出口报运单列表
	 * @return
	 * @throws Exception
	 */
	@Action(value="exportAction_list",results={@Result(name="list",location="/WEB-INF/pages/cargo/export/jExportList.jsp")})
	public String list() throws Exception {
		// TODO Auto-generated method stub
		org.springframework.data.domain.Page<Export> page2 = exportService.findPage(null, new PageRequest(page.getPageNo() - 1, page.getPageSize()));
		
		page.setTotalPage(page2.getTotalPages());
		page.setTotalRecord(page2.getTotalElements());
		page.setResults(page2.getContent());
		page.setUrl("exportAction_list");
		
		super.push(page);
		
		return "list";
	}
	
	/**
	 * 查看单个
	 * @return
	 * @throws Exception
	 */
	@Action(value="exportAction_toview",results={@Result(name="toview",location="/WEB-INF/pages/cargo/export/jExportView.jsp")})
	public String toview() throws Exception {
		// TODO Auto-generated method stub
		Export export = exportService.get(model.getId());
		super.push(export);
		
		return "toview";
	}
	
	/**
	 * 删除逻辑
	 * @return
	 * @throws Exception
	 */
	@Action(value="exportAction_delete")
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		String[] ids = model.getId().split(", ");
		exportService.delete(ids);
		
		return "alist";
	}
	
	/**
	 * 修改出口报运单状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "exportAction_submit")
	public String submit() throws Exception {
		// TODO Auto-generated method stub
		String[] ids = model.getId().split(", ");
		for (String cid : ids) {
			Export export = exportService.get(cid);
			export.setState(1);
			exportService.saveOrUpdate(export);
		}

		return "alist";
	}

	@Action(value = "exportAction_cancel")
	public String cancel() throws Exception {
		// TODO Auto-generated method stub
		String[] ids = model.getId().split(", ");
		for (String cid : ids) {
			Export export = exportService.get(cid);
			export.setState(0);
			exportService.saveOrUpdate(export);
		}

		return "alist";
	}
	
	/**
	 * 去修改页面
	 * @return
	 * @throws Exception
	 */
	@Action(value="exportAction_toupdate",results={@Result(name="toupdate",location="/WEB-INF/pages/cargo/export/jExportUpdate.jsp")})
	public String toupdate() throws Exception {
		// TODO Auto-generated method stub
		Export export = exportService.get(model.getId());
		
		super.push(export);
		
		return "toupdate";
	}
	
	
	@Action(value="exportAction_getTabledoData")
	public String getTabledoData() throws Exception {
		// TODO Auto-generated method stub
		
		Specification<ExportProduct> spec = new Specification<ExportProduct>() {

			@Override
			public Predicate toPredicate(Root<ExportProduct> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return cb.equal(root.get("export").get("id").as(String.class), model.getId());
			}
		};
		List<ExportProduct> epList = exportProductService.find(spec);
		
		ArrayList list = new ArrayList();
		for (ExportProduct exportProduct : epList) {
			HashMap map = new HashMap();
			map.put("id", exportProduct.getId());
			map.put("productNo", exportProduct.getProductNo());
			map.put("cnumber", exportProduct.getCnumber());
			map.put("grossWeight", UtilFuns.convertNull(exportProduct.getGrossWeight()));
			map.put("netWeight", UtilFuns.convertNull(exportProduct.getNetWeight()));
			map.put("sizeLength", UtilFuns.convertNull(exportProduct.getSizeLength()));
			map.put("sizeWidth", UtilFuns.convertNull(exportProduct.getSizeWidth()));
			map.put("sizeHeight", UtilFuns.convertNull(exportProduct.getSizeHeight()));
			map.put("exPrice", UtilFuns.convertNull(exportProduct.getExPrice()));
			map.put("tax", UtilFuns.convertNull(exportProduct.getTax()));
			
			list.add(map);
		}
		
		String jsonString = JSON.toJSONString(list);
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(jsonString);
		
		return NONE;
	}
	
	/**
	 * 修改逻辑
	 * @return
	 * @throws Exception
	 */
	@Action(value="exportAction_update")
	public String update() throws Exception {
		// TODO Auto-generated method stub
		
		// 出口报运单的保存
		Export export = exportService.get(model.getId());
		export.setInputDate(model.getInputDate());
		export.setLcno(model.getLcno());
		export.setConsignee(model.getConsignee());
		export.setShipmentPort(model.getShipmentPort());
		export.setDestinationPort(model.getDestinationPort());
		export.setTransportMode(model.getTransportMode());
		export.setPriceCondition(model.getPriceCondition());
		export.setMarks(model.getMarks());
		export.setRemark(model.getRemark());
		
		exportService.saveOrUpdate(export);

		// 出口报运修改的商品
		for (int i = 0; i < mr_id.length ; i++) {
			if(mr_changed[i].equals("1")){  // 修改过的进行操作 1代表修改
				ExportProduct exportProduct = exportProductService.get(mr_id[i]);
				exportProduct.setCnumber(mr_cnumber[i]);
				exportProduct.setGrossWeight(mr_grossWeight[i]);
				exportProduct.setNetWeight(mr_netWeight[i]);
				exportProduct.setSizeLength(mr_sizeLength[i]);
				exportProduct.setSizeWidth(mr_sizeWidth[i]);
				exportProduct.setSizeHeight(mr_sizeHeight[i]);
				exportProduct.setExPrice(mr_exPrice[i]);
				exportProduct.setTax(mr_tax[i]);
				
				exportProductService.saveOrUpdate(exportProduct);
			}
		}
		
		return "alist";
	}
	
	private String[] mr_id;   
	private String[] mr_changed;
	private Integer[] mr_cnumber;
	private Double[] mr_grossWeight;
	private Double[] mr_netWeight;
	private Double[] mr_sizeLength;
	private Double[] mr_sizeWidth;
	private Double[] mr_sizeHeight;
	private Double[] mr_exPrice;
	private Double[] mr_tax;

	public String[] getMr_id() {
		return mr_id;
	}

	public void setMr_id(String[] mr_id) {
		this.mr_id = mr_id;
	}

	public String[] getMr_changed() {
		return mr_changed;
	}

	public void setMr_changed(String[] mr_changed) {
		this.mr_changed = mr_changed;
	}

	public Integer[] getMr_cnumber() {
		return mr_cnumber;
	}

	public void setMr_cnumber(Integer[] mr_cnumber) {
		this.mr_cnumber = mr_cnumber;
	}

	public Double[] getMr_grossWeight() {
		return mr_grossWeight;
	}

	public void setMr_grossWeight(Double[] mr_grossWeight) {
		this.mr_grossWeight = mr_grossWeight;
	}

	public Double[] getMr_netWeight() {
		return mr_netWeight;
	}

	public void setMr_netWeight(Double[] mr_netWeight) {
		this.mr_netWeight = mr_netWeight;
	}

	public Double[] getMr_sizeLength() {
		return mr_sizeLength;
	}

	public void setMr_sizeLength(Double[] mr_sizeLength) {
		this.mr_sizeLength = mr_sizeLength;
	}

	public Double[] getMr_sizeWidth() {
		return mr_sizeWidth;
	}

	public void setMr_sizeWidth(Double[] mr_sizeWidth) {
		this.mr_sizeWidth = mr_sizeWidth;
	}

	public Double[] getMr_sizeHeight() {
		return mr_sizeHeight;
	}

	public void setMr_sizeHeight(Double[] mr_sizeHeight) {
		this.mr_sizeHeight = mr_sizeHeight;
	}

	public Double[] getMr_exPrice() {
		return mr_exPrice;
	}

	public void setMr_exPrice(Double[] mr_exPrice) {
		this.mr_exPrice = mr_exPrice;
	}

	public Double[] getMr_tax() {
		return mr_tax;
	}

	public void setMr_tax(Double[] mr_tax) {
		this.mr_tax = mr_tax;
	}
	
	@Action(value="exportAction_exportE")
	public String exportE() throws Exception {
		// TODO Auto-generated method stub
		// 出口报运单对象
		Export export = exportService.get(model.getId());
		WebClient client = WebClient.create("http://localhost:8080/jk_export/ws/export/user");
		
		ExportVo exportVo = new ExportVo();
		// export拷贝到exportVo
		BeanUtils.copyProperties(export, exportVo);
		// 拷贝后不相同的属性需要手动赋值
		exportVo.setExportId(export.getId());
		
		// 封装报运单下的货物对象
		Set<ExportProduct> exportProducts = export.getExportProducts();
		HashSet<ExportProductVo> products = new HashSet<ExportProductVo>();
		for (ExportProduct ep : exportProducts) {
			ExportProductVo epVo = new ExportProductVo();
			BeanUtils.copyProperties(ep, epVo);
			epVo.setExportProductId(ep.getId());
			epVo.setExportId(export.getId());  //设置报运单货物对应的报运单号
			
			products.add(epVo);
		}
		
		exportVo.setProducts(products);
		client.post(exportVo);  
		
		WebClient returnClient = WebClient.create("http://localhost:8080/jk_export/ws/export/user/" + exportVo.getId());
		ExportResult exportResult = returnClient.get(ExportResult.class);
		
		//根据返回的id查询数据库对象
		Export exportDB = exportService.get(exportResult.getExportId());
		exportDB.setState(exportResult.getState());
		exportDB.setRemark(exportResult.getRemark());
		exportService.saveOrUpdate(exportDB);
		
		Set<ExportProductResult> products2 = exportResult.getProducts();
		for (ExportProductResult epResult : products2) {
			ExportProduct ep = exportProductService.get(epResult.getExportProductId());
			ep.setTax(epResult.getTax());
			exportProductService.saveOrUpdate(ep);
		}
		
		return "alist";
	}
	
	/*@Action(value="exportAction_wsExportE")
	public String wsExportE() throws Exception {
		// TODO Auto-generated method stub
		// 出口报运单对象
		Export export = exportService.get(model.getId());
		
		String jsonString = JSON.toJSONString(export);
		
		System.out.println("上传====="+jsonString);
		
		String exportE = epService.exportE(jsonString);
		
		Export returnExport = JSON.parseObject(exportE,Export.class);

		// 根据返回内容更新本地报运单
		Export exportDb = exportService.get(returnExport.getId());
		exportDb.setState(returnExport.getState());
		exportDb.setRemark(returnExport.getRemark());
		exportService.saveOrUpdate(exportDb); //修改数据后保存数据库
		
		Set<ExportProduct> exportProducts = returnExport.getExportProducts();
		
		for (ExportProduct ep : exportProducts) {
			// 根据海关返回的货物id查询当前系统的货物对象，进行tax赋值
			ExportProduct epDb = exportProductService.get(ep.getId());
			epDb.setTax(ep.getTax());
			
			exportProductService.saveOrUpdate(epDb);
		}
		return "alist";
	}
	
	
	@Action(value="exportAction_oldExportE")
	public String oldExportE() throws Exception {
		// TODO Auto-generated method stub
		// 出口报运单对象
		Export export = exportService.get(model.getId());
		
		HashMap exportMap = new HashMap();
		exportMap.put("exportId", export.getId());
		// 下面的属性都是为了海关保存mysql数据库时用，并不影响当前系统逻辑
		exportMap.put("boxNums", export.getBoxNums());
		exportMap.put("destinationPort", export.getDestinationPort());
		exportMap.put("customerContract", export.getCustomerContract());
		
		ArrayList list = new ArrayList();
		
		Set<ExportProduct> exportProducts = export.getExportProducts();
		for (ExportProduct exportProduct : exportProducts) { //循环报运单中所有的货物封装map数据
			HashMap epMap = new HashMap();
			epMap.put("exportProductId", exportProduct.getId());
			// 下面的属性都是为了海关保存mysql数据库时用，并不影响当前系统逻辑
			epMap.put("cnumber", exportProduct.getCnumber());
			epMap.put("price", exportProduct.getPrice());
			
			list.add(epMap);
		}
		
		exportMap.put("products", list);  //封装出口报运货物集合到出口报运map中
		String jsonString = JSON.toJSONString(exportMap);
		
		String exportE = epService.exportE(jsonString);
		
		HashMap returnMap = JSON.parseObject(exportE,HashMap.class);

		// 根据返回内容更新本地报运单
		Export exportDb = exportService.get(returnMap.get("exportId").toString());
		exportDb.setState(Integer.parseInt( returnMap.get("state").toString()));
		exportDb.setRemark(returnMap.get("remark").toString());
		exportService.saveOrUpdate(exportDb); //修改数据后保存数据库
		
		List<HashMap> returnList = JSON.parseArray(returnMap.get("products").toString(), HashMap.class);
		
		for (HashMap hashMap : returnList) {
			// 根据海关返回的货物id查询当前系统的货物对象，进行tax赋值
			ExportProduct epDb = exportProductService.get(hashMap.get("exportProductId").toString());
			epDb.setTax(Double.parseDouble( hashMap.get("tax").toString()));
			
			exportProductService.saveOrUpdate(epDb);
		}
		return "alist";
	}*/
}
