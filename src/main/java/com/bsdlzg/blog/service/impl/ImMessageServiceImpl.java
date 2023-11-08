package com.bsdlzg.blog.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.ImMessage;
import com.bsdlzg.blog.mapper.ImMessageMapper;
import com.bsdlzg.blog.service.ImMessageService;
import com.bsdlzg.blog.utils.BeanCopyUtils;
import com.bsdlzg.blog.utils.PageUtils;
import com.bsdlzg.blog.vo.ImMessageVO;
import com.bsdlzg.blog.vo.ImOnlineUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-11-10
 */
@Service
public class ImMessageServiceImpl extends ServiceImpl<ImMessageMapper, ImMessage> implements ImMessageService {


    @Override
    public ResponseResult selectPublicHistoryList() {
        Page<ImMessageVO> page = baseMapper.selectPublicHistoryList(new Page<>(PageUtils.getPageNo(),
                        PageUtils.getPageSize()));
        return ResponseResult.success(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrInsert(ImMessageVO obj) {
        ImMessage imMessage = BeanCopyUtils.copyObject(obj, ImMessage.class);
        //撤回消息
        if (obj.getIsWithdraw() == 1) {
            baseMapper.updateById(imMessage);
        }else {
            //保存消息到数据库
            baseMapper.insert(imMessage);
        }
    }

    @Override
    public List<ImOnlineUserVO> selectPublicOnlineUserList(Set<String> keys) {
        return baseMapper.selectPublicOnlineUserList(keys);
    }

    @Override
    public ResponseResult selectPublicUserImHistoryList(String fromUserId, String toUserId) {
        Page<ImMessageVO> page = baseMapper.selectPublicUserImHistoryList(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),
                fromUserId,toUserId);
        return ResponseResult.success(page);
    }
}
