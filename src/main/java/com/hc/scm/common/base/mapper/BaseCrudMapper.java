package com.hc.scm.common.base.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.hc.scm.common.utils.SimplePage;

/**
 * Description:  crud 公共Mapper类
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-3上午11:29:30
 * 
 *  @param <ModelType>
 */
public interface BaseCrudMapper {
	
    public int deleteByPrimaryKey(int id);

    public <ModelType> int insert(ModelType record);

    public <ModelType> int insertSelective(ModelType record);

    public <ModelType> ModelType selectByPrimaryKey(ModelType modelType);
    
    public <ModelType> List<ModelType> selectByParams(@Param("model")ModelType modelType,@Param("params")Map<String,Object> params);

    public <ModelType> int updateByPrimaryKeySelective(ModelType record);

    public <ModelType> int updateByPrimaryKey(ModelType record);
    
    
    public int selectCount(@Param("params")Map<String,Object> params);
    
    public <ModelType> List<ModelType> selectByPage(@Param("page") SimplePage page,@Param("orderByField") String orderByField,@Param("orderBy") String orderBy,@Param("params")Map<String,Object> params);
    
    public <ModelType> int deleteByPrimarayKeyForModel(ModelType record);

}
