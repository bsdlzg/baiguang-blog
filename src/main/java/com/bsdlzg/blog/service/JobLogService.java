package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.JobLog;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-12-08
 */
public interface JobLogService extends IService<JobLog> {

    ResponseResult listJobLog(String jobName, String jobGroup, String status, String startTime,
                            String endTime, Long jobId);

    ResponseResult deleteBatch(List<Long> ids);

    ResponseResult clean();
}
