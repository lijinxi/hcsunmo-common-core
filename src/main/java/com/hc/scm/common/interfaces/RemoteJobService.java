package com.hc.scm.common.interfaces;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

import com.hc.scm.common.enums.JobBizStatusEnum;

/**
 * 调度处理接口
 *  
 */
public interface RemoteJobService {
	/**
	 * 执行初始化
	 * @param triggerName
	 * @param groupName
	 * @return
	 */
	public void initializeJob(String triggerName,String groupName);
	/**
	 * 执行
	 * @param triggerName
	 * @param groupName
	 */
	@ManagedOperation(description="")  
    @ManagedOperationParameters({  
    @ManagedOperationParameter(name = "triggerName", description = "quartz trigger name"),  
    @ManagedOperationParameter(name = "groupName", description = "quartz trigger group name")})
	public void executeJob(String triggerName,String groupName);
	
	/**
	 * 暂停
	 * @param triggerName
	 * @param groupName
	 */
	@ManagedOperation(description="")  
    @ManagedOperationParameters({  
    @ManagedOperationParameter(name = "triggerName", description = "quartz trigger name"),  
    @ManagedOperationParameter(name = "groupName", description = "quartz trigger group name")})
	public void pauseJob(String triggerName,String groupName);
	/**
	 * 恢复
	 * @param triggerName
	 * @param groupName
	 */
	@ManagedOperation(description="")  
    @ManagedOperationParameters({  
    @ManagedOperationParameter(name = "triggerName", description = "quartz trigger name"),  
    @ManagedOperationParameter(name = "groupName", description = "quartz trigger group name")})
	public void resumeJob(String triggerName,String groupName);
	/**
	 * 停止
	 * @param triggerName
	 * @param groupName
	 */
	@ManagedOperation(description="")  
    @ManagedOperationParameters({  
    @ManagedOperationParameter(name = "triggerName", description = "quartz trigger name"),  
    @ManagedOperationParameter(name = "groupName", description = "quartz trigger group name")})
	public void stopJob(String triggerName,String groupName);
	/**
	 * 重启
	 * @param triggerName
	 * @param groupName
	 */
	@ManagedOperation(description="")  
    @ManagedOperationParameters({  
    @ManagedOperationParameter(name = "triggerName", description = "quartz trigger name"),  
    @ManagedOperationParameter(name = "groupName", description = "quartz trigger group name")})
	public void restartJob(String triggerName,String groupName);
	/**
	 * 获得调度状态
	 * @param triggerName
	 * @param groupName
	 */
	@ManagedOperation(description="get trigger's job running status")  
    @ManagedOperationParameters({  
    @ManagedOperationParameter(name = "triggerName", description = "quartz trigger name"),  
    @ManagedOperationParameter(name = "groupName", description = "quartz trigger group name")})
	public JobBizStatusEnum getJobStatus(String triggerName,String groupName);
	/**
	 * 获得调度日志
	 * @param triggerName
	 * @param groupName
	 * @param lastDate
	 * @return 删除上一次的运行日志  返回json类型，多条
	 */
	@ManagedOperation(description="get trigger was job ran logs")  
    @ManagedOperationParameters({
    @ManagedOperationParameter(name = "triggerName", description = "quartz trigger name"),  
    @ManagedOperationParameter(name = "groupName", description = "quartz trigger group name"),
	@ManagedOperationParameter(name = "lastDate", description = "ran of last time")})
	public String getLogs(String triggerName,String groupName,long lastDate);
}
