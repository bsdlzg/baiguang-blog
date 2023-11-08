package com.bsdlzg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsdlzg.blog.entity.Tags;
import com.bsdlzg.blog.vo.ApiTagVO;
import com.bsdlzg.blog.vo.SystemTagListVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客标签表 Mapper 接口
 * </p>
 *
 * @author bsdlzg
 * @since 2021-07-19
 */
@Repository
public interface TagsMapper extends BaseMapper<Tags> {

    /**
     * 添加标签
     * @param articleId
     * @param tags
     */
    void saveArticleTags(@Param("articleId") Long articleId, @Param("tags")List<Long> tags);

    /**
     * 根据id删除文章对应中间表数据
     * @param ids
     */
    void deleteByArticleIds(@Param("ids") List<Long> ids);

    List<String> selectByArticleId(Long articleId);


    /**
     * 分页获取标签
     * @param objectPage
     * @param name
     * @return
     */
    Page<SystemTagListVo> selectPageRecord(@Param("page") Page<Tags> objectPage, @Param("name") String name);

    /**
     * 标签统计
     * @return
     */
    @MapKey("id")
    List<Map<String, Object>> countTags();


    /**
     * 根据文章id获取标签
     * @param articleId
     * @return
     */
    List<Tags> selectTagByArticleId(Long articleId);

    /**
     * 首页标签墙
     * @return
     */
    List<ApiTagVO> selectListCountArticle();

}
