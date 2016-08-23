package com.hc.scm.common.generator;

import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;

import com.hc.scm.common.model.ItgUserRoleModel;
import com.hc.scm.common.utils.CommonUtil;

/**
 * Description: 通用VO查询代码生成辅助工具类
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-19下午1:51:12
 */
public class GeneratorUtils {
	
    /**
	 * 通过Model对象所有的属性生成VO查询的mapperxml（工具方法）
	 * 支持String Integer Date自动转换。其它类型请自行修改
	 * 表名默认取自bean名，联表查询请自行修改
	 * @param bean
     * @param selectVoName 不填默认生成baseSelectListByVo，baseSelectOneModelByVo；传值必须包含关键字[SelectOneModelByVo]或[SelectListByVo]
	 * @param bean
	 * @return
	 */
    public static String generatorSimpleMapperXML(Object bean,String selectVoName){
    	String modelName=bean.getClass().getSimpleName();
    	if(StringUtils.isNotEmpty(selectVoName)&&!(selectVoName.contains("SelectOneModelByVo")||selectVoName.contains("SelectListByVo"))){
 			return " 	<!--Bean:"+modelName+"生成代码错误：参数selectVoName传值必须包含关键字[SelectOneModelByVo]或[SelectListByVo]。-->\n ";
 		}
    	
    	String tableName=CommonUtil.convertJaveBeanStrToUnderLine(CommonUtil.changeFirstCharUporLow(modelName.replace("Model", ""), 1));
    	modelName=modelName+"Map";
    	String col_list_name=modelName+"_Column_List";
    	StringBuffer sbf_resultMap=new StringBuffer();
    	StringBuffer sbf_select=new StringBuffer();
    	StringBuffer sbf_condition=new StringBuffer();
    	StringBuffer sbf_fcol_list=new StringBuffer();
    	sbf_resultMap.append(" 	<!-- Vo查询的Mapper xml自动生成，请自行调试修改使用。(特别注意resultMap的jdbcType及查询的表名，联表查询请添加表别名前缀)-->\n ");
    	sbf_resultMap.append(" 	<resultMap id=\""+modelName+"\" type=\""+bean.getClass().getName()+"\" >\n");
		
        Class<?> cls = bean.getClass();
        Field[] fields = cls.getDeclaredFields();
        String jdbcType="",fieldName="",fieldName_ul="",fieldType;
        int flen=fields.length;
        for (int i=1;i<flen;i++) {
            try {
            	Field field=fields[i];
                fieldName =field.getName();
                fieldName_ul=CommonUtil.convertJaveBeanStrToUnderLine(fieldName);
                fieldType=field.getType().getName();
                //System.out.println(fieldType);
                if("java.util.List".equals(fieldType)){
                	continue;
                }
                jdbcType=XmlConfigUtils.getString(fieldType);
                
                if(i==1){
                	sbf_resultMap.append(" 	 	<id column=\""+fieldName_ul+"\" property=\""+fieldName+"\" jdbcType=\""+jdbcType+"\" />\n");
                }
                else{
                	sbf_resultMap.append(" 	 	<result column=\""+fieldName_ul+"\" property=\""+fieldName+"\" jdbcType=\""+jdbcType+"\" />\n");
                }
                sbf_condition.append(" 	 	<if test=\""+fieldName+" != null\" >\n");
                sbf_condition.append(" 	 	 	and "+fieldName_ul+"=#{"+fieldName+"}\n");
                sbf_condition.append(" 	 	</if>\n");
                
                sbf_fcol_list.append(","+fieldName_ul);
            } catch (Exception e) {
                continue;
            }
        }
        
        sbf_resultMap.append(" 	</resultMap>\n\n");
        
        sbf_resultMap.append(" 	<sql id=\""+col_list_name+"\" >\n");
        sbf_resultMap.append(" 	 	"+sbf_fcol_list.toString().substring(1)+" \n");
        sbf_resultMap.append(" 	</sql>\n");
        
        if(StringUtils.isEmpty(selectVoName)){
        	sbf_select.append(" 	<sql id=\"baseSelectVoCondition\" >\n");
        	sbf_select.append(sbf_condition.toString());
        	sbf_select.append(" 	</sql>\n\n");
			sbf_select.append(" 	<select id=\"baseSelectListByVo\" resultMap=\""+modelName+"\" parameterType=\"map\">\n");        
			sbf_select.append(" 		 select <include refid=\""+col_list_name+"\" /> from "+tableName+" where 1=1\n");
			sbf_select.append(" 		 <include refid=\"baseSelectVoCondition\" />\n");
			sbf_select.append(" 	</select>   \n\n");
			sbf_select.append(" 	<select id=\"baseSelectOneModelByVo\"  resultMap=\""+modelName+"\" parameterType=\"map\">\n");          
			sbf_select.append("  		 select <include refid=\""+col_list_name+"\" /> from "+tableName+" where  1=1\n");
			sbf_select.append(" 		 <include refid=\"baseSelectVoCondition\" />\n");
			sbf_select.append(" 	</select>\n");   
        }
        else{
			sbf_select.append(" 	<select id=\""+selectVoName+"\" resultMap=\""+modelName+"\" parameterType=\"map\">\n");        
			sbf_select.append(" 		 select <include refid=\""+col_list_name+"\" /> from "+tableName+" where 1=1\n");
			sbf_select.append(sbf_condition.toString());
			sbf_select.append(" 	</select>   \n");
        }
        return sbf_resultMap.toString()+sbf_select.toString();
    }

    /**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(generatorSimpleMapperXML(new ItgUserRoleModel(),null));
		System.out.println(generatorSimpleMapperXML(new ItgUserRoleModel(),"baseSelectListXXXXXXByVo"));
		
	}

}