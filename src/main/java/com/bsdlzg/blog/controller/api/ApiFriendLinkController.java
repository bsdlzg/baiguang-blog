package com.bsdlzg.blog.controller.api;


import com.bsdlzg.blog.annotation.BusinessLogger;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.FriendLink;
import com.bsdlzg.blog.service.FriendLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 友情链接表 前端控制器
 * </p>
 *
 * @author bsdlzg
 * @since 2021-08-18
 */
@RestController
@RequestMapping("/v1/link")
@Api(tags = "友情链接-接口")
@RequiredArgsConstructor
public class ApiFriendLinkController {

    private final FriendLinkService friendLinkService;


    @BusinessLogger(value = "友链模块-用户申请友链",type = "添加",desc = "用户申请友链")
    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ApiOperation(value = "申请友链", httpMethod = "POST", response = ResponseResult.class, notes = "申请友链")
    public ResponseResult publicAddLink(@RequestBody FriendLink friendLink){
        return friendLinkService.publicAddLink(friendLink);
    }

    @BusinessLogger(value = "友链模块-用户访问页面",type = "查询",desc = "友链列表")
    @RequestMapping(value = "/selectLinkList",method = RequestMethod.GET)
    @ApiOperation(value = "友链列表", httpMethod = "POST", response = ResponseResult.class, notes = "友链列表")
    public ResponseResult selectPublicLinkList(){
        return friendLinkService.selectPublicLinkList();
    }

}

