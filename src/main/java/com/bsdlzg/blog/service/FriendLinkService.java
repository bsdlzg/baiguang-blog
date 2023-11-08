package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.FriendLink;

import java.util.List;

/**
 * <p>
 * 友情链接表 服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-08-18
 */
public interface FriendLinkService extends IService<FriendLink> {

    ResponseResult listFriendLink(String name, Integer status);

    ResponseResult insertFriendLink(FriendLink friendLink);

    ResponseResult updateFriendLink(FriendLink friendLink);

    ResponseResult deleteBatch(List<Integer> ids);

    ResponseResult top(Integer id);


    //    ----web端开始-----

    /**
     * 获取所有友链
     * @return
     */
    ResponseResult selectPublicLinkList();

    /**
     * 用户申请友链
     * @param friendLink
     * @return
     */
    ResponseResult publicAddLink(FriendLink friendLink);


}
