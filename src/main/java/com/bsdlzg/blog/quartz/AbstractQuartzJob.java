package com.bsdlzg.blog.quartz;

import com.bsdlzg.blog.entity.Job;
import com.bsdlzg.blog.entity.JobLog;
import com.bsdlzg.blog.common.ScheduleConstants;
import com.bsdlzg.blog.mapper.JobLogMapper;
import com.bsdlzg.blog.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import static com.bsdlzg.blog.common.Constants.ONE;
import static com.bsdlzg.blog.common.Constants.ZERO;

/**
 * @author bsdlzg
 * @date 2021/12/8
 * @apiNote 抽象quartz调用
 */
public abstract class AbstractQuartzJob implements org.quartz.Job {
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static final ThreadLocal<Date> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        Job job = new Job();
        BeanUtils.copyProperties(context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES), job);
        try {
            before(context, job);
            doExecute(context, job);
            after(context, job, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, job, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param job  系统计划任务
     */
    protected void before(JobExecutionContext context, Job job) {
        THREAD_LOCAL.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param job  系统计划任务
     */
    protected void after(JobExecutionContext context, Job job, Exception e) {
        Date startTime = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        //redis心跳任务不添加日志
        if (!job.getInvokeTarget().contains("redisTimer")) {
            final JobLog jobLog = new JobLog();
            jobLog.setJobId(job.getJobId());
            jobLog.setJobName(job.getJobName());
            jobLog.setJobGroup(job.getJobGroup());
            jobLog.setInvokeTarget(job.getInvokeTarget());
            jobLog.setStartTime(startTime);
            jobLog.setStopTime(new Date());
            long runMs = jobLog.getStopTime().getTime() - jobLog.getStartTime().getTime();
            jobLog.setJobMessage(jobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
            if (e != null) {
                jobLog.setStatus(ONE);
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                String errorMsg = StringUtils.substring(str, 0, 2000);
                jobLog.setExceptionInfo(errorMsg);
            } else {
                jobLog.setStatus(ZERO);
            }

            // 写入数据库当中
            SpringUtils.getBean(JobLogMapper.class).insert(jobLog);
        }

    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param job  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, Job job) throws Exception;
}
