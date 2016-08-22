package com.hc.scm.common.base.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BaseReqContronller {
	
	@RequestMapping("/load_service.json")
	@ResponseBody
	public Object  loadService(){
        return "var loadIndexPage='ok';";
	}
	
}
