package com.bsdlzg.blog.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ApiTagVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "标签名")
    private String name;

    @ApiModelProperty(value = "封面图")
    private String avatar;

    @ApiModelProperty(value = "文章量")
    private Integer articleCount;
}
