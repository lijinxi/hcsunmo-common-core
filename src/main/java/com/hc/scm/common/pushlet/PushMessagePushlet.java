package com.hc.scm.common.pushlet;

import java.util.ArrayList;

import com.hc.scm.common.utils.SpringComponent;

import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.EventPullSource;

public class PushMessagePushlet extends EventPullSource {

	public long getSleepTime() {
		return 5*1000;
	}

	@Override
	protected Event pullEvent() {
		// TODO Auto-generated method stub
		Event event = Event.createDataEvent("/pushMessage");
		
		String userId = event.getField("userId");// 页面传递过来的参数
		PushMessagePool pool = (PushMessagePool)SpringComponent.getBean("pushMessagePool");
		ArrayList<Object> msgList = pool.getMsg(userId);
		int count = 0;//消息数
		if(msgList != null){
			count = msgList.size();
		}
		
		event.setField("data", count);
		return event;
	}

	/*
	 * 
	 * @Override
	protected Event pullEvent(Event reqEvent) {
		Event event = Event.createDataEvent("/pushMessage");

		String userId = reqEvent.getField("userId");// 页面传递过来的参数
		PushMessagePool pool = (PushMessagePool)SpringComponent.getBean("pushMessagePool");
		ArrayList<Object> msgList = pool.getMsg(userId);
		int count = 0;//消息数
		if(msgList != null){
			count = msgList.size();
		}

		event.setField("data", count);
		return event;
	}*/

}
