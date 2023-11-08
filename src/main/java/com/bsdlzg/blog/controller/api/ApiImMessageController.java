package com.bsdlzg.blog.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.bsdlzg.blog.annotation.BusinessLogger;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.service.ImMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/im")
@Api(tags = "聊天管理-门户")
@RequiredArgsConstructor
public class ApiImMessageController {

    private final ImMessageService imMessageService;

    @SaCheckLogin
    @GetMapping(value = "/history")
    @BusinessLogger(value = "首页-获取历史聊天记录",type = "查询",desc = "获取历史聊天记录")
    @ApiOperation(value = "获取历史聊天记录", httpMethod = "GET", response = ResponseResult.class, notes = "获取历史聊天记录")
    public ResponseResult selectPublicHistoryList() {
        return  imMessageService.selectPublicHistoryList();
    }

    @SaCheckLogin
    @GetMapping(value = "/userImHistory")
    @BusinessLogger(value = "首页-获取单聊历史消息",type = "查询",desc = "获取单聊历史消息")
    @ApiOperation(value = "获取单聊历史消息", httpMethod = "GET", response = ResponseResult.class, notes = "获取单聊历史消息")
    public ResponseResult selectPublicUserImHistoryList(String fromUserId,String toUserId) {
        return  imMessageService.selectPublicUserImHistoryList(fromUserId,toUserId);
    }
}
