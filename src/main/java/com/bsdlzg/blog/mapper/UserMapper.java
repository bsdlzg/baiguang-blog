package com.bsdlzg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsdlzg.blog.entity.User;
import com.bsdlzg.blog.vo.SystemUserInfoVO;
import com.bsdlzg.blog.vo.SystemUserVO;
import com.bsdlzg.blog.vo.UserInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统管理-用户基础信息表 Mapper 接口
 * </p>
 *
 * @author bsdlzg
 * @since 2021-07-30
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
    void insertBackId(User user);

    List<Integer> getMenuId(String userId);

    Page<SystemUserInfoVO> selectPageRecord(@Param("page") Page<SystemUserInfoVO> page, @Param("username")String username, @Param("loginType")Integer loginType);

    SystemUserVO getById(Object id);

    User selectNameAndPassword(@Param("username") String username, @Param("password") String password);

    void updateLoginInfo(@Param("loginId")Object loginId,@Param("ip") String ip, @Param("cityInfo")String cityInfo,
                         @Param("os") String os,@Param("browser") String browser);

    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    UserInfoVO selectByUserName(String username);

}
