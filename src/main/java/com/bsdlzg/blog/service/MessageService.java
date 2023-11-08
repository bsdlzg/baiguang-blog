package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.Message;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-09-03
 */
public interface MessageService extends IService<Message> {

    ResponseResult listMessage(String name);

    ResponseResult deleteMessageById(int id);

    ResponseResult passBatch(List<Integer> ids);

    ResponseResult deleteBatch(List<Integer> ids);




    //    ------web端方法开始-----


    /**
     * 获取所有留言
     * @return
     */
    ResponseResult selectPublicMessage();

    /**
     * 添加留言
     * @param message
     * @return
     */
    ResponseResult publicAddMessage(Message message);


}
