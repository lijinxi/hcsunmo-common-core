package com.hc.scm.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 需要记录的表
 * Description: 
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      peng.hz
 * Createdate:  2015-4-20上午8:56:33
 *
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-4-20     	peng.hz
 */
public class LogTablesCache {
	
	private static List<String> havaLogTables=new ArrayList<String>();
	
	/**
	 * 拥用日志的表
	 * @return
	 */
	public static List<String> getLogTables(){
		if(havaLogTables.size()==0){
			String have_log_tables= PropertiesUtils.getPropertieValue("have_log_tables");
			if(StringUtils.isNotBlank(have_log_tables)){
				String [] tables=have_log_tables.split(",");
				for(String table:tables){
					havaLogTables.add(CommonUtil.
							changeFirstCharUporLow(CommonUtil.convertUnderLineStrToJaveBean(table),0));
									
				}
			}
			return havaLogTables;
		}
		return havaLogTables;
	}

}
