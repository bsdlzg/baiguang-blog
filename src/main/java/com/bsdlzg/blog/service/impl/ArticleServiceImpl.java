package com.bsdlzg.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bsdlzg.blog.common.*;
import com.bsdlzg.blog.dto.ArticleDTO;
import com.bsdlzg.blog.dto.ArticleInsertDTO;
import com.bsdlzg.blog.entity.*;
import com.bsdlzg.blog.enums.PublishEnum;
import com.bsdlzg.blog.enums.YesOrNoEnum;
import com.bsdlzg.blog.exception.BusinessException;
import com.bsdlzg.blog.mapper.*;
import com.bsdlzg.blog.service.ArticleService;
import com.bsdlzg.blog.service.RedisService;
import com.bsdlzg.blog.service.SystemConfigService;
import com.bsdlzg.blog.strategy.context.SearchStrategyContext;
import com.bsdlzg.blog.utils.*;
import com.bsdlzg.blog.vo.*;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.bsdlzg.blog.common.Constants.IMG_URL_API;
import static com.bsdlzg.blog.common.Constants.OTHER_CATEGORY_ID;
import static com.bsdlzg.blog.common.RedisConstants.*;
import static com.bsdlzg.blog.common.ResultCode.*;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author bsdlzg
 * @since 2021-08-18
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    private final SystemConfigService systemConfigService;

    private final RedisService redisService;

    private final TagsMapper tagsMapper;

    private final CommentMapper commentMapper;

    private final SearchStrategyContext searchStrategyContext;

    private final RestTemplate restTemplate;

    private final HttpServletRequest request;

    private final ElasticsearchUtil elasticsearchUtil;

    private final UserInfoMapper userInfoMapper;

    @Value("${baidu.url}")
    private String baiduUrl;


    /**
     *  后台获取所有文章
     * @return
     */
    @Override
    public ResponseResult listArticle(Map<String,Object> map) {
        String loginId = StpUtil.getLoginIdAsString();
        //如果没有admin权限 即不是网站拥有者就只能获取到当前用户所拥有的文章
        if (!StpUtil.hasRole("admin")) {
            map.put("userId",loginId);
        }
        Page<SystemArticleListVO> data = baseMapper.selectArticle(new Page<>((Integer)map.get("pageNo"), (Integer)map.get("pageSize")),map);
        data.getRecords().forEach(item ->{
            SystemUserVO userInfo = userMapper.getById(item.getUserId());
            item.setNickname(userInfo.getNickname());
        });
        return ResponseResult.success(data);
    }

    /**
     *  后台获取文章详情
     * @return
     */
    @Override
    public ResponseResult getArticleById(Long id) {
        ArticleDTO articleDTO = baseMapper.selectPrimaryKey(id);
        articleDTO.setTags(tagsMapper.selectByArticleId(id));
        return ResponseResult.success(articleDTO);
    }

    /**
     *  添加文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult  insertArticle(ArticleDTO article) {
        Article blogArticle = BeanCopyUtils.copyObject(article, Article.class);
        blogArticle.setUserId(StpUtil.getLoginIdAsString());
        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle.setCategoryId(categoryId);

        int insert = baseMapper.insert(blogArticle);
        if (insert > 0) {
            tagsMapper.saveArticleTags(blogArticle.getId(),tagList);
        }
        return ResponseResult.success();
    }

    /**
     *  修改文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateArticle(ArticleDTO article) {
        Article blogArticle = baseMapper.selectById(article.getId());
        if (ObjectUtil.isNull(blogArticle)) {
            throw new BusinessException(ARTICLE_NOT_EXIST);
        }
        //只能修改属于当前登录用户的文章
        String loginId = StpUtil.getLoginIdAsString();
        if (!blogArticle.getUserId().equals(loginId) && !StpUtil.hasRole("admin")){
            throw new BusinessException(ERROR);
        }

        //添加分类
        Long categoryId = savaCategory(article.getCategoryName());
        //添加标签
        List<Long> tagList = getTagsList(article);

        blogArticle = BeanCopyUtils.copyObject(article, Article.class);
        blogArticle.setCategoryId(categoryId);
        baseMapper.updateById(blogArticle);

        //先删出所有标签
        tagsMapper.deleteByArticleIds(Collections.singletonList(blogArticle.getId()));
        //然后新增标签
        tagsMapper.saveArticleTags(blogArticle.getId(),tagList);

        return ResponseResult.success();
    }


    /**
     * 批量删除文章
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatchArticle(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        tagsMapper.deleteByArticleIds(ids);
        return ResponseResult.success();
    }

    /**
     *  置顶文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult putTopArticle(ArticleDTO article) {
        baseMapper.putTopArticle(article);
        return ResponseResult.success();
    }


    /**
     *  文章百度推送
     * @return
     */
    @Override
    public ResponseResult articleSeo(List<Long> ids) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Host", "data.zz.baidu.com");
        headers.add("User-Agent", "curl/7.12.1");
        headers.add("Content-Length", "83");
        headers.add("Content-Type", "text/plain");

        ids.forEach(item -> {
            String url = "http://www.bsdlzg.com/article/" + item;
            HttpEntity<String> entity = new HttpEntity<>(url, headers);
            restTemplate.postForObject(baiduUrl, entity, String.class);
        });
        return ResponseResult.success();
    }

    /**
     *  抓取文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult reptile(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements title  = document.getElementsByClass("title-article");
            Elements tags  = document.getElementsByClass("tag-link");
            Elements content  = document.getElementsByClass("article_content");
            if (StringUtils.isBlank(content.toString())) {
                throw new BusinessException(CRAWLING_ARTICLE_FAILED.getDesc());
            }

            //爬取的是HTML内容，需要转成MD格式的内容
            String newContent = content.get(0).toString().replaceAll("<code>", "<code class=\"lang-java\">");
            MutableDataSet options = new MutableDataSet();
            String markdown = FlexmarkHtmlConverter.builder(options).build().convert(newContent)
                    .replace("lang-java","java");

            //文章封面图片 由https://api.btstu.cn/该网站随机获取
            String strResult = restTemplate.getForObject(IMG_URL_API, String.class);
            JSONObject jsonObject = JSON.parseObject(strResult);
            Object imgUrl = jsonObject.get("imgurl");

            Article entity = Article.builder().userId(StpUtil.getLoginIdAsString()).contentMd(markdown)
                    .categoryId(OTHER_CATEGORY_ID).isOriginal(YesOrNoEnum.NO.getCode()).originalUrl(url)
                    .title(title.get(0).text()).avatar(imgUrl.toString()).content(newContent).build();

            baseMapper.insert(entity);
            //为该文章添加标签
            List<Long> tagsId = new ArrayList<>();
            tags.forEach(item ->{
                String tag = item.text();
                Tags result = tagsMapper.selectOne(new QueryWrapper<Tags>().eq(FieldConstants.NAME,tag ));
                if (result == null){
                    result = Tags.builder().name(tag).build();
                    tagsMapper.insert(result);
                }
                tagsId.add(result.getId());
            });
            tagsMapper.saveArticleTags(entity.getId(),tagsId);

            log.info("文章抓取成功，内容为:{}", JSON.toJSONString(entity));
        } catch (IOException e) {
            throw new BusinessException(e);
        }
        return ResponseResult.success();
    }

    /**
     *  发布或下架文章
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult publishAndShelf(ArticleDTO article) {
        baseMapper.publishAndShelf(article);
        return ResponseResult.success();
    }

    @Override
    public ResponseResult randomImg() {
        //文章封面图片 由https://api.btstu.cn/该网站随机获取
        String result = restTemplate.getForObject(IMG_URL_API, String.class);
        Object imgUrl = JSON.parseObject(result).get("imgurl");
        return ResponseResult.success(imgUrl);
    }

    //    ----------web端方法开始-------
    /**
     *  获取文章列表
     * @return
     */
    @Override
    public ResponseResult selectPublicArticleList(Integer categoryId,Integer tagId) {
        Page<ApiArticleListVO> articlePage = baseMapper.selectArticleList(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),
               categoryId,tagId);
        articlePage.getRecords().forEach(this::setCommentAndLike);
        return ResponseResult.success(articlePage);
    }

    /**
     *  获取文章详情
     * @return
     */
    @Override
    public ResponseResult selectPublicArticleInfo(Integer id) {
        ApiArticleInfoVO apiArticleInfoVO = baseMapper.selectArticleByIdToVO(id);
        //获取标签
        List<Tags> list = tagsMapper.selectTagByArticleId(apiArticleInfoVO.getId());
        apiArticleInfoVO.setTagList(list);
        //获取评论数量
        List<Comment> comments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>().eq(Comment::getArticleId, id));
        apiArticleInfoVO.setCommentCount(comments.size());
        //获取点赞数量
        Map<String, Object> map = redisService.getCacheMap(ARTICLE_LIKE_COUNT);
        if (map!= null && map.size() > 0){
            apiArticleInfoVO.setLikeCount(map.get(id.toString()));
        }
        //获取当前登录用户是否点赞该文章
        Object userId = StpUtil.getLoginIdDefaultNull();
        if (userId != null){
            String articleLikeKey = ARTICLE_USER_LIKE + userId;
            if (redisService.sIsMember(articleLikeKey, id)) {
                apiArticleInfoVO.setIsLike(true);
            }
        }
        //获取文章作者信息
        User user = userMapper.selectById(apiArticleInfoVO.getUserId());
        UserInfo userInfo = userInfoMapper.selectById(user.getUserInfoId());
        apiArticleInfoVO.setUserInfo(userInfo);
        //增加文章阅读量
        redisService.incrArticle(id.longValue(),ARTICLE_READING);
        return ResponseResult.success(apiArticleInfoVO);
    }

    /**
     *  获取归档
     * @return
     */
    @Override
    public ResponseResult archive() {
        List<ApiArchiveVO> articleList = baseMapper.selectListArchive();
        //按日期分组
        Map<String, List<ApiArchiveVO>> resultList = articleList.stream().collect(Collectors.groupingBy(ApiArchiveVO::getTime));
        Object[] keyArr = resultList.keySet().toArray();  //获取resultList的所有key值数组
        Arrays.sort(keyArr);
        List<Map<String,Object>> result = new ArrayList<>();
        for (int i = keyArr.length - 1; i >= 0; i--) {
            Map<String,Object> map = new HashMap<>();
            map.put("time",keyArr[i]);
            List<ApiArchiveVO> list = resultList.get(keyArr[i]);
            Collections.sort(list, (o1, o2) -> o2.getFormatTime().compareTo(o1.getFormatTime()));
            map.put("list",list);
            result.add(map);
        }
        return ResponseResult.success(result).putExtra("total",articleList.size());
    }

    /**
     *  搜索文章
     * @return
     */
    @Override
    public ResponseResult publicSearchArticle(String keywords) {
//        if (StringUtils.isBlank(keywords)) {
//            throw new BusinessException(PARAMS_ILLEGAL.getDesc());
//        }
//        //获取搜索模式（es搜索或mysql搜索）
//        SystemConfig systemConfig = systemConfigService.getCustomizeOne();
//        String strategy = SearchModelEnum.getStrategy(systemConfig.getSearchModel());
//        //搜索逻辑
//        List<ArticleSearchVO> articleSearchVOS = searchStrategyContext.executeSearchStrategy(strategy, keywords);
        Page<ApiArticleListVO> articlePage = baseMapper.publicPageSearchArticle(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()),
                keywords);
        articlePage.getRecords().forEach(item -> {
            item.setTitle(item.getTitle().replaceAll("(?i)" + keywords, Constants.PRE_TAG + keywords + Constants.POST_TAG));
            setCommentAndLike(item);
        });

        return ResponseResult.success(articlePage);
    }

    /**
     * 文章点赞
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult articleLike(Integer articleId) {
        // 判断是否点赞
        String articleLikeKey = ARTICLE_USER_LIKE + StpUtil.getLoginIdAsString();
        if (redisService.sIsMember(articleLikeKey, articleId)) {
            // 点过赞则删除文章id
            redisService.sRemove(articleLikeKey, articleId);
            // 文章点赞量-1
            redisService.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            // 未点赞则增加文章id
            redisService.sAdd(articleLikeKey, articleId);
            // 文章点赞量+1
            redisService.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
        return ResponseResult.success();
    }

    /**
     * 用户添加文章
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult publicInsertArticle(ArticleInsertDTO dto) {
        Article article = BeanCopyUtils.copyObject(dto, Article.class);
        article.setIsPublish(PublishEnum.AUDIO.code);
        if (article.getId() != null) {
            if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
                throw new BusinessException("只能修改自己的文章！");
            }
            baseMapper.updateById(article);

            //先删出所有标签
            tagsMapper.deleteByArticleIds(Collections.singletonList(article.getId()));
            //然后新增标签
            tagsMapper.saveArticleTags(article.getId(),dto.getTagList());
        }else {
            article.setUserId(StpUtil.getLoginIdAsString());
            int insert = baseMapper.insert(article);
            //添加标签
            if (insert > 0){
                tagsMapper.saveArticleTags(article.getId(),dto.getTagList());
            }
        }

        return ResponseResult.success();
    }

    /**
     * 查询我的文章
     * @return
     */
    @Override
    public ResponseResult publicSelectMyArticle() {
        Page<ApiArticleListVO> list = baseMapper.publicSelectMyArticle(new Page<>(PageUtils.getPageNo(),PageUtils.getPageSize()),StpUtil.getLoginIdAsString());
        return ResponseResult.success(list);
    }

    /**
     * 删除我的文章
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult publicDeleteMyArticle(Long id) {
        Article article = baseMapper.selectById(id);
        if (!article.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BusinessException("只能删除自己的文章！");
        }
        baseMapper.deleteById(id);
        tagsMapper.deleteByArticleIds(Collections.singletonList(id));
        return ResponseResult.success();
    }

    /**
     * 获取我的文章详情
     * @param id
     * @return
     */
    @Override
    public ResponseResult publicSelectMyArticleInfo(Long id) {
        ArticleInsertDTO articleInsertDTO  =  baseMapper.publicSelectMyArticleInfo(id);
        if (!articleInsertDTO.getUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BusinessException("只能查看自己的文章！");
        }
        List<Tags> tags = tagsMapper.selectTagByArticleId(id);
        List<Long> tagList = tags.stream().map(Tags::getId).collect(Collectors.toList());
        articleInsertDTO.setTagList(tagList);
        return ResponseResult.success(articleInsertDTO);
    }

    /**
     *  校验文章验证码(验证码通过关注公众号获取)
     * @return
     */
    @Override
    public ResponseResult checkSecret(String code) {
        //校验验证码
        String key = RedisConstants.WECHAT_CODE + code;
        Object redisCode = redisService.getCacheObject(key);
        if (ObjectUtil.isNull(redisCode)) {
            throw new BusinessException(ERROR_EXCEPTION_MOBILE_CODE.getDesc());
        }

        //将ip存在redis 有效期一天，当天无需再进行验证码校验
        List<Object> cacheList = redisService.getCacheList(CHECK_CODE_IP);
        if (cacheList.isEmpty()) {
            cacheList = new ArrayList<>();
        }
        cacheList.add(IpUtil.getIp(request));
        redisService.setCacheList(CHECK_CODE_IP,cacheList);
        //通过删除验证码
        redisService.deleteObject(key);
        return ResponseResult.success("验证成功");
    }


    //    -----自定义方法开始-------

    /**
     * 设置评论量和点赞量
     * @param item
     */
    private void setCommentAndLike(ApiArticleListVO item) {
        List<Tags> list = tagsMapper.selectTagByArticleId(item.getId());
        Integer commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getArticleId, item.getId()));
        //获取点赞数量
        Map<String, Object> map = redisService.getCacheMap(ARTICLE_LIKE_COUNT);
        if (map!= null && map.size() > 0){
            Object obj = map.get(item.getId().toString());
            item.setLikeCount(obj == null ? 0 : obj);
        }
        item.setTagList(list);
        item.setCommentCount(commentCount);
    }

    /**
     * 删除文章后的一些同步删除
     * @param ids
     */
    private void deleteAfter(List<Long> ids){
        //异步删除es文章
        elasticsearchUtil.delete(ids);
    }

    /**
     * 将数据库不存在的标签新增
     * @param article
     * @return
     */
    private List<Long> getTagsList(ArticleDTO article) {
        List<Long> tagList = new ArrayList<>();
        article.getTags().forEach(item ->{
            Tags tags = tagsMapper.selectOne(new QueryWrapper<Tags>().eq(FieldConstants.NAME, item));
            if (tags == null){
                tags = Tags.builder().name(item).sort(0).build();
                tagsMapper.insert(tags);
            }
            tagList.add(tags.getId());
        });
        return tagList;
    }

    /**
     * 如果分类不存在则新增
     * @param categoryName
     * @return
     */
    private Long savaCategory(String categoryName) {
        Category category = categoryMapper.selectOne(new QueryWrapper<Category>().eq(FieldConstants.NAME, categoryName));
        if (category == null){
            category = Category.builder().name(categoryName).sort(0).build();
            categoryMapper.insert(category);
        }
        return category.getId();
    }
}
