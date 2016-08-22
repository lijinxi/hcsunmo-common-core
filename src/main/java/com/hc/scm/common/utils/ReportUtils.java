package com.hc.scm.common.utils;

import java.awt.Color;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.general.FRFont;
import com.fr.io.exporter.ExcelExporter;
import com.fr.io.exporter.PDFExporter;
import com.fr.io.exporter.TextExporter;
import com.fr.io.exporter.WordExporter;
import com.fr.main.impl.WorkBook;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.Constants;
import com.fr.stable.WriteActor;

/**
 * 生成报表工具类
 * Description: 
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      zhu.liang
 * Createdate:  2015-4-16上午11:19:18
 *
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-4-16     	zhu.liang
 */
public class ReportUtils {
	
	//生成的报表文件服务器存放目录
	public static final String FILE_PATH = "/usr/local/apache-tomcat-7.0.53-8080/webapps/WebReport/report/"; 
	
	/**
	 * 生成报表标题
	 * @param nameStr
	 * @param sheet
	 * @return
	 */
	public static WorkSheet createHead(String nameStr,WorkSheet sheet){
		if(StringUtils.isNotBlank(nameStr)){
	    	String[] columArr = nameStr.split(";");
	    	for(int i=0;i<columArr.length;i++){
	    		TemplateCellElement cell1 = new DefaultTemplateCellElement(i, 0, columArr[i]);    
	    	    Style style = Style.getInstance();
	    	    FRFont frfont = FRFont.getInstance("Arial", 1, 9.0F, new Color(0,0,255));    
	    	    style = style.deriveFRFont(frfont);        
	    	    style = style.deriveHorizontalAlignment(Constants.LEFT);  
	    	    // 设置边框    
	    	    style = style.deriveBorder(Constants.LINE_THIN, Color.gray,    
	                    Constants.LINE_THIN, Color.gray, Constants.LINE_THIN,    
	                    Color.gray, Constants.LINE_THIN, Color.gray);  
	    	    // 设置背景  
	            ColorBackground background = ColorBackground.getInstance(new Color(240,240,240));  
	            style = style.deriveBackground(background);  
	    	    cell1.setStyle(style);    
	    	    sheet.addCellElement(cell1);      
	    	}	    	
	    }
		
		return sheet;
	}
	
	/**
	 * 生成报表内容
	 * @param columNames
	 * @param jsonData
	 * @param sheet
	 * @return
	 */
	public static WorkSheet createContent(String columNames,String jsonData,WorkSheet sheet){
		if(StringUtils.isNotBlank(jsonData)){
	    	List<HashMap> contentList = JSON.parseArray(jsonData,HashMap.class);
	    	if(contentList != null && contentList.size() > 0){
	    		int row = 1;
	    		for(int j=0;j<contentList.size();j++){
	    			HashMap map = contentList.get(j);
	    			int col = 0;
	    			String[] keyArr = columNames.split(";");
	    			for(String key : keyArr){
	    				TemplateCellElement cell = new DefaultTemplateCellElement(col, row, map.get(key));    
	    	    	    Style style = Style.getInstance();
	    	    	    FRFont frfont = FRFont.getInstance("Arial", 0, 7.0F, new Color(0,0,0));    
	    	    	    style = style.deriveFRFont(frfont);    
	    	    	    style = style.deriveHorizontalAlignment(Constants.LEFT);  
	    	    	    // 设置边框    
	    	            style = style.deriveBorder(Constants.LINE_THIN, Color.gray,    
	    	                    Constants.LINE_THIN, Color.gray, Constants.LINE_THIN,    
	    	                    Color.gray, Constants.LINE_THIN, Color.gray);  
	    	            if(row % 2 == 0){
	    	            	// 设置背景  
	    		            ColorBackground background = ColorBackground.getInstance(new Color(250,250,250));  
	    		            style = style.deriveBackground(background);  
	    	            }
	    	    	    cell.setStyle(style);    
	    	    	    sheet.addCellElement(cell);     
	    	    	    col ++;
	    			}
	    			
	    			row ++;
	    		}
	    	}
	    }
		
		return sheet;
	}
	
	/**
	 * 保存报表文件
	 * @param workbook
	 * @param fName
	 */
	public static void saveReportFile(WorkBook workbook,String fName){
		 OutputStream stream = null;
		    //导出到pdf文件
		    try {
		    	String fileName = FILE_PATH + fName;
				stream = new FileOutputStream(new File(fileName));
				if(fileName.endsWith(".pdf") || fileName.endsWith(".PDF")){
					PDFExporter exporter = new PDFExporter();
					exporter.export(stream, workbook.execute(new HashMap(), new WriteActor()));
				}else if(fileName.endsWith(".xls") || fileName.endsWith(".xlsx") || fileName.endsWith(".XLS") || fileName.endsWith(".XLSX")){
					ExcelExporter exporter = new ExcelExporter();
					exporter.export(stream, workbook.execute(new HashMap(), new WriteActor()));
				}else if(fileName.endsWith(".TXT") || fileName.endsWith(".txt")){
					TextExporter exporter = new TextExporter();
					exporter.export(stream, workbook.execute(new HashMap(), new WriteActor()));
				}else if(fileName.endsWith(".doc") || fileName.endsWith(".docx") || fileName.endsWith(".DOC") || fileName.endsWith(".DOCX")){
					WordExporter exporter = new WordExporter();
					exporter.export(stream, workbook.execute(new HashMap(), new WriteActor()));
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(stream != null){
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}

}
