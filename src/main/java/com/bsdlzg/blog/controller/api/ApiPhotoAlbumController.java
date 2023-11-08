package com.bsdlzg.blog.controller.api;


import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.service.PhotoAlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 相册 前端控制器
 * </p>
 *
 * @author bsdlzg
 * @since 2021-12-29
 */
@RestController
@RequestMapping("v1/photoAlbum")
@Api(tags = "相册接口-API")
@RequiredArgsConstructor
public class ApiPhotoAlbumController {

    private final PhotoAlbumService albumService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "相册列表", httpMethod = "GET", response = ResponseResult.class, notes = "相册列表")
    public ResponseResult webAlbumList() {
        return albumService.webAlbumList();
    }

}

