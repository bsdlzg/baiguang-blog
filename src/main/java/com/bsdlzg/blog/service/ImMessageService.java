package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.ImMessage;
import com.bsdlzg.blog.vo.ImMessageVO;
import com.bsdlzg.blog.vo.ImOnlineUserVO;

import java.util.List;
import java.util.Set;

/**
 *  聊天服务类
 *
 * @author bsdlzg
 * @since 2021-11-10
 */
public interface ImMessageService extends IService<ImMessage> {


    /**
     * 获取历史聊天记录
     * @return
     */
    ResponseResult selectPublicHistoryList();

    /**
     * 添加或修改消息（如发送消息和撤回消息）
     * @param obj
     */
    void updateOrInsert(ImMessageVO obj);

    /**
     * 获取在线用户列表
     */
    List<ImOnlineUserVO> selectPublicOnlineUserList(Set<String> strings);

    /**
     * 获取单聊历史消息
     * @return
     */
    ResponseResult selectPublicUserImHistoryList(String fromUserId, String toUserId);
}
