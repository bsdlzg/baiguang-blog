package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.entity.Menu;

import java.util.List;

/**
 * @author bsdlzg
 * @description:
 * @date 2021/8/20 9:30
 */
public interface MenuService extends IService<Menu>{

    List<Menu> listMenuTree(List<Menu> list);

    ResponseResult listMenuApi(Integer id);

    ResponseResult insertMenu(Menu menu);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenuById(Integer id);
}
