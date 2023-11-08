package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.Comment;

import java.util.List;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-08-18
 */
public interface CommentService extends IService<Comment> {

    ResponseResult listComment(String keywords);

    ResponseResult deleteBatch(List<Integer> ids);



//    ------web端方法开始------

    /**
     * 发表评论
     * @param comment
     * @return
     */
    ResponseResult publicAddComment(Comment comment);

    /**
     * 分页获取文章评论
     * @param pageNo
     * @param pageSize
     * @param articleId
     * @return
     */
    ResponseResult selectCommentByArticleId(int pageNo, int pageSize, Long articleId);

}
