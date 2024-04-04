package com.pi.farmease.services;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;





public class UserExcelExporter {
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	
	private List<Amortisement> listAmortissement;
	
	
	public UserExcelExporter(List<Amortisement> listAmortissement) {
		this.listAmortissement=listAmortissement;
		workbook = new XSSFWorkbook();
		
	}
	
	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell=row.createCell(columnCount);
		
		if(value instanceof Long) {
			cell.setCellValue((Long) value);
		}else if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		}else if(value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		}
		else if(value instanceof Double) {
			cell.setCellValue((Double)value);
		}
		else if(value instanceof Float) {
			cell.setCellValue((Double)value);
		}
		else {
			
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}
	private void writeHeaderLine() {
		sheet=workbook.createSheet("Amortissement");
		
		Row row = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		XSSFFont font=workbook.createFont();
		font.setBold(true);
		font.setFontHeight(20);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		createCell(row,0,"Credit Information",style);
		sheet.addMergedRegion(new CellRangeAddress(0,0,0,4));
		font.setFontHeightInPoints((short)(10));
		
		row=sheet.createRow(1);
		font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        
        
        createCell(row, 0, "Echéance N°", style);
        createCell(row, 1, "Montant restant dù\r\n", style);
        createCell(row, 2, "Interet", style);
        createCell(row, 3, "Amortissement ", style);
        createCell(row, 4, "Mensualité", style);
        
	}
	
	private void writeDataLines() {
		int rowCount=2;
		
		CellStyle style=workbook.createCellStyle();
		XSSFFont font=workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
			//int o=0;
		for(Amortisement stu:listAmortissement) {
			//listAmortissement.get(i).toString();
			Row row=sheet.createRow(rowCount++);
			int columnCount=0;
			createCell(row, columnCount++,rowCount-2, style);
			createCell(row, columnCount++,stu.getAmount(), style);
			createCell(row, columnCount++, stu.getIntrest(),style);
			createCell(row, columnCount++,stu.getAmortiValue(), style);
			createCell(row, columnCount++,stu.getMonthlyPayment(), style);
		}
	}
	
	public void export(HttpServletResponse response) throws IOException{
		writeHeaderLine();
		writeDataLines();
		
		ServletOutputStream outputStream=response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
	
	
}
