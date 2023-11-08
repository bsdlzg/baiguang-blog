package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.dto.EmailLoginDTO;
import com.bsdlzg.blog.dto.EmailRegisterDTO;
import com.bsdlzg.blog.dto.UserInfoDTO;
import com.bsdlzg.blog.entity.UserInfo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-12-25
 */
public interface UserInfoService extends IService<UserInfo> {


    /**
     * 邮箱注册
     * @param emailRegisterDTO
     * @return
     */
    public ResponseResult emailRegister(EmailRegisterDTO emailRegisterDTO);

    /**
     * 邮箱登录
     * @param emailLoginDTO
     * @return
     */
    public ResponseResult emailLogin(EmailLoginDTO emailLoginDTO);

    /**
     * 修改密码
     * @param emailRegisterDTO
     * @return
     */
    public ResponseResult updatePassword(EmailRegisterDTO emailRegisterDTO);

    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    public ResponseResult sendEmailCode(String email);

    /**
     * 用户绑定邮箱
     * @param vo
     * @return
     */
    public ResponseResult publicBindEmail(UserInfoDTO vo);

    /**
     * 获取微信登录二维码
     * @return
     */
    public ResponseResult wxQr();

    /**
     * 微信授权登录回调地址
     * @param body
     * @return
     */
    public Map<String,Object> wechatLogin(String body);

    /**
     * 判断用户是否微信登录成功
     * @param tempUserId 用户临时id
     * @return
     */
    public ResponseResult wxIsLogin(String tempUserId);

    /**
     * 获取用户信息
     * @return
     */
    public ResponseResult publicSelectUserInfo();

    /**
     * 修改用户信息
     * @param vo
     * @return
     */
    public ResponseResult publicUpdateUser(UserInfoDTO vo);

    /**
     * 根据token获取用户信息
     * @param token
     * @return
     */
    ResponseResult publicSelectUserInfoByToken(String token);
}
