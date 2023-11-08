package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.Photo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 照片 服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-12-29
 */
public interface PhotoService extends IService<Photo> {

    ResponseResult listPhoto(Integer albumId);

    ResponseResult getPhotoById(Integer id);

    ResponseResult insertAlbum(Photo photo);

    ResponseResult updatePhoto(Photo photo);

    ResponseResult movePhoto(Map<String,Object> map);

    ResponseResult deleteBatch(List<Integer> ids);

    /**
     * 获取所有照片
     * @param photoAlbumId
     * @param pageNo
     * @param pageSize
     * @return
     */
    ResponseResult selectListPhoto(Integer photoAlbumId, Integer pageNo, Integer pageSize);
}
