package com.bsdlzg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bsdlzg.blog.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 系统管理-角色表  Mapper 接口
 * </p>
 *
 * @author bsdlzg
 * @since 2021-07-30
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    Integer queryByUserId(Object userId);

    List<Integer> queryByRoleMenu(Integer roleId);

    void delByRoleId(@Param("roleId") Integer roleId,@Param("menus") List<Integer> menus);

    void insertBatchByRole(@Param("menus") List<Integer> menus, @Param("roleId") Integer roleId);

    List<String> selectByUserId(Object loginId);
}
