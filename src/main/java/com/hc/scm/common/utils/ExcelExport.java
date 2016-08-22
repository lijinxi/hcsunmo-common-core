package com.hc.scm.common.utils;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.alibaba.fastjson.JSON;

/**
 * 导出excel类
 * Description: 
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      peng.hz
 * Createdate:  2015-3-13上午11:55:51
 *
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-3-13     	peng.hz
 */
public final class ExcelExport {
	/**
	 * 工具类防止用户new出实例
	 */
	private ExcelExport(){}

	/**
	 * 导出数据到Excel,自动获取easyui的表头信息 与 查询条件  暂时不支持合并的表头  纵横转换的表头是单独写的
	 * @param fileName
	 * @param ColumnsMapList
	 * @param dataMapList
	 * @param response
	 * @param rowAccessWindowSize 导出excel过程中，如果需要访问导的第几行数据，则，需要给定这个参数为访问的excel行数；如传递的为空，则默认值为1行，
	 * 推荐使用默认值。 例如：如果想在程序中取得最后100行的数据，那么该参数=100； 否则就按照默认值导出。
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void commonExportData(String fileName, List<Map> ColumnsMapList, List<Map> dataMapList,
			HttpServletResponse response, Integer rowAccessWindowSize,String fileType) throws Exception {
		
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		fileName=StringUtils.isNotBlank(fileName)?fileName:String.valueOf(new Date().getTime());
		String fileName2 = new String(fileName.getBytes("gb2312"), "iso-8859-1");
		fileType=StringUtils.isBlank(fileType)?"xls":"xlsx";
		//文件名
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName2 + "."+fileType);
		response.setHeader("Pragma", "no-cache");

		if (rowAccessWindowSize == null) {

			rowAccessWindowSize = 1;
		}

		SXSSFWorkbook wb = new SXSSFWorkbook(rowAccessWindowSize.intValue());
		Sheet sheet1 = wb.createSheet();
		wb.setSheetName(0, fileName);
		sheet1.setDefaultRowHeightInPoints(20);
		sheet1.setDefaultColumnWidth((short) 16);
		//设置页脚
		Footer footer = sheet1.getFooter();
		footer.setRight("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());

		//设置样式 表头
		CellStyle style1 = wb.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Font font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 13);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		//设置样式 表头
		CellStyle style2 = wb.createCellStyle();
		style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style2.setWrapText(true);
//		//合并
//		CellRangeAddress rg1 = new CellRangeAddress(0, (short) 0, 0, (short) (ColumnsMapList.size() - 1));
//		sheet1.addMergedRegion(rg1);
		//设置样式 表头
		CellStyle style3 = wb.createCellStyle();
		style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		Font font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 18);
		font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style3.setFont(font3);
		style3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style3.setFillPattern(CellStyle.SOLID_FOREGROUND);
//		Row row0 = sheet1.createRow(0);
//		row0.setHeightInPoints(35);
//		//第一行 提示长
//		Cell cell0 = row0.createCell((short) 0);
//		cell0.setCellValue(fileName.toString());
//		cell0.setCellStyle(style3);

		//设置表头
		Row row1 = sheet1.createRow(0);
		row1.setHeightInPoints(20);
		for (int i = 0; i < ColumnsMapList.size(); i++) {
			Cell cell1 = row1.createCell(i);
			cell1.setCellType(HSSFCell.ENCODING_UTF_16);
			cell1.setCellValue(ColumnsMapList.get(i).get("title").toString());
			cell1.setCellStyle(style1);
		}

		//填充数据
		for (int j = 0; j < dataMapList.size(); j++) {
			Row row2 = sheet1.createRow((j + 1)); // 第三行开始填充数据 
			Map cellDataMap = dataMapList.get(j);
			for (int i = 0; i < ColumnsMapList.size(); i++) {
				Cell cell = row2.createCell(i);
				String cellValue = StringUtils.EMPTY;
				if (ColumnsMapList.get(i).get("field") != null) {
					String fieldString = String.valueOf(ColumnsMapList.get(i).get("field"));
					cellValue = String.valueOf(cellDataMap.get(fieldString));
				}
				cell.setCellValue(cellValue);
				cell.setCellStyle(style2);
			}

		}

		wb.write(response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
		wb.dispose();
	}
	
	/**
	 * 导出excel
	 * @param fileName 文件名
	 * @param exportColumns 导出列 json字符串 格式如：[{"field":"appCode","title":"编码"},{"field":"name","title":"名称"}]
	 * @param datalist 数据list
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void commonExportData(String fileName, String exportColumns, List datalist,	HttpServletResponse response,String fileType) throws Exception {
           List<Map> listArrayList= new ArrayList< Map>();
           Map map;
            if (datalist!= null&&datalist.size()>0){
               for (Object vo:datalist){
                   map= new HashMap();
                  CommonUtil.object2MapWithoutNull(vo,map);
                  listArrayList.add(map);
                       
               }
            }
            List<Map> ColumnsMapList=JSON.parseArray(exportColumns, Map.class);
            commonExportData(fileName, ColumnsMapList, listArrayList,
            		response, 1, fileType);
	}
	
	/**
	 * 导出excel(导入一个sheet)
	 * @param fileName 文件名
	 * @param exportColumns 导出列 json字符串 格式如：[{"field":"appCode","title":"编码"},{"field":"name","title":"名称"}]
	 * @param datalist 数据list
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void commonExportData(List datalist,
		 HttpServletResponse response,HttpServletRequest req) throws Exception {
		 req.setCharacterEncoding("utf-8");
		 String exportColumns=(String)req.getParameter( "exportColumns");
         String fileName=req.getParameter( "fileName");
         String fileType=req.getParameter("fileType");
         commonExportData(fileName, exportColumns, datalist, response, fileType);
	}
	
	/**
	 * 加载excel基本信息
	 * @param response
	 * @param fileName 导入excel名称
	 * @throws Exception
	 */
	public static SXSSFWorkbook initExcel( HttpServletResponse response,String fileName,String fileType) throws Exception{
		SXSSFWorkbook wb =new SXSSFWorkbook();
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		fileName=StringUtils.isNotBlank(fileName)?fileName:String.valueOf(new Date().getTime());
		String fileName2 = new String(fileName.getBytes("gb2312"), "iso-8859-1");
		fileType=StringUtils.isBlank(fileType)?"xls":"xlsx";
		//文件名
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName2 + "."+fileType);
		response.setHeader("Pragma", "no-cache");
		return wb;
	}
	
	/**
	 * 设置表头信息
	 * @param wb
	 * @param ColumnsMapList
	 * @param sheetName
	 * @param sheetIndex
	 * @return
	 */
	@SuppressWarnings("rawtypes") 
	public static Sheet initSheet(SXSSFWorkbook wb, List<Map> columnsMapList,String sheetName,int sheetIndex){
		Sheet sheet = wb.createSheet();
		sheetName=StringUtils.isBlank(sheetName)?"sheet"+sheetIndex:sheetName;
		wb.setSheetName(sheetIndex, sheetName);
		sheet.setDefaultRowHeightInPoints(20);
		sheet.setDefaultColumnWidth((short) 16);
		//设置页脚
		Footer footer = sheet.getFooter();
		footer.setRight("Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages());
		//设置样式 表头
		CellStyle style1 = wb.createCellStyle();
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		Font font1 = wb.createFont();
		font1.setFontHeightInPoints((short) 13);
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style1.setFont(font1);
		Row row1 = sheet.createRow(0);
		row1.setHeightInPoints(20);
		for (int i = 0; i <columnsMapList.size(); i++) {
			Cell cell1 = row1.createCell(i);
			cell1.setCellType(HSSFCell.ENCODING_UTF_16);
			cell1.setCellValue(columnsMapList.get(i).get("title").toString());
			cell1.setCellStyle(style1);
		}

		return sheet;
	}
	
	/**
	 * 填充数据
	 * @param wb
	 * @param sheet
	 * @param dataMapList 数据项
	 * @param ColumnsMapList 表头
	 */
	@SuppressWarnings("rawtypes") 
	public static void fillSheetData(SXSSFWorkbook wb,Sheet sheet,List dataList, List<Map> columnsMapList)throws Exception{
		 List<Map> dataMapList= new ArrayList< Map>();
         Map map;
          if (dataList!= null&&dataList.size()>0){
             for (Object vo:dataList){
                 map= new HashMap();
                CommonUtil.object2MapWithoutNull(vo,map);
                dataMapList.add(map);
                     
             }
          }
		//设置样式 表头
				CellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				style.setWrapText(true);
				//填充数据
				Map cellDataMap;
				for (int j = 0; j < dataMapList.size(); j++) {
					Row row2 = sheet.createRow((j + 1)); // 第三行开始填充数据 
					 cellDataMap = dataMapList.get(j);
					for (int i = 0; i < columnsMapList.size(); i++) {
						Cell cell = row2.createCell(i);
						String cellValue = StringUtils.EMPTY;
						if (columnsMapList.get(i).get("field") != null) {
							String fieldString = String.valueOf(columnsMapList.get(i).get("field"));
							cellValue = String.valueOf(cellDataMap.get(fieldString));
						}
						cell.setCellValue(cellValue);
						cell.setCellStyle(style);
					}

				}
	}
	
	/**
	 * 输出excel
	 * @param wb
	 * @param response
	 * @throws Exception
	 */
	public static void responseExcel(SXSSFWorkbook wb,HttpServletResponse response) throws Exception{
		wb.write(response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
		wb.dispose();
	}

}
