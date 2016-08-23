package com.hc.scm.common.pushlet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import com.hc.scm.common.cache.RedisClient;

/**
 * 推送消息池
 * Description: 
 * All rights Reserved, Designed By hc* Copyright:   Copyright(C) 2014-2015
 * Company:     Wonhigh.
 * author:      zhu.liang
 * Createdate:  2015-5-9下午3:44:45
 *
 *
 * Modification  History:
 * Date         Author             What
 * ------------------------------------------
 * 2015-5-9     	zhu.liang
 */
public class PushMessagePool {
	/**
	 * 用户异步消息key
	 */
	private static String UserRedisMessageKey="User_Redis_PushMessage_Key_";
	
	/**
	 *  每个用户异步消息最大条娄
	 */
	private static Integer UserRedisMessageMaxCount=100;
	
	/**
	 * 推送消息池
	 */
	//private static HashMap<String,ArrayList<Object>> msgMap = new HashMap<String,ArrayList<Object>>();
	@Resource
	private RedisClient redisClient;

	private String getUserRedisKey(String key){
		return UserRedisMessageKey;
	}
	
	/**
	 * 增加消息到队列
	 * @param key
	 * @param val
	 */
	@SuppressWarnings("unchecked")
	public void addMsg(String key,Object val) {
		ArrayList<Object> list = new ArrayList<Object>();
		key=getUserRedisKey(key);
		if(redisClient.get(key)!=null){
			list = (ArrayList<Object>) redisClient.get(key);
			int list_size=list.size();
			if(list !=null && list_size > 0){
				val=setMsgId(val, (long)list.size());
				//超过最大条数先清空
				if(list_size>UserRedisMessageMaxCount){
					list.clear();
				}
				list.add(val);
			}else{
				val=setMsgId(val, (long)0);
				list.add(val);
			}
		}else{
			val=setMsgId(val, (long)0);
			list.add(val);
		}
		redisClient.set(key, list,86400);//缓存一天
	}
	
	/**
	 * 清空全部消息
	 */
	private void clearAllMsg(){
		redisClient.delete(getUserRedisKey(""));
	}
	
	/**
	 *  返回msg list
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Object> getMsg(String key) {
		return (ArrayList<Object>) redisClient.get(getUserRedisKey(key));
	}
	
	/**
	 * 发送系统消息通知用户
	 * @param key
	 * @param val
	 */
	public void sendMsg(String userId,Object val) {
		addMsg(userId, val);
	}
	
	/**
	 * 设置msg的id
	 * @param obj
	 * @param id
	 * @return
	 */
	public Object setMsgId(Object obj,Long id) {
		if (obj instanceof PushMessage) {
			((PushMessage) obj).setId(id);
		}
		return obj;
	}

	
	@SuppressWarnings("unused")
	@PostConstruct
	private void afterInit(){
		Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23); 
        calendar.set(Calendar.MINUTE, 59);       
        calendar.set(Calendar.SECOND, 59);       
        Date time = calendar.getTime();        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            	clearAllMsg();//清空数据
            }
        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
	}
	
	@SuppressWarnings("unused")
	@PreDestroy
	private void beforeDestroy(){
		System.out.println("beforeDestroy() method run...");
		clearAllMsg();//清空数据
	}
	

}
