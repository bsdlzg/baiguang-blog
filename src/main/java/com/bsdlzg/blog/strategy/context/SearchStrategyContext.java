package com.bsdlzg.blog.strategy.context;

import com.bsdlzg.blog.strategy.FileUploadStrategy;
import com.bsdlzg.blog.strategy.SearchStrategy;
import com.bsdlzg.blog.utils.SpringContextUtils;
import com.bsdlzg.blog.vo.ApiArticleSearchVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author bsdlzg
 * @date 2022/1/5
 * @apiNote 搜索策略上下文
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SearchStrategyContext {

    private final SpringContextUtils springContextUtils;

    /**
     * 执行搜索策略
     *
     * @param keywords 关键字
     * @return {@link List<  ApiArticleSearchVO  >} 搜索文章
     */
    public List<ApiArticleSearchVO> executeSearchStrategy(String searchMode, String keywords) {
        return ((SearchStrategy) springContextUtils.getBean(searchMode)).searchArticle(keywords);
    }

}
