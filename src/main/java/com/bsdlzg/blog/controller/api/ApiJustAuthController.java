package com.bsdlzg.blog.controller.api;

import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.dto.EmailLoginDTO;
import com.bsdlzg.blog.dto.EmailRegisterDTO;
import com.bsdlzg.blog.service.UserInfoService;
import com.bsdlzg.blog.service.UserService;
import com.bsdlzg.blog.utils.AuthRequestUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiJustAuthController {

    private final UserService userService;

    private final AuthRequestUtil authRequestUtil;

    private final UserInfoService userInfoService;


    /**
     * 通过JustAuth的AuthRequest拿到第三方的授权链接，并跳转到该链接页面
     *
     * @param response
     * @throws IOException
     */
    @RequestMapping("/render/{source}")
    public ResponseResult renderAuth(HttpServletResponse response, @PathVariable String source) throws IOException {
        AuthRequest authRequest = authRequestUtil.getAuthRequest(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        return ResponseResult.success(authorizeUrl);
    }

    /**
     * 用户在确认第三方平台授权（登录）后， 第三方平台会重定向到该地址，并携带code、state等参数
     * authRequest.login通过code向第三方请求用户数据
     *
     * @param callback 第三方回调时的入参
     * @return 第三方平台的用户信息
     */
    @RequestMapping("/callback/{source}")
    public void login(AuthCallback callback, @PathVariable String source, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        AuthRequest authRequest = authRequestUtil.getAuthRequest(source);
        AuthResponse response = authRequest.login(callback);
        userService.authLogin(response, source, request, httpServletResponse);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "邮箱账号注册", httpMethod = "POST", response = ResponseResult.class, notes = "邮箱账号注册")
    public ResponseResult emailRegister(@Valid @RequestBody EmailRegisterDTO emailRegisterDTO) {
        return userInfoService.emailRegister(emailRegisterDTO);
    }

    @RequestMapping(value = "/emailLogin", method = RequestMethod.POST)
    @ApiOperation(value = "邮箱登录", httpMethod = "POST", response = ResponseResult.class, notes = "邮箱登录")
    public ResponseResult emailLogin(@Valid @RequestBody EmailLoginDTO emailLoginDTO) {
        return userInfoService.emailLogin(emailLoginDTO);
    }


    @RequestMapping(value = "/wxQr", method = RequestMethod.GET)
    @ApiOperation(value = "获取微信登录二维码", httpMethod = "GET", response = ResponseResult.class, notes = "获取微信登录二维码")
    public ResponseResult wxQr() {
        return userInfoService.wxQr();
    }

    @ApiOperation(value = "wx登录授权回调接口", httpMethod = "POST", response = ResponseResult.class, notes = "wx登录授权回调接口")
    @RequestMapping(value = "/callBack", method = RequestMethod.POST)
    public Map<String, Object> wxCallBack(@RequestBody String body) {
        return userInfoService.wechatLogin(body);
    }

    @ApiOperation(value = "判断用户是否微信登录成功", httpMethod = "GET", response = ResponseResult.class, notes = "判断用户是否微信登录成功")
    @RequestMapping("/wx/is_login")
    public ResponseResult wxIsLogin(@ApiParam(name = "tempUserId", value = "tempUserId") String tempUserId) {
        return userInfoService.wxIsLogin(tempUserId);
    }


}
