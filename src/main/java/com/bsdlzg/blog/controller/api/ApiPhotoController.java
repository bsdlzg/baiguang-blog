package com.bsdlzg.blog.controller.api;

import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "照片管理-API")
@RequestMapping("v1/photo")
@RequiredArgsConstructor
public class ApiPhotoController {

    private final PhotoService photoService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "根据相册id分页获取照片", httpMethod = "GET", response = ResponseResult.class, notes = "根据相册id分页获取照片")
    public ResponseResult selectListPhoto(Integer photoAlbumId, Integer pageNo, Integer pageSize) {
        return photoService.selectListPhoto(photoAlbumId, pageNo, pageSize);
    }

}
