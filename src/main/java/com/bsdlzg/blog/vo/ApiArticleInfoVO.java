package com.bsdlzg.blog.vo;

import com.bsdlzg.blog.entity.Category;
import com.bsdlzg.blog.entity.Tags;
import com.bsdlzg.blog.entity.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ApiArticleInfoVO {
    /**
     * 主键id
     */
    private Long id;


    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "所属用户名")
    private String username;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "作者头像")
    private String avatar;

    @ApiModelProperty(value = "文章简介")
    private String summary;
    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "文章内容MD版")
    private String contentMd;

    @ApiModelProperty(value = "是否置顶 0否 1是")
    private Integer isStick;

    @ApiModelProperty(value = "文章阅读量")
    private Integer quantity;


    @ApiModelProperty(value = "点赞量")
    private Object likeCount;

    @ApiModelProperty(value = "评论量")
    private Integer commentCount;

    @ApiModelProperty(value = "发表时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "标签集合")
    private List<Tags> tagList;

    @ApiModelProperty(value = "分类")
    private Category category;

    @ApiModelProperty(value = "当前用户是否点赞")
    private Boolean isLike = false;

    @ApiModelProperty(value = "评论")
    private List<ApiCommentListVO> apiCommentListVos;

    private UserInfo userInfo;


}
