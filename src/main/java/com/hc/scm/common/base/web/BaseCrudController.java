package com.hc.scm.common.base.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hc.scm.common.base.service.BaseCrudService;
import com.hc.scm.common.constans.SysConstans;
import com.hc.scm.common.enums.CommonOperatorEnum;
import com.hc.scm.common.exception.ValidException;
import com.hc.scm.common.model.AuditRequest;
import com.hc.scm.common.model.CustomerRequest;
import com.hc.scm.common.model.ExportCustomerReqest;
import com.hc.scm.common.model.ImportRequest;
import com.hc.scm.common.model.ImportResolve;
import com.hc.scm.common.model.ImportValidationCondition;
import com.hc.scm.common.model.MasterRequest;
import com.hc.scm.common.model.QueryConditionReq;
import com.hc.scm.common.model.RequestCommonList;
import com.hc.scm.common.model.SystemUser;
import com.hc.scm.common.pushlet.PushMessage;
import com.hc.scm.common.pushlet.PushMessagePool;
import com.hc.scm.common.utils.CommonUtil;
import com.hc.scm.common.utils.ConditionUtils;
import com.hc.scm.common.utils.ExcelExport;
import com.hc.scm.common.utils.ExcelUtils;
import com.hc.scm.common.utils.HttpUtils;
import com.hc.scm.common.utils.PropertiesUtils;
import com.hc.scm.common.utils.ResultModel;
import com.hc.scm.common.utils.SignUtils;
import com.hc.scm.common.utils.SpringComponent;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * Description: 公共controller
 * All rights Reserved, Designed Byhc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      wugy
 * Createdate:  2015-3-3下午12:37:23
 */
public abstract class BaseCrudController<ModelType> {
	private static Logger logger = LoggerFactory.getLogger(BaseCrudController.class);
	private BaseCrudService baseService;

	@Value("${export.max.row}")
	private  int  exportMaxrow; 
	
	@Value("${report.server.url}")
	private  String reportServerUrl; //报表服务器url,例如：http://172.17.209.67:8080/WebReport/ReportServer
	
	@Value("${report.file.path}")
	private  String reportFilePath;//生成的报表文件路径
	
	@PostConstruct
	protected void initConfig() {
		this.baseService = this.init();
	}

	protected abstract BaseCrudService init();
	
	
	/**
	 * 添加
	 * @param modelType
	 * @return
	 */
    @RequestMapping("/add.json")
    @ResponseBody
    public Map<String,Object> add(ModelType modelType) throws Exception {
    	Map<String, Object> resultMap =new HashMap<String, Object>();
    	ResultModel resultModel =new ResultModel();
    	
    	baseService.add(modelType);
		
		resultMap.put("result", resultModel);
        return resultMap;
    }
    
    /**
     * 删除
     * @param modelType
     * @return
     */
    @RequestMapping("/deleteById.json")
    @ResponseBody
    public Map<String,Object> deleteById(ModelType modelType) throws Exception {
    	Map<String, Object> resultMap =new HashMap<String, Object>();
    	ResultModel resultModel =new ResultModel();
    	baseService.deleteById(modelType);
		resultMap.put("result", resultModel);
        return resultMap;
    }

    /**
     * 修改
     * @param modelType
     * @return
     */
    @RequestMapping("/modifyById.json")
    @ResponseBody
    public Map<String,Object> modifyById(ModelType modelType) throws Exception {
    	Map<String, Object> resultMap =new HashMap<String, Object>();
    	ResultModel resultModel =new ResultModel();
    	baseService.modifyById(modelType);
		resultMap.put("result", resultModel);
        return resultMap;
    }

    /**
     * 查询对象
     * @param modelType
     * @return
     */
    @RequestMapping("/get.json")
    @ResponseBody
    public Map<String,Object> get(ModelType modelType) throws Exception {
        Map<String, Object> resultMap =new HashMap<String, Object>();
        ResultModel resultModel =new ResultModel();
        resultMap.put("entity", baseService.findById(modelType));
		resultMap.put("result", resultModel);
		return resultMap;
    }

    /**
     * 分页获取对象list
     * @param req
     * @param model
     * @return
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping("/list.json")
    @ResponseBody
    public Map<String,Object> listByPage(HttpServletRequest req, Model model) throws Exception{
        Map<String, Object> resultMap =new HashMap<String, Object>();
        ResultModel resultModel =new ResultModel();
        int pageNum = StringUtils.isEmpty(req.getParameter("pageNum")) ? 1 : Integer.parseInt(req.getParameter("pageNum"));
        int pageSize = StringUtils.isEmpty(req.getParameter("pageSize")) ? 10 : Integer.parseInt(req.getParameter("pageSize"));
        String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
        String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
        Map<String, Object> params = builderParams(req, model);
        PageHelper.startPage(pageNum, pageSize);
        List<ModelType> list = this.baseService.findByPage(null, CommonUtil.convertJaveBeanStrToUnderLine(sortColumn), sortOrder, params);
        resultMap.put("totalCount",((Page)list).getTotal());
        resultMap.put("list",list);
		resultMap.put("result", resultModel);
        return resultMap;
    }
    
    /**
     * 查询对象VO
     * @param selectVoName xxxxMapper.xml调用对应的select方法；传值必须包含关键字“SelectOneModelByVo”
     * 		如：selectVoName=mySelectOneModelByVoTest则调用<select id="mySelectOneModelByVoTest" resultMap="ModelMap" parameterType="map">
     * 		不传该参数则默认调用<select id="baseSelectOneModelByVo" resultMap="ModelMap" parameterType="map">
     * @return
     */
    @RequestMapping("/getvo.json")
    @ResponseBody
    public Map<String,Object> getVo(HttpServletRequest req, Model model) throws Exception {
    	String selectVoName =req.getParameter("selectVoName");
    	String mapperClassType =getModelMapperName(req);
        if(StringUtils.isEmpty(mapperClassType)){
 			throw new ValidException("参数错误。[mapperClassType]");
 		}
        else if(StringUtils.isNotEmpty(selectVoName)&&!selectVoName.contains("SelectOneModelByVo")){
 			throw new ValidException("参数selectVoName传值必须包含关键字[SelectOneModelByVo]");
 		}
        
        Map<String, Object> resultMap =new HashMap<String, Object>();
        ResultModel resultModel =new ResultModel();
        Map<String, Object> params = builderParams(req, model);
        params.put("mapperClassType", mapperClassType);
        if(StringUtils.isNotEmpty(selectVoName)){
        	params.put("selectVoName", selectVoName);
        }
        resultMap.put("entity", baseService.findVoByParams(params));
		resultMap.put("result", resultModel);
		return resultMap;
    }
    
    /**
     * 分页获取VO对象list
     * @param req
     * @param model
     * @param selectVoName xxxxMapper.xml调用对应的select方法；；传值必须包含关键字“SelectListByVo”
     * 		如：selectVoName=mySelectListByVoTest则调用<select id="mySelectListByVoTest" resultMap="ModelMap" parameterType="map">
     * 		不传该参数则默认调用<select id="baseSelectListByVo" resultMap="ModelMap" parameterType="map">
     * @return
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping("/listvo.json")
    @ResponseBody
    public Map<String,Object> listVoByPage(HttpServletRequest req, Model model) throws Exception {
        Map<String, Object> resultMap =new HashMap<String, Object>();
        ResultModel resultModel =new ResultModel();
        int pageNum = StringUtils.isEmpty(req.getParameter("pageNum")) ? 1 : Integer.parseInt(req.getParameter("pageNum"));
        int pageSize = StringUtils.isEmpty(req.getParameter("pageSize")) ? 10 : Integer.parseInt(req.getParameter("pageSize"));
        String sortColumn = StringUtils.isEmpty(req.getParameter("sort")) ? "" : String.valueOf(req.getParameter("sort"));
        String sortOrder = StringUtils.isEmpty(req.getParameter("order")) ? "" : String.valueOf(req.getParameter("order"));
        Map<String, Object> params = this.getParamsForListVo(req, model);
        PageHelper.startPage(pageNum, pageSize);
        List<Object> list = this.baseService.findVoByPage(null, CommonUtil.convertJaveBeanStrToUnderLine(sortColumn), sortOrder, params);
        resultMap.put("totalCount",((Page)list).getTotal());
        resultMap.put("list",list);
		resultMap.put("result", resultModel);
        return resultMap;
    }
    
    /**
     * 全部获取VO对象list
     * @param req
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping("/listvoAll.json")
    @ResponseBody
    public Map<String,Object> listVoAll(HttpServletRequest req, Model model) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultModel resultModel = new ResultModel();
		Map<String, Object> params = this.getParamsForListVo(req, model);
		List<Object> list = this.baseService.findVoByPage(null, null, null,
				params);
		resultMap.put("list", list);
		resultMap.put("result", resultModel);
		return resultMap;
    }
    
    /**
     * 通过请求URI返回mapper名称
     * 如：/hcc-web/itg_module_list/listvo.json 返回：ItgModuleListMapper
     * @param req
     * @return
     */
    public String getModelMapperName(HttpServletRequest req){
    	/* @param mapperClassType mapper名称参数，可选。如：ItgModuleListMapper、ItgDepartmentMapper
        * 		     不传则系统默认从请求路径获取
        *		    系统自动根据该名称定位mapper。如：ItgModuleListMapper.baseSelectListByVo、ItgDepartmentMapper.baseSelectListByVo
    	*/
    	 String mapperClassType=req.getParameter("mapperClassType");
    	 if(StringUtils.isEmpty(mapperClassType)){
    		 String modelpath=req.getRequestURI().split("/")[2];
    		 modelpath=CommonUtil.changeFirstCharUporLow(CommonUtil.convertUnderLineStrToJaveBean(modelpath),0);
    		 mapperClassType = modelpath+"Mapper";
    	 }
		return mapperClassType;
    }
    
    /**
     * 获取参数map
     * @param req
     * @param model
     * @return
     */
    public Map<String, Object> getParamsForListVo(HttpServletRequest req, Model model){
        String mapperClassType =getModelMapperName(req);
        String selectVoName =req.getParameter("selectVoName");
        if(StringUtils.isEmpty(mapperClassType)){
			throw new ValidException("参数错误。[mapperClassType]");
		}
        else if(StringUtils.isNotEmpty(selectVoName)&&!selectVoName.contains("SelectListByVo")){
 			throw new ValidException("参数selectVoName传值必须包含关键字[SelectListByVo]");
 		}
        Map<String, Object> params = builderParams(req, model);
        params.put("mapperClassType", mapperClassType);
        if(StringUtils.isNotEmpty(selectVoName)){
        	params.put("selectVoName", selectVoName);
        }
		return params;
    }

    /**
     * 查询全部对象list
     * @param req
     * @param model
     * @return
     */
	@RequestMapping("/listAll.json")
    @ResponseBody
    public Map<String,Object> listAll(HttpServletRequest req, Model model,ModelType modelType) throws Exception {
        Map<String, Object> resultMap =new HashMap<String, Object>();
        ResultModel resultModel =new ResultModel();
        Map<String, Object> params = builderParams(req, model);
        System.out.println("listAll:"+modelType);
        List<ModelType> list = this.baseService.findByBiz(modelType, params);
        resultMap.put("list",list);
		resultMap.put("result", resultModel);
        return resultMap;
    }
    
    public Map<String, Object> builderParams(HttpServletRequest req,
			Model model) {
		Map<String, Object> retParams = new HashMap<String,Object>(req.getParameterMap().size());
		Map<String, String[]> params = req.getParameterMap();
		if (null != params && params.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for (Entry<String, String[]> p : params.entrySet()) {
				if (null == p.getValue() || StringUtils.isEmpty(p.getValue().toString()))
					continue;
				// 只转换一个参数，多个参数不转换
				String values[] = (String[]) p.getValue();
				String match = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
				if (values[0].matches(match)) {
					try {
						retParams.put(p.getKey(), sdf.parse(values[0]));
					} catch (ParseException e) {
						retParams.put(p.getKey(), values);
						e.printStackTrace();
					}
				}else if(p.getKey().equals("queryCondition")){
					List<QueryConditionReq> list=JSON.parseArray(values[0], QueryConditionReq.class);
					retParams.put(p.getKey(), ConditionUtils.getQueryCondition(list,retParams));
//					retParams.put(p.getKey(), model.asMap().get("queryCondition"));
				} else {
					retParams.put(p.getKey(), values[0]);
				}
			}
		}
		return retParams;
	}
    
    /**
     * 批量操作
     * @param req
     * @param reqData
     * @return
     */
    @RequestMapping("/batchOperate.json")
	@ResponseBody
	public Map<String,Object> batchOperate(HttpServletRequest req,@RequestBody RequestCommonList<ModelType> reqData,ModelType modeltype) throws Exception{
	   Map<String, Object> resultMap =new HashMap<String, Object>();
	   ResultModel resultModel =new ResultModel();
	   Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
		if(reqData.getDeletelist()!=null && reqData.getDeletelist().size()>0){
			params.put(CommonOperatorEnum.DELETED, reqData.getDeletelist());
		}
		if(reqData.getUpdatelist()!=null && reqData.getUpdatelist().size()>0){
			params.put(CommonOperatorEnum.UPDATED,reqData.getUpdatelist());
		}
		if(reqData.getInsertlist()!=null && reqData.getInsertlist().size()>0){
			params.put(CommonOperatorEnum.INSERTED, reqData.getInsertlist());
		}
		SystemUser systemUser=getCurrentUser(req);
		if(params.size()>0){
			baseService.save(params,modeltype,systemUser,reqData.getBillType());
		}
		resultMap.put("result", resultModel);
	    return resultMap;

  }
    
    /**
     * 批量保存
     * @param req
     * @param ModelType
     * @return
     */
    @RequestMapping("/listsave.json")
    @ResponseBody
	public Map<String, Object> listsave(HttpServletRequest req,ModelType modelType) throws Exception{
	 Map<String, Object> resultMap =new HashMap<String, Object>();
	   ResultModel resultModel =new ResultModel();
	   String deletedList = StringUtils.isEmpty(req.getParameter("deletelist")) ? "" : req.getParameter("deletedList");
		String upadtedList = StringUtils.isEmpty(req.getParameter("updatelist")) ? "" : req.getParameter("upadtedList");
		String insertedList = StringUtils.isEmpty(req.getParameter("insertlist")) ? "" : req.getParameter("insertedList");
		String billType=req.getParameter("billType");
		Map<CommonOperatorEnum, List<ModelType>> params = new HashMap<CommonOperatorEnum, List<ModelType>>();
		if (StringUtils.isNotEmpty(deletedList)) {
			List<ModelType> oList = JSON.parseObject(deletedList, new TypeReference<List<ModelType>>(){});
			params.put(CommonOperatorEnum.DELETED, oList);
		}
		
		if (StringUtils.isNotEmpty(upadtedList)) {
			List<ModelType> oList = JSON.parseObject(upadtedList, new TypeReference<List<ModelType>>(){});
			params.put(CommonOperatorEnum.UPDATED, oList);
		}
		if (StringUtils.isNotEmpty(insertedList)) {
			List<ModelType> oList = JSON.parseObject(insertedList, new TypeReference<List<ModelType>>(){});
			params.put(CommonOperatorEnum.INSERTED, oList);
		}
		SystemUser systemUser=getCurrentUser(req);
		if(params.size()>0){
			baseService.save(params,modelType,systemUser,billType);
		}
		else{
			throw new ValidException("请检查参数。");
		}
		resultModel.setMsg("操作成功");
		resultMap.put("result", resultModel);
	    return resultMap;
	}

    /**
     * 获得当前用户信息
     * @param req
     * @return
     */
    protected SystemUser getCurrentUser(HttpServletRequest req){
    	SystemUser user=null;
    	user=(SystemUser)req.getSession().getAttribute(SysConstans.SESSION_USER);
    	if(user==null){
    		//通过org.jasig.cas.client.util.AssertionHolder来获取用户的登录名。 如AssertionHolder.getAssertion().getPrincipal().getName()。
    		//通过HttpServletRequest的getRemoteUser()方法获得SSO登录用户的登录名
        	/*String userCode=req.getRemoteUser();//需要打开cas单点配置才能取到
        	if(StringUtils.isNotEmpty(userCode)){
        		Map<String,Object> params=new HashMap<String,Object>();
        		params.put("userCode", userCode);
        		String userJson=JSON.toJSONString(baseService.findByParams(params));//数据库取用户信息
        		user=(SystemUser) JSON.parseObject(userJson,SystemUser.class);
        		user.setPassword("");
        	}
        	else{
        	}*/
        	user = new SystemUser();
        	user.setUserId(1);
        	user.setUserCode("admin");
        	user.setUserName("超级管理员");
        	//req.getSession().setAttribute(SysConstans.SESSION_USER, user);
    	}
    	return user;
    }

    /**
     * 保存主从(一个主表对一个从表)
     * @param req
     * @return
     */
    @SuppressWarnings("unused")
	@RequestMapping("/addMasterCustomer.json")
    @ResponseBody
   public  Map<String, Object>  addMasterCustomer(HttpServletRequest req,ModelType modelType)throws Exception{
	 Map<String, Object> resultMap =new HashMap<String, Object>();
	 ResultModel resultModel =new ResultModel();
      try {
       String masterName=req.getParameter("masterName");
	   String customerName=req.getParameter("customerName");
	   String masterJson=req.getParameter("masterJson");
	   String customerJsonList=req.getParameter("customerJsonList");
	   String idField=req.getParameter("idField");
	   customerName=CommonUtil.changeFirstCharUporLow(customerName, 0);
	   masterName=CommonUtil.changeFirstCharUporLow(masterName, 0);
	   String modelClassName= modelType.getClass().getName();
	   modelClassName=modelClassName.substring(0,modelClassName.lastIndexOf("."));
	   String   masterNameAllPath=modelClassName+"."+masterName;
	   String customerNameAllPath=modelClassName+"."+customerName;
	   @SuppressWarnings("static-access")
	   Class<?> masterClazz= this.getClass().getClassLoader().getClass().forName(masterNameAllPath);
	   @SuppressWarnings("static-access")
	   Class<?> customerClazz= this.getClass().getClassLoader().getClass().forName(customerNameAllPath);
	   Object objm=JSON.parseObject(masterJson,modelType.getClass());
	   SystemUser systemUser=getCurrentUser(req);
	   this.baseService.addMasterCustomer(JSON.parseObject(masterJson, masterClazz), 
			   JSON.parseArray(customerJsonList, customerClazz),idField,customerName,systemUser);
    	}catch (Exception e) {
	   		resultModel.setResultCode("9009");
			resultModel.setMsg("系统异常");
			logger.error("error:", e);
		}
	  resultMap.put("result", resultModel);
	  return resultMap;
   }
    
    
    /**
     * 保存主从(一个主表对多个从表)
     * @param req
     * @return
     */
	@RequestMapping("/saveMasterCustomerList.json")
    @ResponseBody
   public  Map<String, Object>  saveMasterCustomerList(HttpServletRequest req,ModelType modelType,@RequestBody MasterRequest masterRequest)throws Exception{
      Map<String, Object> resultMap =new HashMap<String, Object>();
      ResultModel resultModel =new ResultModel();
	   String customerListData=masterRequest.getCustomerListData();//从表数据json格式的list
	   String masterJson=masterRequest.getMasterJson();//主表json数据
//	   String idField=masterRequest.getIdFieldName();//主表id名称
//	   String operateType=masterRequest.getOperateType();//操作类型
	   String modelClassName= modelType.getClass().getName();
	   modelClassName=modelClassName.substring(0,modelClassName.lastIndexOf("."));//获得实体空间名
	   List<CustomerRequest> listData=JSON.parseArray(customerListData, CustomerRequest.class);
	   SystemUser systemUser=getCurrentUser(req);
	   Object masterId= baseService.saveMasterCustomerList(JSON.parseObject(masterJson, modelType.getClass())
			   , listData, 
			   systemUser, modelClassName, masterRequest);
	  resultModel.setMsg("操作成功");
	  resultMap.put("masterId", masterId);
	  resultMap.put("result", resultModel);
	  return resultMap;
   }
	
	 /**
     * 审核
     * @param req
     * @return
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/audit.json")
    @ResponseBody
   public  Map<String, Object>  audit(HttpServletRequest req,ModelType modelType,@RequestBody AuditRequest auditRequest)throws Exception{
		 Map<String, Object> resultMap =new HashMap<String, Object>();
	      ResultModel resultModel =new ResultModel();
	      try{
		  	  List list = JSON.parseArray(auditRequest.getAuditModelList(), modelType.getClass());
		      this.baseService.audit(list, getCurrentUser(req));
	      }catch (Exception e) {
	    	  resultModel.setMsg(e.getMessage());
	    	  resultModel.setResultCode("1");
		  	  resultMap.put("result", resultModel);
		  	  logger.error(e.toString(), e);
			  return resultMap;
		}
	      resultModel.setMsg("审核成功");
	  	  resultMap.put("result", resultModel);
		  return resultMap;
	}
	
	/**
	 * 尺码横排的保存
	 * @param req
	 * @param modelType
	 * @param sizeHorizontalReq
	 * @return
	 */
	@RequestMapping("/saveSizeHorizontalData.json")
    @ResponseBody
	public Map<String, Object> saveSizeHorizontalData(HttpServletRequest req,ModelType modelType,@RequestBody MasterRequest masterRequest) throws Exception{
		  Map<String, Object> resultMap =new HashMap<String, Object>();
	      ResultModel resultModel =new ResultModel();
		   String masterJson=masterRequest.getMasterJson();//主表json数据
		   String modelClassName= modelType.getClass().getName();
		   modelClassName=modelClassName.substring(0,modelClassName.lastIndexOf("."));//获得实体空间名
	      Object masterObj=JSON.parseObject(masterJson, modelType.getClass());
	      Object masterId= this.baseService.saveSizeHorizontal(masterObj, masterRequest, modelClassName, getCurrentUser(req));
	      resultModel.setMsg("操作成功");
		  resultMap.put("masterId", masterId);
		  resultMap.put("result", resultModel);
		  return resultMap;
	}
    
    /**
	 * 单表导出
	 * @param modelType
	 * @param req
	 * @param model
	 * @param response
	 */
	@RequestMapping(value = "/do_export")
	public void doExport(ModelType modelType,HttpServletRequest req,Model model,HttpServletResponse response) throws Exception{
		 Map<String,Object> params=builderParams(req, model);
              try {
	               if(StringUtils.isNotEmpty(req.getParameter("pageNum"))){
	            	   int pageNum= Integer.parseInt(req.getParameter("pageNum"));
	   				   int pageSize = StringUtils.isEmpty(req.getParameter("pageSize")) ? 
	   						   10 : Integer.parseInt(req.getParameter("pageSize"));
	            	   PageHelper.startPage(pageNum, pageSize);
	               }else{
	            	   PageHelper.startPage(1, exportMaxrow);
	               }
	               List<ModelType> list = baseService.findByPage(null, "", "", params);
	               //导出excel文件
	               ExcelExport.commonExportData(list, response, req);
               } catch (Exception e) {
            	   logger.error(e.toString());
               }
	}
	
	
	/**
	 * 单据从表导出
	 * @param modelType
	 * @param req
	 * @param model
	 * @param response
	 */
	@RequestMapping(value = "/do_master_customer_export")
	public void doMasterCustomerExport(ModelType modelType,HttpServletRequest req,
			Model model,HttpServletResponse response) throws Exception{
		req.setCharacterEncoding("utf-8");
		String fileName=req.getParameter("fileName");
		String customerJsonList=req.getParameter("customerJsonList");
		String fileType=req.getParameter("fileType");
		List<ExportCustomerReqest> customerList=JSON.parseArray(customerJsonList, ExportCustomerReqest.class);
		SXSSFWorkbook wb=ExcelExport.initExcel(response, fileName,fileType);
		String modelClassName= modelType.getClass().getName();
		modelClassName=modelClassName.substring(0,modelClassName.lastIndexOf(".")+1);//获得实体空间名
		int sheetIndex=0;
		@SuppressWarnings("unused")
		Class<?> clazz;
		String masterNameAllPath;
		BaseCrudService baseCrudService;
		@SuppressWarnings("rawtypes")
		Map params;
		for(ExportCustomerReqest exportCustomerReqest:customerList){
			  @SuppressWarnings("rawtypes")
			  List<Map> columnsMapList=JSON.parseArray(exportCustomerReqest.getColumnsList(), Map.class);
			  Sheet sheet= ExcelExport.initSheet(wb, columnsMapList, exportCustomerReqest.getSheetName(), sheetIndex);
			  masterNameAllPath=modelClassName+CommonUtil.changeFirstCharUporLow(exportCustomerReqest.getCustomerName(), 0);
			  clazz= Class.forName(masterNameAllPath);//获得从表对象
			  params= JSON.parseObject(exportCustomerReqest.getCustomerModle(), Map.class);
			  String customerNameService=CommonUtil.changeFirstCharUporLow(exportCustomerReqest.getCustomerName(), 1)+"Service";
			  baseCrudService=(BaseCrudService)SpringComponent.getBean(customerNameService);
			  PageHelper.startPage(1, exportMaxrow);
			  @SuppressWarnings({ "rawtypes", "unchecked"})
			  List listdata=baseCrudService.findByPage(null, null, null, params);
			  ExcelExport.fillSheetData(wb, sheet, listdata, columnsMapList);
			  sheetIndex++;
		}
		ExcelExport.responseExcel(wb, response);
	}
	
	
	/**
    * 导入excel
    * @param req
    * @param modelType
    * @param colNames  导入属性名 （多个导入属性名用逗号隔开）
    * @param mustArray 导入是否必须（ true|| false 多个导入属性名用逗号隔开）
    * @param mainKey  主key判断是否重复用 (多个用逗号隔开)
    * @return
    */
	@SuppressWarnings("unchecked")
	@RequestMapping("/importExcel.json")
    @ResponseBody
    public Map<String,Object> importExcel(HttpServletRequest req, ModelType modelType,ImportRequest importRequest)throws Exception {
    	 Map<String, Object> resultMap =new HashMap<String, Object>();
         ResultModel resultModel =new ResultModel();
         String[] colNames=importRequest.getColNames().split(",");
         String[] mustArray=importRequest.getMustArray().split(",");
         if(colNames.length!=mustArray.length){
        	 resultModel.setMsg("参数错误");
			 resultModel.setResultCode("9009");
	     }else{
	         String[] mainKey=(req.getParameter("mainKey")==null)?null:req.getParameter("mainKey").split(",");
	         String  objJson=req.getParameter("objJson");
	         int sheetIdx=importRequest.getSheetIdx()==null?0:importRequest.getSheetIdx().intValue();
	         int firstLineIdx=importRequest.getFirstLineIdx()==null?1:importRequest.getFirstLineIdx().intValue();
	         List<ImportValidationCondition> listCondition=JSON.parseArray(importRequest.getValidationConditions(), ImportValidationCondition.class);
	         ImportResolve<ModelType> importResolve = new ImportResolve<ModelType>();
	         importResolve=(ImportResolve<ModelType>) ExcelUtils.getData(req,
	        		 sheetIdx,firstLineIdx, colNames, mustArray, mainKey, modelType.getClass(),objJson);
			 if(StringUtils.isNotBlank(importResolve.getErrorMsg() )&& importRequest.getIsValidateAll().equals("Y")){
			 }else{
					 this.baseService.importExcel(importResolve, 
							 listCondition, this.getCurrentUser(req), importRequest.getIsValidateAll());
			 }
			 if( StringUtils.isNotEmpty(importResolve.getErrorMsg())){
				 resultModel.setMsg(importResolve.getErrorMsg());
			 }else{
				 resultModel.setMsg("导入成功");
			 }
	     }
//		 resultMap.put("errorMsg", importResolve.getErrorMsg());
//		 resultMap.put("suceesMsg", StringUtils.isNotEmpty(importResolve.getSuccesMsg())?importResolve.getSuccesMsg():"");
     	 resultMap.put("result", resultModel);
         return resultMap;
    }
	
	/**
	 * 统一 异常处理返回
	 * 继承了BaseCrudController的controller无需再 try catch
	 * @param e
	 * @param request
	 * @return
	 */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public  Map<String, Object> exception(Exception e, HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> resultMap =new HashMap<String, Object>();
    	ResultModel resultModel =new ResultModel();
    	resultModel.setResultCode("9009");
    	if(e instanceof ValidException){
    		resultModel.setMsg("验证不通过：" + e.getMessage());
    	}
    	else{
    		String returnErrorData=PropertiesUtils.getString("return.error.data", "true");
    		if("true".equalsIgnoreCase(returnErrorData)){
        		resultModel.setRetData("Error:"+e.getMessage()); //业务系统内部异常的获取错误堆栈
    		}
    		resultModel.setMsg("系统内部异常，请联系管理员");
    		
    		logger.error("error:", e);
    	}
    	
		resultMap.put("result", resultModel);
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());	//异常统一返回500状态
        return resultMap;
    }
    
    /**
     * 公共的打印接口,获取报表打印地址
     * @param req
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/do_print.json")
    @ResponseBody
	public String doPrint(HttpServletRequest req,HttpServletResponse response) throws Exception{
    	String headNames = req.getParameter("headNames");//报表标题名称列表
    	String columNames = req.getParameter("columNames");//排好序的列字段
    	String fileSuffix = req.getParameter("fileSuffix");//获取输出文件后缀
    	Map<String, String> params = new HashMap<String,String>();
    	params.put("reportlet", "com.hc.scm.common.report.hcChcReportlet");
    	params.put("headNames", headNames);
    	params.put("columNames", columNames);
    	String fileName = "report_"+System.currentTimeMillis()+".pdf";
    	if("doc".equalsIgnoreCase(fileSuffix) || "docx".equalsIgnoreCase(fileSuffix)){
    		fileName = "report_"+System.currentTimeMillis()+".doc";
    	}else if("xls".equalsIgnoreCase(fileSuffix) || "xlsx".equalsIgnoreCase(fileSuffix)){
    		fileName = "report_"+System.currentTimeMillis()+".xls";
    	}else if("txt".equalsIgnoreCase(fileSuffix)){
    		fileName = "report_"+System.currentTimeMillis()+".txt";
    	}	
    	params.put("fileName", fileName);
    	
    	String data = req.getParameter("data");//报表内容数据接口url
    	JSONObject jsonObj = JSON.parseObject(data);
    	if(jsonObj != null){
    		String listJson = jsonObj.getString("list");
    		params.put("data", listJson);
    	}
    	
    	HttpUtils.post(reportServerUrl, params, 10, HTTP.UTF_8);
    	String pdfFileUrl = reportFilePath + fileName;
    	
    	/*ServletOutputStream out= response.getOutputStream();
    	response.setContentType("application/pdf");
    	response.setHeader( "Content-disposition", "attachment;filename=" +"report.pdf");//下载时的文件名。
    	URL url = new URL(pdfFileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
    	BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
    	BufferedOutputStream bos = new BufferedOutputStream(out);
    	byte[] buff = new byte[2048];
        int bytesRead;
	    while(-1 != (bytesRead = bis.read(buff,0,buff.length))){
	    	bos.write(buff, 0, bytesRead);
	    }
	    
	    if(bos != null){
	    	bos.close();
	    }
	    if(bis != null){
	    	bis.close();
	    }
	    if(out != null){
	    	out.close();
	    }*/
	    
    	return pdfFileUrl;
	}
    
    /**
     * 多表打印接口，建议报表个数不要超过2个
     * @param req
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/do_multiple_print.json")
    @ResponseBody
	public String doMultiplePrint(HttpServletRequest req,HttpServletResponse response) throws Exception{
    	String pdfFileUrl = null;
    	Integer count = Integer.valueOf(req.getParameter("count").toString());//报表个数
    	if(count != null && count.intValue() > 0){
    		String fileSuffix = req.getParameter("fileSuffix");//获取输出文件后缀
    		Map<String, String> params = new HashMap<String,String>();
    		params.put("count", count.toString());
        	params.put("reportlet", "com.hc.scm.common.report.hcMulthceportlet");
        	String fileName = "report_"+System.currentTimeMillis()+".pdf";
        	if("doc".equalsIgnoreCase(fileSuffix) || "docx".equalsIgnoreCase(fileSuffix)){
        		fileName = "report_"+System.currentTimeMillis()+".doc";
        	}else if("xls".equalsIgnoreCase(fileSuffix) || "xlsx".equalsIgnoreCase(fileSuffix)){
        		fileName = "report_"+System.currentTimeMillis()+".xls";
        	}else if("txt".equalsIgnoreCase(fileSuffix)){
        		fileName = "report_"+System.currentTimeMillis()+".txt";
        	}	
        	params.put("fileName", fileName);
        	for(int i=0;i<count.intValue();i++){
        		String headNames = req.getParameter("headNames"+i);//报表标题名称列表
            	String columNames = req.getParameter("columNames"+i);//排好序的列字段
            	params.put("headNames"+i, headNames);
            	params.put("columNames"+i, columNames);
            	String data = req.getParameter("data"+i);//报表内容数据接口url
            	JSONObject jsonObj = JSON.parseObject(data);
            	if(jsonObj != null){
            		String listJson = jsonObj.getString("list");
            		params.put("data"+i, listJson);
            	}
        	}
        	
        	HttpUtils.post(reportServerUrl, params, 10, HTTP.UTF_8);
        	pdfFileUrl = reportFilePath + fileName;
    	}else {
    		throw new ValidException("参数错误。[count]");
    	}
    	
    	return pdfFileUrl;
	}
    
    /**
     * 给指定用户发送短信接口
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("/sendMsg.json")
    @ResponseBody
    public Map<String,Object> sendMsg(HttpServletRequest req) throws Exception {
    	Map<String, Object> resultMap =new HashMap<String, Object>();
    	ResultModel resultModel =new ResultModel();
    	PushMessage message = new PushMessage();
    	String msgId = req.getParameter("id");
    	if(StringUtils.isNoneBlank(msgId)){
    		message.setId(Long.valueOf(msgId));
    	}else{
    		message.setId(System.currentTimeMillis());
    	}
    	System.out.println("sendMsg");
    	String userId = req.getParameter("userId");
    	if(StringUtils.isNoneBlank(userId)){
    		message.setUserId(Integer.valueOf(userId));
    	}
    	String jobId = req.getParameter("jobId");
    	if(StringUtils.isNoneBlank(jobId)){
    		message.setJobId(Integer.valueOf(jobId));
    	}
    	
    	String content = req.getParameter("content");
    	message.setContent(content);
    	message.setCreateTime(new Date());
    	PushMessagePool pool = (PushMessagePool)SpringComponent.getBean("pushMessagePool");
    	pool.addMsg(userId, message);
    	
		resultMap.put("result", resultModel);
        return resultMap;
    }
    
    /**
     * 获取指定用户消息通知列表接口
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryUserMsgList.json")
    @ResponseBody
    public Map<String,Object> queryUserMsgList(HttpServletRequest req) throws Exception {
    	Map<String, Object> resultMap =new HashMap<String, Object>();
        ResultModel resultModel =new ResultModel();
        String userId = req.getParameter("userId");
        PushMessagePool pool = (PushMessagePool)SpringComponent.getBean("pushMessagePool");
        ArrayList<Object> list = pool.getMsg(userId);//获取该用户全部短信
        
        resultMap.put("totalCount",list != null ? list.size() : 0);
        resultMap.put("list",list);
		resultMap.put("result", resultModel);
        return resultMap;
    }
    
    /**
     * 跨项目httpclient查询路由接口
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("/httpclientser.json")
    @ResponseBody
    public Map<String,Object> httpclientser(HttpServletRequest req, Model model,ModelType modelType) throws Exception {
    	Map<String, Object> resultMap =new HashMap<String, Object>();
    	 ResultModel resultModel =new ResultModel();
    	//签名正确执行查询 
    	if(SignUtils.filterSignRequest(req)){
    		String httpClientReqURI = req.getParameter("httpClientReqURI");///hchc-web/bas_customer/list.json
            if(httpClientReqURI.contains("/list.json")){
            	resultMap=listByPage(req, model);
            }
            else if(httpClientReqURI.contains("listAll.json")){
            	resultMap=listAll(req, model, modelType);
            }
            else if(httpClientReqURI.contains("listvo.json")){
            	resultMap=listVoByPage(req, model);
            }
            else if(httpClientReqURI.contains("listvoAll.json")){
            	resultMap=listVoAll(req, model);
            }
    	}
    	else{
    		resultModel.setMsg("签名验证不通过。");
    		resultMap.put("result", resultModel);
    	}
        return resultMap;
    }
    
    /**
     * 跨项目httpclientpost提交接口
     * 返回数据统一放在data节点
     * @param req
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	@RequestMapping("/httpclientpost.json")
    @ResponseBody
	public Map<String, Object> httpclientpost(HttpServletRequest req,
			Model model, ModelType modelType) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultModel resultModel = new ResultModel();
		//请求目标的url
		String httpClientReqURI = req.getParameter("httpClientReqURI");// /hc-mhceb/bas_customer/list.json
		if(StringUtils.isEmpty(httpClientReqURI)){
			resultMap.put("result", resultModel);
			throw new ValidException("参数httpClientReqURI不能为空。");
		}
		else{
			Map<String, String> params = new HashMap<String, String>(req.getParameterMap().size());
			Enumeration paramNames = req.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				String[] paramValues = req.getParameterValues(paramName);
				if (paramValues.length == 1) {
					String paramValue = paramValues[0];
					if (paramValue.length() != 0) {
						params.put(paramName, paramValue);
					}
				}
			}
			
			String url=CommonUtil.getHostUrl()+httpClientReqURI;
			//转换为路由接口
			url=url.substring(0,url.lastIndexOf("/"))+"/httpclientser.json";
			String res=HttpUtils.postWithAuthority(url, params);
			//resultMap.put("data", JSON.parse(res));
			resultMap=parseJSON2Map(res);
		}
		return resultMap;
	}
    
    /**
     * 最外层解析
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJSON2Map(String jsonStr){  
        Map<String, Object> map = new HashMap<String, Object>();  
        //最外层解析  
        JSONObject json = (JSONObject) JSON.parse(jsonStr);  
        for(Object k : json.keySet()){  
            Object v = json.get(k);   
            map.put(k.toString(), v);
        }  
        return map;  
    }  
    
}
