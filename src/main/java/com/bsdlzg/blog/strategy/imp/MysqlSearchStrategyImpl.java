package com.bsdlzg.blog.strategy.imp;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bsdlzg.blog.common.Constants;
import com.bsdlzg.blog.entity.Tags;
import com.bsdlzg.blog.mapper.ArticleMapper;
import com.bsdlzg.blog.mapper.TagsMapper;
import com.bsdlzg.blog.strategy.SearchStrategy;
import com.bsdlzg.blog.utils.PageUtils;
import com.bsdlzg.blog.vo.ApiArticleListVO;
import com.bsdlzg.blog.vo.ApiArticleSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("mysqlStrategyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MysqlSearchStrategyImpl implements SearchStrategy {

    private final ArticleMapper articleMapper;
    private final TagsMapper tagsMapper;

    @Override
    public List<ApiArticleSearchVO> searchArticle(String keywords){
        // 搜索文章
        Page<ApiArticleListVO> articlePage = articleMapper.publicPageSearchArticle(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),
                keywords);
        articlePage.getRecords().forEach(item -> {
            List<Tags> list = tagsMapper.selectTagByArticleId(item.getId());
            item.setTagList(list);
        });
        //高亮处理
        return articlePage.getRecords().stream().map(item -> {
            // 获取关键词第一次出现的位置
            String articleContent = item.getTitle();
            int index = item.getTitle().indexOf(keywords);
            if (index != -1) {
                // 获取关键词前面的文字
                int preIndex = index > 25 ? index - 25 : 0;
                String preText = item.getTitle().substring(preIndex, index);
                // 获取关键词到后面的文字
                int last = index + keywords.length();
                int postLength = item.getTitle().length() - last;
                int postIndex = postLength > 175 ? last + 175 : last + postLength;
                String postText = item.getTitle().substring(index, postIndex);
                // 文章内容高亮
                articleContent = (preText + postText).replaceAll(keywords, Constants.PRE_TAG + keywords + Constants.POST_TAG);
            }
            // 文章标题高亮
            String articleTitle = item.getTitle().replaceAll(keywords, Constants.PRE_TAG + keywords + Constants.POST_TAG);
            return ApiArticleSearchVO.builder()
                    .id(item.getId())
                    .title(articleTitle)
                    .content(articleContent)
                    .build();
        }).collect(Collectors.toList());
    }
}
