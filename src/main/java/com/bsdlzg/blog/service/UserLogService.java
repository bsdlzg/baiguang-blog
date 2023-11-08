package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.UserLog;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-11-09
 */
public interface UserLogService extends IService<UserLog> {

    ResponseResult listUserLog();

    ResponseResult deleteBatch(List<Long> ids);
}
