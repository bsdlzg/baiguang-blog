package com.bsdlzg.blog.controller.api;


import com.bsdlzg.blog.annotation.BusinessLogger;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.service.TagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 博客标签表 前端控制器
 * </p>
 *
 * @author bsdlzg
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/v1/tag")
@Api(tags = "标签分类接口")
@RequiredArgsConstructor
public class ApiTagsController {

    private final TagsService tagsService;

    @BusinessLogger(value = "标签模块-用户访问页面", type = "查询", desc = "用户访问页面")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "标签列表", httpMethod = "GET", response = ResponseResult.class, notes = "标签列表")
    public ResponseResult selectPublicTagList() {
        return tagsService.selectPublicTagList();
    }

}

