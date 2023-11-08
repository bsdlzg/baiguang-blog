package com.bsdlzg.blog.vo;

import lombok.Data;

@Data
public class ApiCategoryListVO {
    /**
     * id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String name;

    /**
     * 封面图
     */
    private  String avatar;

    /**
     * 文章量
     */
    private Integer articleCount;
}
