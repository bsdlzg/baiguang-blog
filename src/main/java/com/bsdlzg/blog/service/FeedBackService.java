package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.FeedBack;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2022-01-13
 */
public interface FeedBackService extends IService<FeedBack> {

    ResponseResult listFeedBack(Integer type);

    ResponseResult deleteBatch(List<Integer> ids);


    ResponseResult insertFeedback(FeedBack feedBack);

}
