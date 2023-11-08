package com.bsdlzg.blog.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.bsdlzg.blog.annotation.BusinessLogger;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.dto.EmailRegisterDTO;
import com.bsdlzg.blog.dto.UserInfoDTO;
import com.bsdlzg.blog.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/v1/user")
@RestController
@Api(tags = "登录接口")
@RequiredArgsConstructor
public class ApiUserController {

    private final UserInfoService userInfoService;

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @BusinessLogger(value = "个人中心模块-邮箱账号修改密码", type = "修改", desc = "邮箱账号修改密码")
    public ResponseResult updatePassword(@Valid @RequestBody EmailRegisterDTO emailRegisterDTO) {
        return userInfoService.updatePassword(emailRegisterDTO);
    }

    @RequestMapping(value = "/sendEmailCode", method = RequestMethod.GET)
    @ApiOperation(value = "发送邮箱验证码", httpMethod = "GET", response = ResponseResult.class, notes = "发送邮箱验证码")
    public ResponseResult sendEmailCode(String email) {
        return userInfoService.sendEmailCode(email);
    }

    @SaCheckLogin
    @RequestMapping(value = "/bindEmail", method = RequestMethod.POST)
    @BusinessLogger(value = "个人中心模块-绑定邮箱", type = "修改", desc = "绑定邮箱")
    public ResponseResult publicBindEmail(@RequestBody UserInfoDTO vo) {
        return userInfoService.publicBindEmail(vo);
    }

    @SaCheckLogin
    @BusinessLogger(value = "个人中心模块-获取用户信息", type = "修改", desc = "获取用户信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseResult publicSelectUserInfo() {
        return userInfoService.publicSelectUserInfo();
    }

    @SaCheckLogin
    @BusinessLogger(value = "个人中心模块-修改用户信息", type = "修改", desc = "修改用户信息")
    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseResult publicUpdateUser(@RequestBody UserInfoDTO vo) {
        return userInfoService.publicUpdateUser(vo);
    }

    @RequestMapping(value = "selectUserInfoByToken", method = RequestMethod.GET)
    @ApiOperation(value = "根据token获取用户信息", httpMethod = "GET", response = ResponseResult.class, notes = "根据token获取用户信息")
    public ResponseResult publicSelectUserInfoByToken(String token) {
        return userInfoService.publicSelectUserInfoByToken(token);
    }

}

