package com.bsdlzg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsdlzg.blog.entity.Job;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 定时任务调度表 Mapper 接口
 * </p>
 *
 * @author bsdlzg
 * @since 2021-12-08
 */
@Repository
public interface JobMapper extends BaseMapper<Job> {

}
