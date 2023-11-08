package com.bsdlzg.blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxUserInfoVO {

    @ApiModelProperty(value = "用户临时id")
    private String tempUserId;

    @ApiModelProperty(value = "是否扫描二维码")
    private Boolean scanSuccess = false;

    @ApiModelProperty(value = "用户取消授权状态")
    private Boolean cancelLogin = false;

    @ApiModelProperty(value = "微信用户信息")
    private WxMaUserInfo wxMaUserInfo;

    @Data
    public static class WxMaUserInfo{
        @ApiModelProperty(value = "用户唯一标识符")
        private String openId;

        @ApiModelProperty(value = "用户昵称")
        private String nickName;

        @ApiModelProperty(value = "头像地址")
        private String avatarUrl;
    }
}
