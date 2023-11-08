package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.PhotoAlbum;

/**
 * <p>
 * 相册 服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-12-29
 */
public interface PhotoAlbumService extends IService<PhotoAlbum> {

    ResponseResult listAlbum(String name);

    ResponseResult getAlbumById(Integer id);

    ResponseResult insertAlbum(PhotoAlbum photoAlbum);

    ResponseResult updateAlbum(PhotoAlbum photoAlbum);

    ResponseResult deleteAlbumById(Integer id);





    //web端方法开始

    /**
     * 获取所有相册
     * @return
     */
    ResponseResult webAlbumList();

}
