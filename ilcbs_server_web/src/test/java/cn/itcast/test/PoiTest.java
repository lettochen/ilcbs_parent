package cn.itcast.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class PoiTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Workbook book = new HSSFWorkbook();
		Sheet sheet = book.createSheet();
		sheet.setColumnWidth(1, 25 * 256);
		sheet.setColumnWidth(2, 10 * 256);
		sheet.setColumnWidth(3, 25 * 256);
		sheet.setColumnWidth(4, 10 * 256);
		sheet.setColumnWidth(5, 10 * 256);
		sheet.setColumnWidth(6, 10 * 256);
		sheet.setColumnWidth(7, 10 * 256);
		sheet.setColumnWidth(8, 10 * 256);
		
		Row row = sheet.createRow(0);
		row.setHeightInPoints(36);
		Cell cell = row.createCell(1);
		cell.setCellValue("2012年8月份出货表");
		
		
		
		//合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));
		
		// 居中显示
		CellStyle style = book.createCellStyle();
		Font font = book.createFont();
		//设置字体样式
		   // 加粗设置字体大小
		   font.setBold(true);
		   font.setFontHeightInPoints((short)16);
		   font.setFontName("宋体");
		// 横向居中
		style.setAlignment(CellStyle.ALIGN_CENTER); 
		// 纵向居中
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); 
		style.setFont(font);
		
		cell.setCellStyle(style);
		
		
		book.write(new FileOutputStream("E:/itcast297.xls"));
		book.close();
	}
}
