package com.bsdlzg.blog.controller.api;


import com.bsdlzg.blog.annotation.BusinessLogger;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.Message;
import com.bsdlzg.blog.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bsdlzg
 * @since 2021-09-03
 */
@RestController
@RequestMapping("/v1/message")
@Api(tags = "评论留言接口")
@RequiredArgsConstructor
public class ApiMessageController {

    private final MessageService messageService;

    @BusinessLogger(value = "留言模块-留言列表",type = "查询",desc = "留言列表")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "留言列表", httpMethod = "GET", response = ResponseResult.class, notes = "留言列表")
    public ResponseResult selectPublicMessage(){
        return messageService.selectPublicMessage();
    }


    @BusinessLogger(value = "留言模块-用户留言",type = "添加",desc = "用户留言")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ApiOperation(value = "添加留言", httpMethod = "POST", response = ResponseResult.class, notes = "添加留言")
    public ResponseResult publicAddMessage(@RequestBody Message message){
        return messageService.publicAddMessage(message);
    }

}

