package com.hc.scm.common.report;

import com.hc.scm.common.utils.ReportUtils;
import com.fr.main.TemplateWorkBook;
import com.fr.main.impl.WorkBook;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.unit.OLDPIX;
import com.fr.web.WebletException;
import com.fr.web.core.Reportlet;
import com.fr.web.request.ReportletRequest;

/**
 * 多表网络程序报表模板
 * Description: 
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      zhu.liang
 * Createdate:  2015-4-16下午2:10:54
 *
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-4-16     	zhu.liang
 */
public class HcMultipleReportlet extends Reportlet {

	@Override
	public TemplateWorkBook createReport(ReportletRequest req)
			throws WebletException {
		WorkBook workbook = null;  
	    Integer count = Integer.valueOf(req.getParameter("count").toString());//报表个数
	    if(count != null && count.intValue() > 0){
	    	//创建一个WorkBook工作薄，在工作薄中插入一个WorkSheet      
		    workbook = new WorkBook();  
		    for(int i=0;i<count.intValue();i++){
		    	WorkSheet sheet = new WorkSheet();
			    sheet.setRowHeight(1, new OLDPIX(10.0F));     
			    String nameStr = req.getParameter("headNames"+i).toString();//报表显示的列名，多个列名用分号隔开,例如：项目编码；项目名称；启用状态
			    String columNames = req.getParameter("columNames"+i).toString();
			    String jsonData = req.getParameter("data"+i).toString();//报表内容,json格式的数据
			    
			    sheet = ReportUtils.createHead(nameStr, sheet);
			    sheet = ReportUtils.createContent(columNames, jsonData, sheet);
			    workbook.addReport(sheet);//报表内容
		    }
		    String fName = req.getParameter("fileName").toString();
		    ReportUtils.saveReportFile(workbook, fName);
	    }
	    
		return workbook;
	}

}
