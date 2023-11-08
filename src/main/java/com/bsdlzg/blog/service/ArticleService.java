package com.bsdlzg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsdlzg.blog.common.ResponseResult;
import com.bsdlzg.blog.dto.ArticleDTO;
import com.bsdlzg.blog.dto.ArticleInsertDTO;
import com.bsdlzg.blog.entity.Article;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-08-18
 */
public interface ArticleService extends IService<Article> {

    /**
     * 后台分页获取文章
     * @param map 参数map
     * @return
     */
    ResponseResult listArticle(Map<String,Object> map);

    /**
     * 后台根据主键获取文章详情
     * @param id 主键id
     * @return
     */
    ResponseResult getArticleById(Long id);

    /**
     * 添加文章
     * @param article 文章对象
     * @return
     */
    ResponseResult insertArticle(ArticleDTO article);

    /**
     * 修改文章
     * @param article 文章对象
     * @return
     */
    ResponseResult updateArticle(ArticleDTO article);


    /**
     * 后台批量删除文章
     * @param ids 文章id集合
     * @return
     */
    ResponseResult deleteBatchArticle(List<Long> ids);

    /**
     * 置顶文章
     * @param article 文章对象
     * @return
     */
    ResponseResult putTopArticle(ArticleDTO article);

    /**
     * 发布或下架文章
     * @param article 文章对象
     * @return
     */
    ResponseResult publishAndShelf(ArticleDTO article);

    /**
     * 百度seo
     * @param ids 文章id集合
     * @return
     */
    ResponseResult articleSeo(List<Long> ids);

    /**
     * 爬取文章
     * @param url 文章地址
     * @return
     */
    ResponseResult reptile(String url);

    /**
     * 随机获取图片
     * @return
     */
    ResponseResult randomImg();




    //    ----------web端开始------

    /**
     * 首页分页获取文章
     * @return
     */
    public ResponseResult selectPublicArticleList(Integer categoryId,Integer tagId);

    /**
     * 首页获取文章详情
     * @param id 文章id
     * @return
     */
    public ResponseResult selectPublicArticleInfo(Integer id);


    /**
     * 校验秘钥
     * @param code 验证码
     * @return
     */
    public ResponseResult checkSecret(String code);

    /**
     * 文章归档
     * @return
     */
    public ResponseResult archive();

    /**
     * 搜索文章
     * @param keywords 搜索关键词
     * @return
     */
    public ResponseResult publicSearchArticle(String keywords);

    /**
     * 文章点赞
     * @param articleId 文章id
     * @return
     */
    public ResponseResult articleLike(Integer articleId);

    /**
     * 用户添加文章
     * @param dto
     * @return
     */
    public ResponseResult publicInsertArticle(ArticleInsertDTO dto);

    /**
     * 查询我的文章
     * @return
     */
    public ResponseResult publicSelectMyArticle();

    /**
     * 删除我的文章
     * @param id
     * @return
     */
    ResponseResult publicDeleteMyArticle(Long id);

    /**
     * 获取我的文章详情
     * @param id
     * @return
     */
    ResponseResult publicSelectMyArticleInfo(Long id);
}
