package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.Role;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-11-09
 */
public interface RoleService extends IService<Role> {


    ResponseResult listRole(String name);

     ResponseResult insertRole(Role role);

    ResponseResult updateRole(Role role);

    ResponseResult deleteBatch(List<Integer> ids);

    ResponseResult getCurrentUserRole();

    ResponseResult selectById(Integer roleId);

}
