package com.bsdlzg.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 博客评论表
 * </p>
 *
 * @author bsdlzg
 * @since 2021-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_im_message")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="ImMessage对象", description="群聊实体类")
public class ImMessage implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "发送用户Id")
    private String toUserId;

    @ApiModelProperty(value = "接收用户id")
    private String fromUserId;

    @ApiModelProperty(value = "发送用户头像")
    private String toUserAvatar;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "ip地址")
    private String ipSource;

    @ApiModelProperty(value = "是否撤回")
    private int isWithdraw;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
