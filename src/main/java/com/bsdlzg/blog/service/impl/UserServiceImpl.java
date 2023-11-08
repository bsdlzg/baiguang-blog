package com.bsdlzg.blog.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.config.satoken.MySaTokenListener;
import com.bsdlzg.blog.config.satoken.OnlineUser;
import com.bsdlzg.blog.dto.SystemUserDTO;
import com.bsdlzg.blog.entity.Menu;
import com.bsdlzg.blog.entity.User;
import com.bsdlzg.blog.entity.UserInfo;
import com.bsdlzg.blog.enums.LoginTypeEnum;
import com.bsdlzg.blog.enums.UserStatusEnum;
import com.bsdlzg.blog.exception.BusinessException;
import com.bsdlzg.blog.mapper.RoleMapper;
import com.bsdlzg.blog.mapper.UserInfoMapper;
import com.bsdlzg.blog.mapper.UserMapper;
import com.bsdlzg.blog.service.MenuService;
import com.bsdlzg.blog.service.UserService;
import com.bsdlzg.blog.utils.AesEncryptUtils;
import com.bsdlzg.blog.utils.IpUtil;
import com.bsdlzg.blog.utils.PageUtils;
import com.bsdlzg.blog.vo.SystemUserInfoVO;
import com.bsdlzg.blog.vo.SystemUserVO;
import com.bsdlzg.blog.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.bsdlzg.blog.common.ResultCode.ERROR_USER_NOT_EXIST;

/**
 * @author bsdlzg
 * @description:
 * @date 2021/7/30 17:25
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final MenuService menuService;

    private final UserInfoMapper userInfoMapper;

    private final RoleMapper roleMapper;


    /**
     * 用户列表
     *
     * @param username
     * @param loginType
     * @return
     */
    @Override
    public ResponseResult listUser(String username, Integer loginType) {
        Page<SystemUserInfoVO> page = baseMapper.selectPageRecord(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), username, loginType);
        return ResponseResult.success(page);
    }

    /**
     * 用户详情
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult getUserById(String id) {
        SystemUserVO user = baseMapper.getById(id);
        return ResponseResult.success(user);
    }

    /**
     * 添加用户
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertUser(SystemUserDTO dto) {
        UserInfoVO userInfoVO = baseMapper.selectByUserName(dto.getUsername());
        if (userInfoVO != null) {
            throw new BusinessException("用户名已存在!");
        }
        //添加用户信息
        UserInfo userInfo = UserInfo.builder().nickname(dto.getNickname()).avatar(dto.getAvatar()).build();
        userInfoMapper.insert(userInfo);

        //添加用户账号
        User user = User.builder().username(dto.getUsername()).password(AesEncryptUtils.aesEncrypt(dto.getPassword()))
                .status(dto.getStatus()).userInfoId(userInfo.getId()).roleId(dto.getRoleId()).loginType(LoginTypeEnum.EMAIL.getType()).build();

        baseMapper.insert(user);
        return ResponseResult.success(user);
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateUser(User user) {
        baseMapper.updateById(user);
        return ResponseResult.success();
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<String> ids) {
        //先删除用户账号
        baseMapper.deleteBatchIds(ids);
        //删除用户信息
        userInfoMapper.deleteByUserIds(ids);
        return ResponseResult.success();
    }

    /**
     * 获取当前登录用户详情
     *
     * @return
     */
    @Override
    public ResponseResult getCurrentUserInfo() {
        return ResponseResult.success("获取当前登录用户信息成功", baseMapper.getById(StpUtil.getLoginIdAsString()));
    }

    /**
     * 获取当前登录用户所拥有的菜单权限
     *
     * @return
     */
    @Override
    public ResponseResult getCurrentUserMenu() {
        List<Integer> menuIds = baseMapper.getMenuId(StpUtil.getLoginIdAsString());
        List<Menu> menus = menuService.listByIds(menuIds);
        List<Menu> menuTree = menuService.listMenuTree(menus);
        return ResponseResult.success(menuTree);
    }

    /**
     * 修改密码
     *
     * @param map
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updatePassword(Map<String, String> map) {

        User user = baseMapper.selectById(StpUtil.getLoginIdAsString());
        Assert.notNull(user, ERROR_USER_NOT_EXIST.getDesc());

        boolean isValid = AesEncryptUtils.validate(user.getPassword(), map.get("oldPassword"));
        Assert.isTrue(isValid, "旧密码校验不通过!");

        String newPassword = AesEncryptUtils.aesEncrypt(map.get("newPassword"));
        user.setPassword(newPassword);
        baseMapper.updateById(user);
        return ResponseResult.success("修改成功");
    }

    /**
     * 在线用户
     *
     * @param keywords
     * @return
     */
    @Override
    public ResponseResult listOnlineUsers(String keywords) {
        int pageNo = PageUtils.getPageNo().intValue();
        int pageSize = PageUtils.getPageSize().intValue();

        List<OnlineUser> onlineUsers = MySaTokenListener.ONLINE_USERS;
        //根据关键词过滤
        if (StringUtils.isNotBlank(keywords)) {
            onlineUsers = MySaTokenListener.ONLINE_USERS.stream().filter(item -> item.getNickname().contains(keywords)).collect(Collectors.toList());
        }
        //排序
        onlineUsers.sort((o1, o2) -> DateUtil.compare(o2.getLoginTime(), o1.getLoginTime()));
        int fromIndex = (pageNo - 1) * pageSize;
        int toIndex = onlineUsers.size() - fromIndex > pageSize ? fromIndex + pageSize : onlineUsers.size();
        List<OnlineUser> userOnlineList = onlineUsers.subList(fromIndex, toIndex);
        logger.info("memory用户数：{}", userOnlineList.size());

        Map<String, Object> map = new HashMap<>();
        map.put("total", onlineUsers.size());
        map.put("records", userOnlineList);
        return ResponseResult.success(map);
    }

    /**
     * 踢人下线
     *
     * @param token
     * @return
     */
    @Override
    public ResponseResult kick(String token) {
        logger.info("当前踢下线的用户token为:{}", token);
        StpUtil.kickoutByTokenValue(token);
        return ResponseResult.success();
    }


    /**
     * 第三方登录授权之后的逻辑
     *
     * @param response
     * @param source
     * @param request
     * @param httpServletResponse
     * @throws IOException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void authLogin(AuthResponse response, String source, HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        String result = JSONObject.toJSONString(response.getData());
        log.info("第三方登录验证结果:{}", result);

        JSONObject jsonObject = JSON.parseObject(result);
        Object uuid = jsonObject.get("uuid");
        // 获取用户ip信息
        String ipAddress = IpUtil.getIp(request);
        String ipSource = IpUtil.getIp2region(ipAddress);
        // 判断是否已注册
        User user = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, uuid));
        if (Objects.nonNull(user)) {
            // 更新登录信息
            baseMapper.update(new User(), new LambdaUpdateWrapper<User>()
                    .set(User::getLastLoginTime, LocalDateTime.now())
                    .set(User::getIpAddress, ipAddress)
                    .set(User::getIpSource, ipSource)
                    .eq(User::getId, user.getId()));

            //更新头像和昵称
            userInfoMapper.update(new UserInfo(), new LambdaUpdateWrapper<UserInfo>()
                    .set(UserInfo::getAvatar, jsonObject.get("avatar"))
                    .set(UserInfo::getNickname, jsonObject.get("nickname"))
                    .eq(UserInfo::getId, user.getUserInfoId()));

        } else {
            // 获取第三方用户信息，保存到数据库返回
            // 保存用户信息
            UserInfo userInfo = UserInfo.builder()
                    .nickname(source.equals("github") ? jsonObject.get("username").toString() : jsonObject.get("nickname").toString())
                    .avatar(jsonObject.get("avatar").toString())
                    .build();
            userInfoMapper.insert(userInfo);
            // 保存账号信息
            user = User.builder()
                    .userInfoId(userInfo.getId())
                    .username(uuid.toString())
                    .password(UUID.randomUUID().toString())
                    .loginType(LoginTypeEnum.getType(source))
                    .lastLoginTime(com.bsdlzg.blog.utils.DateUtil.getNowDate())
                    .ipAddress(ipAddress)
                    .ipSource(ipSource)
                    .roleId(2)
                    .status(UserStatusEnum.normal.getCode())
                    .build();
            baseMapper.insert(user);
        }

        StpUtil.login(user.getId(), new SaLoginModel().setDevice("PC").setTimeout(60 * 60 * 24 * 7));
        httpServletResponse.sendRedirect("http://www.bsdlzg.com/?token=" + StpUtil.getTokenValue());
    }
}
