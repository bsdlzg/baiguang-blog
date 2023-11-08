package com.bsdlzg.blog.service;


import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.dto.Captcha;
import com.bsdlzg.blog.dto.LoginDTO;

/**
 * @author bsdlzg
 * @description:
 * @date 2021/7/30 14:58
 */
public interface LoginService {

    ResponseResult login(LoginDTO vo);

    ResponseResult getCaptcha(Captcha captcha);
}
