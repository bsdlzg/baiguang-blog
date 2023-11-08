package com.bsdlzg.blog.dto;

import lombok.Data;

/**
 * @author bsdlzg
 * @date 2022/2/25
 * @apiNote
 */
@Data
public class UserInfoDTO {
    /**
     * 用户信息id
     */
    private Integer id;
    /**
     * 昵称
     * */
    private String nickname;
    /**
     * 简介
     * */
    private String intro;

    /**
     * 个人网站
     * */
    private String webSite;

    /**
     * 头像
     * */
    private String avatar;

   /**
     * 邮箱
     * */
    private String email;

    /**
     * 验证码
     * */
    private String code;
}
