package com.hc.scm.common.report;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.hc.scm.common.utils.ReportUtils;
import com.fr.base.Style;
import com.fr.base.background.ColorBackground;
import com.fr.general.FRFont;
import com.fr.io.exporter.ExcelExporter;
import com.fr.io.exporter.PDFExporter;
import com.fr.io.exporter.TextExporter;
import com.fr.io.exporter.WordExporter;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.report.cell.DefaultTemplateCellElement;
import com.fr.report.cell.TemplateCellElement;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.Constants;
import com.fr.stable.WriteActor;
import com.fr.stable.unit.OLDPIX;
import com.fr.web.WebletException;
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;

/**
 * 通用报表网络模板
 * Description: 
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      zhu.liang
 * Createdate:  2015-4-13下午1:51:42
 *
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-4-13     	zhu.liang
 */
public class HcCommonReportlet extends Reportlet {

	@Override
	public TemplateWorkBook createReport(ReportletRequest req)
			throws WebletException {
		//创建一个WorkBook工作薄，在工作薄中插入一个WorkSheet      
	    WorkBook workbook = new WorkBook();    
	    WorkSheet sheet = new WorkSheet();
	    //sheet.setColumnWidth(0, new OLDPIX(50.0F));    
	    sheet.setRowHeight(1, new OLDPIX(10.0F));     
	    String nameStr = req.getParameter("headNames").toString();//报表显示的列名，多个列名用分号隔开,例如：项目编码；项目名称；启用状态
	    String columNames = req.getParameter("columNames").toString();
	    String jsonData = req.getParameter("data").toString();//报表内容,json格式的数据
	    
	    sheet = ReportUtils.createHead(nameStr, sheet);
	    sheet = ReportUtils.createContent(columNames, jsonData, sheet);
	    workbook.addReport(sheet);//报表内容
	    
	    String fName = req.getParameter("fileName").toString();
	    ReportUtils.saveReportFile(workbook, fName);
	    
		return workbook;
	}

}
