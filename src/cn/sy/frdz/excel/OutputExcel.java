package cn.sy.frdz.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class OutputExcel {
	public static void main(String[] args) {
		List<Map<String, String>> dataList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Map<String, String> map = new HashMap<>();
			map.put("aaa", "aaa" + i);
			map.put("bbb", "bbb" + i);
			map.put("ccc", "ccc" + i);
			map.put("ddd", "ddd" + i);
			dataList.add(map);
		}
		OutputExcel("D:/testClear/test.xls", "outputExcelName", dataList);
	}

	public static void OutputExcel(String url, String outputExcelName, List<Map<String, String>> dataList) {
		HSSFWorkbook workbook;
		HSSFSheet sheet;
		try {
			File file = new File(url);
			if (!file.exists()) {
				workbook = new HSSFWorkbook();
				workbook.createSheet();
			} else {
				FileInputStream input = new FileInputStream(file);
				workbook = new HSSFWorkbook(input);
			}
			if (outputExcelName.isEmpty()) {
				sheet = workbook.getSheetAt(0);
			} else {
				sheet = workbook.getSheet(outputExcelName);
				if (sheet == null) {
					sheet = workbook.createSheet(outputExcelName);
				}
			}

			int rows = sheet.getLastRowNum();
			sheet.setDefaultColumnWidth(50);
			if(rows!=0)
			rows += 2;
			HSSFRow row;
			HSSFCell cell;// 产生单元格
			HSSFCellStyle style = workbook.createCellStyle();
			style.setWrapText(true);
			row = sheet.createRow(rows);
			if (dataList == null || dataList.size() == 0) {
				return;
			}

			Map<String, String> map = dataList.get(0);
			int rowCell = 0;
			Map<String, Integer> cellRow = new HashMap<>();
			for (String key : map.keySet()) {
				cell = row.createCell(rowCell);
				cell.setCellType(CellType.STRING);
				cell.setCellStyle(style);
				cell.setCellValue(key);
				cellRow.put(key, rowCell);
				rowCell++;
			}
			rows++;
			for (int i = 0; i < dataList.size(); i++) {
				row = sheet.createRow(i + rows);
				Map<String, String> datas = dataList.get(i);
				for (String key : datas.keySet()) {
					cell = row.createCell(cellRow.get(key));
					cell.setCellType(CellType.STRING);
					cell.setCellValue(datas.get(key));
				}
			}
			FileOutputStream output = new FileOutputStream(url);
			workbook.write(output);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
