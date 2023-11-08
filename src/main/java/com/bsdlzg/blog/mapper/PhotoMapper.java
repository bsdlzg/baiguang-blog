package com.bsdlzg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsdlzg.blog.entity.Photo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 照片 Mapper 接口
 * </p>
 *
 * @author bsdlzg
 * @since 2021-12-29
 */
@Repository
public interface PhotoMapper extends BaseMapper<Photo> {

    void movePhoto(@Param("idList") List<Integer> ids, @Param("albumId") Integer albumId);
}
