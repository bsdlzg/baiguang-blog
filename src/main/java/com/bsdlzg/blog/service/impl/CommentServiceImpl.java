package com.bsdlzg.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.Comment;
import com.bsdlzg.blog.entity.UserInfo;
import com.bsdlzg.blog.exception.BusinessException;
import com.bsdlzg.blog.mapper.CommentMapper;
import com.bsdlzg.blog.mapper.UserInfoMapper;
import com.bsdlzg.blog.service.CommentService;
import com.bsdlzg.blog.utils.HTMLUtils;
import com.bsdlzg.blog.utils.IpUtil;
import com.bsdlzg.blog.utils.PageUtils;
import com.bsdlzg.blog.vo.ApiCommentListVO;
import com.bsdlzg.blog.vo.SystemCommentVO;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-08-18
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final UserInfoMapper userInfoMapper;

    private final HttpServletRequest request;

    /**
     * 评论列表
     *
     * @param keywords
     * @return
     */
    @Override
    public ResponseResult listComment(String keywords) {
        Page<SystemCommentVO> dtoPage = baseMapper.selectPageList(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), keywords);
        return ResponseResult.success(dtoPage);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public ResponseResult deleteBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }


    //-----------------------web端方法开始-------------
    /**
     * 发表评论
     * @param comment
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult publicAddComment(Comment comment) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
        //获取ip地址
        String ipAddress = IpUtil.getIp2region(IpUtil.getIp(request));
        String os = userAgent.getOperatingSystem().getName();
        if (os.contains("mac") || os.contains("Mac")) {
            comment.setSystem("mac");
        } else if (os.contains("Windows")) {
            comment.setSystem("windowns");
        }else {
            comment.setSystem("android");
        }
        //过滤内容 如删除html标签和敏感词替换
        String content = HTMLUtils.deleteTag(comment.getContent());
        comment.setContent(content);
        comment.setSystemVersion(os);
        comment.setIpAddress(ipAddress);
        comment.setUserId(StpUtil.getLoginIdAsString());
        int insert = baseMapper.insert(comment);
        if (insert == 0){
            throw new BusinessException("评论失败");
        }
        return ResponseResult.success(comment);
    }

    @Override
    public ResponseResult selectCommentByArticleId(int pageNo, int pageSize, Long articleId) {
        //获取评论父级评论
        Page<ApiCommentListVO> pageList = baseMapper.selectCommentPage(new Page<ApiCommentListVO>(pageNo,pageSize),articleId);
        //获取子级
        for (ApiCommentListVO vo : pageList.getRecords()) {
            List<Comment> commentList = baseMapper.selectList(
                    new LambdaQueryWrapper<Comment>().eq(Comment::getParentId, vo.getId()).orderByDesc(Comment::getCreateTime));
            for (Comment e : commentList) {
                UserInfo replyUserInfo = userInfoMapper.getByUserId(e.getReplyUserId());
                UserInfo userInfo1 = userInfoMapper.getByUserId(e.getUserId());
                ApiCommentListVO apiCommentListVO = ApiCommentListVO.builder()
                        .id(e.getId())
                        .userId(e.getUserId())
                        .replyUserId(e.getReplyUserId())
                        .nickname(userInfo1.getNickname())
                        .webSite(userInfo1.getWebSite())
                        .replyNickname(replyUserInfo.getNickname())
                        .replyWebSite(replyUserInfo.getWebSite())
                        .content(e.getContent())
                        .avatar(userInfo1.getAvatar())
                        .createTime(e.getCreateTime())
                        .browser(e.getBrowser())
                        .browserVersion(e.getBrowserVersion())
                        .system(e.getSystem())
                        .systemVersion(e.getSystemVersion())
                        .ipAddress(e.getIpAddress())
                        .build();
                vo.getChildren().add(apiCommentListVO);
            }
        }
        return ResponseResult.success(pageList);
    }

}
