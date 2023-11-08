package com.bsdlzg.blog.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author bsdlzg
 * @date 2022/3/11
 * @apiNote
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Boolean rememberMe;

    @NotBlank(message = "验证码nonceStr不能为空")
    private String nonceStr;
    @NotBlank(message = "验证码value不能为空")
    private String value;
}
