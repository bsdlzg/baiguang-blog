package com.bsdlzg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsdlzg.blog.entity.Comment;
import com.bsdlzg.blog.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 博客评论表 Mapper 接口
 * </p>
 *
 * @author bsdlzg
 * @since 2021-07-19
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {


    Page<SystemCommentVO> selectPageList(@Param("page")Page<Object> objectPage, @Param("keywords")String keywords);

    Page<ApiCommentListVO> selectCommentPage(@Param("page") Page<ApiCommentListVO> commentListVOPage,
                                             @Param("articleId") Long articleId);


}
