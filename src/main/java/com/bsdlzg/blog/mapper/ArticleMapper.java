package com.bsdlzg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsdlzg.blog.dto.ArticleDTO;
import com.bsdlzg.blog.dto.ArticleInsertDTO;
import com.bsdlzg.blog.entity.Article;
import com.bsdlzg.blog.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 文章 Mapper 接口
 * @author bsdlzg
 * @since 2021-08-18
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 后台分页获取文章
     * @param page 分页对象
     * @param map 参数map
     * @return
     */
    Page<SystemArticleListVO> selectArticle(@Param("page") Page<Object> page, @Param("param") Map<String,Object> map);

    /**
     * 后台根据主键获取文章详情
     * @param id 主键id
     * @return
     */
    ArticleDTO selectPrimaryKey(Long id);

    /**
     * 置顶文章
     * @param article 文章对象
     */
    void putTopArticle(@Param("article") ArticleDTO article);

    /**
     * 发布或下架文章
     * @param article 文章对象
     */
    void publishAndShelf(@Param("article") ArticleDTO article);

    /**
     * 文章贡献度
     * @param lastTime 开始时间
     * @param nowTime 结束时间
     * @return
     */
    List<SystemArticleContributeVO> contribute(@Param("lastTime") String lastTime, @Param("nowTime")String nowTime);



    //-----------------------门户端方法分割线-------------------------------
    /**
     * 首页分页获取归档
     * @return
     */
    List<ApiArchiveVO> selectListArchive();

    /**
     * 首页分页获取文章
     * @param page 分页对象
     * @param categoryId 分类id
     * @param tagId 标签id
     * @return
     */
    Page<ApiArticleListVO> selectArticleList(Page<Object> page,
                                             @Param("categoryId") Integer categoryId,@Param("tagId")Integer tagId);


    /**
     * 获取文章详情
     * @param id
     * @return
     */
    ApiArticleInfoVO selectArticleByIdToVO(Integer id);

    /**
     * 获取推荐的文章
     * @return
     */
    List<SystemArticleListVO> selectListByBanner();

    /**
     * 获取最新文章
     * @return
     */
    List<ApiArticleListVO> selectUpToDateArticle();

    /**
     * 获取所搜框热门文章
     * @return
     */
    List<ApiArticleListVO> selectHotArticleList();

    /**
     * 根据关键词分页搜索文章
     * @param objectPage
     * @param keywords
     * @return
     */
    Page<ApiArticleListVO> publicPageSearchArticle(@Param("page") Page<Object> objectPage, @Param("keywords") String keywords);


    /**
     * 查询我的文章
     * @return
     */
    Page<ApiArticleListVO> publicSelectMyArticle(@Param("page")Page<Object> objectPage,@Param("userId") String userId);

    /**
     * 获取我的文章详情
     * @param id
     * @return
     */
    ArticleInsertDTO publicSelectMyArticleInfo(Long id);
}
