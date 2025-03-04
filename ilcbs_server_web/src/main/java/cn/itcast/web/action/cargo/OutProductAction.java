package cn.itcast.web.action.cargo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.domain.ContractProduct;
import cn.itcast.service.ContractProductService;
import cn.itcast.utils.DownloadUtil;
import cn.itcast.utils.UtilFuns;
import cn.itcast.web.action.BaseAction;

@Namespace("/cargo")
public class OutProductAction extends BaseAction{

	@Autowired
	private ContractProductService contractProductService;
	
	
	@Action(value="outProductAction_toedit",results={@Result(name="toedit",location="/WEB-INF/pages/cargo/outproduct/jOutProduct.jsp")})
	public String toedit() throws Exception {
		// TODO Auto-generated method stub
		return "toedit";
	}
	
	private String inputDate;

	public String getInputDate() {
		return inputDate;
	}


	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	/**
	 * 测试导出80w条数据
	 * @return
	 * @throws Exception
	 */
	@Action(value="outProductAction_print")
	public String print() throws Exception {
		// TODO Auto-generated method stub
		Workbook book = new SXSSFWorkbook();
		Sheet sheet = book.createSheet();
		
		// 设置列宽
		sheet.setColumnWidth(1, 25 * 256);
		sheet.setColumnWidth(2, 10 * 256);
		sheet.setColumnWidth(3, 25 * 256);
		sheet.setColumnWidth(4, 10 * 256);
		sheet.setColumnWidth(5, 10 * 256);
		sheet.setColumnWidth(6, 10 * 256);
		sheet.setColumnWidth(7, 10 * 256);
		sheet.setColumnWidth(8, 10 * 256);
		
		int rowIndex = 0;
		// 大标题
		Row bigTitleRow = sheet.createRow(rowIndex++);
		bigTitleRow.setHeightInPoints(36);
		Cell bigCell = bigTitleRow.createCell(1);
		bigCell.setCellStyle(bigTitle(book));
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
		// 标题内容  inputDate 2015年7月份出货表
		String titleStr = inputDate.replace("-0", "-").replace("-", "年")+"月份出货表";
		bigCell.setCellValue(titleStr);
		
		// 小标题
		String[] smartStrs = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
		Row smartRow = sheet.createRow(rowIndex++);
		smartRow.setHeightInPoints(26);
		for (int i=0; i < smartStrs.length ; i++) {
			Cell smartCell = smartRow.createCell(i+1);
			smartCell.setCellValue(smartStrs[i]); //设置内容
			smartCell.setCellStyle(title(book));  //设置样式
		}
		
		// 内容
		
		List<ContractProduct> cpList = contractProductService.findCpByShipTime(inputDate);
		System.out.println("======="+cpList.size());
		
		for (int i = 0; i < 5000; i++) {
			for (ContractProduct cp : cpList) {
				Row contentRow = sheet.createRow(rowIndex++);
				contentRow.setHeightInPoints(26);
				
				Cell cell01 = contentRow.createCell(1);
				cell01.setCellValue(cp.getContract().getCustomName());
				cell01.setCellStyle(text(book));
				
				Cell cell02 = contentRow.createCell(2);
				cell02.setCellValue(cp.getContract().getContractNo());
				cell02.setCellStyle(text(book));
				
				Cell cell03 = contentRow.createCell(3);
				cell03.setCellValue(cp.getProductNo());
				cell03.setCellStyle(text(book));
				
				Cell cell04 = contentRow.createCell(4);
				cell04.setCellValue(cp.getCnumber());
				cell04.setCellStyle(text(book));
				
				Cell cell05 = contentRow.createCell(5);
				cell05.setCellValue(cp.getFactoryName());
				cell05.setCellStyle(text(book));
				
				Cell cell06 = contentRow.createCell(6);
				cell06.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getDeliveryPeriod()));
				cell06.setCellStyle(text(book));
				
				Cell cell07 = contentRow.createCell(7);
				cell07.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getShipTime()));
				cell07.setCellStyle(text(book));
				
				Cell cell08 = contentRow.createCell(8);
				cell08.setCellValue(cp.getContract().getTradeTerms());
				cell08.setCellStyle(text(book));
			}
		}
		
	
		
		
		// 写出excel
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HttpServletResponse response = ServletActionContext.getResponse();
		String returnName = titleStr + ".xls";
		response.setContentType("application/octet-stream;charset=utf-8");
		returnName = response.encodeURL(new String(returnName.getBytes(),"iso8859-1"));	
		response.addHeader("Content-Disposition",   "attachment;filename=" + returnName);  
		ServletOutputStream outputStream2 = response.getOutputStream();
		
		book.write(outputStream2);
		book.close();
		
		return NONE;
	}
	
	
	/**
	 * 用模版的方式，导入excel文件
	 * @return
	 * @throws Exception
	 */
	@Action(value="outProductAction_templePrint")
	public String templePrint() throws Exception {
		// TODO Auto-generated method stub
		String path = "/webapp/make/xlsprint/tOUTPRODUCT.xlsx".replace("/", File.separator);
		String filePath = ServletActionContext.getServletContext().getRealPath("/make/xlsprint/tOUTPRODUCT.xlsx");
		FileInputStream is = new FileInputStream(filePath);
		Workbook book = new XSSFWorkbook(is);
		Sheet sheet = book.getSheetAt(0);
	
		int rowIndex = 0;
		// 大标题
		Row bigTitleRow = sheet.getRow(rowIndex++);
		Cell bigCell = bigTitleRow.getCell(1);
	
		// 标题内容  inputDate 2015年7月份出货表
		String titleStr = inputDate.replace("-0", "-").replace("-", "年")+"月份出货表";
		bigCell.setCellValue(titleStr);
		
		// 小标题
		rowIndex++;
		
		// 内容
		// 保存样式
		CellStyle cs01 = sheet.getRow(rowIndex).getCell(1).getCellStyle();
		String stringCellValue = sheet.getRow(rowIndex).getCell(1).getStringCellValue();
		System.out.println("获取的第一列信息"+stringCellValue);
		
		CellStyle cs02 = sheet.getRow(rowIndex).getCell(2).getCellStyle();
		CellStyle cs03 = sheet.getRow(rowIndex).getCell(3).getCellStyle();
		CellStyle cs04 = sheet.getRow(rowIndex).getCell(4).getCellStyle();
		CellStyle cs05 = sheet.getRow(rowIndex).getCell(5).getCellStyle();
		CellStyle cs06 = sheet.getRow(rowIndex).getCell(6).getCellStyle();
		CellStyle cs07 = sheet.getRow(rowIndex).getCell(7).getCellStyle();
		CellStyle cs08 = sheet.getRow(rowIndex).getCell(8).getCellStyle();
		
		List<ContractProduct> cpList = contractProductService.findCpByShipTime(inputDate);
		
		for (int i = 0; i < 5000; i++) {
			for (ContractProduct cp : cpList) {
				
				Row contentRow = sheet.createRow(rowIndex++);
				contentRow.setHeightInPoints(26);
				
				Cell cell01 = contentRow.createCell(1);
				cell01.setCellValue(cp.getContract().getCustomName());
				cell01.setCellStyle(cs01);
				
				Cell cell02 = contentRow.createCell(2);
				cell02.setCellValue(cp.getContract().getContractNo());
				cell02.setCellStyle(cs02);
				
				Cell cell03 = contentRow.createCell(3);
				cell03.setCellValue(cp.getProductNo());
				cell03.setCellStyle(cs03);
				
				Cell cell04 = contentRow.createCell(4);
				cell04.setCellValue(cp.getCnumber());
				cell04.setCellStyle(cs04);
				
				Cell cell05 = contentRow.createCell(5);
				cell05.setCellValue(cp.getFactoryName());
				cell05.setCellStyle(cs05);
				
				Cell cell06 = contentRow.createCell(6);
				cell06.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getDeliveryPeriod()));
				cell06.setCellStyle(cs06);
				
				Cell cell07 = contentRow.createCell(7);
				cell07.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getShipTime()));
				cell07.setCellStyle(cs07);
				
				Cell cell08 = contentRow.createCell(8);
				cell08.setCellValue(cp.getContract().getTradeTerms());
				cell08.setCellStyle(cs08);
			}
		}
		
		
		
		// 写出excel
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HttpServletResponse response = ServletActionContext.getResponse();

		DownloadUtil util = new DownloadUtil();
		book.write(os);
		util.download(os, response, titleStr + ".xlsx");
		
		return NONE;
	}
	

	@Action(value="outProductAction_oldPrint")
	public String oldPrint() throws Exception {
		// TODO Auto-generated method stub
		HSSFWorkbook book = new HSSFWorkbook();
		HSSFSheet sheet = book.createSheet();
		
		// 设置列宽
		sheet.setColumnWidth(1, 25 * 256);
		sheet.setColumnWidth(2, 10 * 256);
		sheet.setColumnWidth(3, 25 * 256);
		sheet.setColumnWidth(4, 10 * 256);
		sheet.setColumnWidth(5, 10 * 256);
		sheet.setColumnWidth(6, 10 * 256);
		sheet.setColumnWidth(7, 10 * 256);
		sheet.setColumnWidth(8, 10 * 256);
		
		int rowIndex = 0;
		// 大标题
		HSSFRow bigTitleRow = sheet.createRow(rowIndex++);
		bigTitleRow.setHeightInPoints(36);
		HSSFCell bigCell = bigTitleRow.createCell(1);
		bigCell.setCellStyle(bigTitle(book));
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
		// 标题内容  inputDate 2015年7月份出货表
		String titleStr = inputDate.replace("-0", "-").replace("-", "年")+"月份出货表";
		bigCell.setCellValue(titleStr);
		
		// 小标题
		String[] smartStrs = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
		HSSFRow smartRow = sheet.createRow(rowIndex++);
		smartRow.setHeightInPoints(26);
		for (int i=0; i < smartStrs.length ; i++) {
			HSSFCell smartCell = smartRow.createCell(i+1);
			smartCell.setCellValue(smartStrs[i]); //设置内容
			smartCell.setCellStyle(title(book));  //设置样式
		}
		
		// 内容
		
		List<ContractProduct> cpList = contractProductService.findCpByShipTime(inputDate);
		System.out.println("======="+cpList.size());
		
		for (ContractProduct cp : cpList) {
			HSSFRow contentRow = sheet.createRow(rowIndex++);
			contentRow.setHeightInPoints(26);
			
			HSSFCell cell01 = contentRow.createCell(1);
			cell01.setCellValue(cp.getContract().getCustomName());
			cell01.setCellStyle(text(book));
			
			HSSFCell cell02 = contentRow.createCell(2);
			cell02.setCellValue(cp.getContract().getContractNo());
			cell02.setCellStyle(text(book));
			
			HSSFCell cell03 = contentRow.createCell(3);
			cell03.setCellValue(cp.getProductNo());
			cell03.setCellStyle(text(book));
			
			HSSFCell cell04 = contentRow.createCell(4);
			cell04.setCellValue(cp.getCnumber());
			cell04.setCellStyle(text(book));
			
			HSSFCell cell05 = contentRow.createCell(5);
			cell05.setCellValue(cp.getFactoryName());
			cell05.setCellStyle(text(book));
			
			HSSFCell cell06 = contentRow.createCell(6);
			cell06.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getDeliveryPeriod()));
			cell06.setCellStyle(text(book));
			
			HSSFCell cell07 = contentRow.createCell(7);
			cell07.setCellValue(UtilFuns.dateTimeFormat(cp.getContract().getShipTime()));
			cell07.setCellStyle(text(book));
			
			HSSFCell cell08 = contentRow.createCell(8);
			cell08.setCellValue(cp.getContract().getTradeTerms());
			cell08.setCellStyle(text(book));
		}
		
		
		// 写出excel
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HttpServletResponse response = ServletActionContext.getResponse();
		String returnName = titleStr + ".xls";
		response.setContentType("application/octet-stream;charset=utf-8");
		returnName = response.encodeURL(new String(returnName.getBytes(),"iso8859-1"));	
		response.addHeader("Content-Disposition",   "attachment;filename=" + returnName);  
		ServletOutputStream outputStream2 = response.getOutputStream();
		
		book.write(outputStream2);
		book.close();
		
/*		response.setContentType("application/octet-stream;charset=utf-8");
		returnName = response.encodeURL(new String(returnName.getBytes(),"iso8859-1"));			//保存的文件名,必须和页面编码一致,否则乱码
		response.addHeader("Content-Disposition",   "attachment;filename=" + returnName);  
		response.setContentLength(byteArrayOutputStream.size());
		
		ServletOutputStream outputstream = response.getOutputStream();	//取得输出流
		byteArrayOutputStream.writeTo(outputstream);					//写到输出流
		byteArrayOutputStream.close();									//关闭
		outputstream.flush();			*/				
		
/*		DownloadUtil util = new DownloadUtil();
		book.write(os);
		util.download(os, response, titleStr + ".xls");*/
		
		return NONE;
	}
	
	
	//大标题的样式
	public CellStyle bigTitle(Workbook wb){
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short)16);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);					//字体加粗
		
		style.setFont(font);
		
		style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
		
		return style;
	}
	//小标题的样式
	public CellStyle title(Workbook wb){
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("黑体");
		font.setFontHeightInPoints((short)12);
		
		style.setFont(font);
		
		style.setAlignment(CellStyle.ALIGN_CENTER);					//横向居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
		
		style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
		style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
		style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
		style.setBorderRight(CellStyle.BORDER_THIN);				//右细线
		
		return style;
	}
	
	//文字样式
	public CellStyle text(Workbook wb){
		CellStyle style = wb.createCellStyle();
		Font font = wb.createFont();
		font.setFontName("Times New Roman");
		font.setFontHeightInPoints((short)10);
		
		style.setFont(font);
		
		style.setAlignment(CellStyle.ALIGN_LEFT);					//横向居左
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);		//纵向居中
		
		style.setBorderTop(CellStyle.BORDER_THIN);					//上细线
		style.setBorderBottom(CellStyle.BORDER_THIN);				//下细线
		style.setBorderLeft(CellStyle.BORDER_THIN);					//左细线
		style.setBorderRight(CellStyle.BORDER_THIN);				//右细线
		
		return style;
	}
	
}
