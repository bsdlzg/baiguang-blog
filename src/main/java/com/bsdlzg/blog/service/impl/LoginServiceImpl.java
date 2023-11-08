package com.bsdlzg.blog.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.bsdlzg.blog.common.Constants;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.common.ResultCode;
import com.bsdlzg.blog.dto.Captcha;
import com.bsdlzg.blog.dto.LoginDTO;
import com.bsdlzg.blog.entity.User;
import com.bsdlzg.blog.exception.BusinessException;
import com.bsdlzg.blog.mapper.UserMapper;
import com.bsdlzg.blog.service.LoginService;
import com.bsdlzg.blog.utils.AesEncryptUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bsdlzg
 * @description:
 * @date 2021/7/30 14:59
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginServiceImpl implements LoginService {

    private final UserMapper userMapper;

    private final CaptchaServiceImpl captchaService;

    @Override
    public ResponseResult getCaptcha(Captcha captcha) {
        return ResponseResult.success(captchaService.getCaptcha(captcha));
    }

    @Override
    public ResponseResult login(LoginDTO dto) {

        //先校验验证码
        String msg = captchaService.checkImageCode(dto.getNonceStr(),dto.getValue());
        if (StringUtils.isNotBlank(msg)) {
            return ResponseResult.error(msg);
        }

        //校验用户名和密码
        User user = userMapper.selectNameAndPassword(dto.getUsername(), AesEncryptUtils.aesEncrypt(dto.getPassword()));
        if (user == null){
            throw new BusinessException(ResultCode.ERROR_PASSWORD.desc);
        }

        if (dto.getRememberMe()) {
            StpUtil.login(user.getId(),new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
        }else {
            StpUtil.login(user.getId(),"system");
        }
        StpUtil.getSession().set(Constants.CURRENT_USER,userMapper.getById(user.getId()));
        return ResponseResult.success(StpUtil.getTokenValue());
    }


}
