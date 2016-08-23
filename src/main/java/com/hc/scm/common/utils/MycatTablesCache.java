package com.hc.scm.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * mycat全局表
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
public class MycatTablesCache {
	
	private static List<String> mycatTables=new ArrayList<String>();
	
	/**
	 * 获得全局表
	 * @return
	 */
	public static List<String> getMycatTables(){
		if(mycatTables.size()==0){
			String mycat_tables= PropertiesUtils.getPropertieValue("mycat_tables");
			if(StringUtils.isNotBlank(mycat_tables)){
				String [] tables=mycat_tables.split(",");
				for(String table:tables){
					mycatTables.add(table);
				}
			}
			return mycatTables;
		}
		return mycatTables;
	}

}
