package com.bsdlzg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsdlzg.blog.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bsdlzg
 * @since 2021-12-25
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    void deleteByUserIds(@Param("ids") List<String> ids);

    /**
     * 根据用户id获取用户信息
     * @param loginId 用户id
     * @return
     */
    UserInfo getByUserId(Object loginId);

}
