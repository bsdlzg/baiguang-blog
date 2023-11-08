package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.WebConfig;

/**
 * <p>
 * 网站配置表 服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-11-27
 */
public interface WebConfigService extends IService<WebConfig> {

    ResponseResult listWebConfig();

    ResponseResult updateWebConfig(WebConfig webConfig);
}
