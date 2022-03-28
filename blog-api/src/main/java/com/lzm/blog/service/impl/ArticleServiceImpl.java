package com.lzm.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzm.blog.dao.dos.Archive;
import com.lzm.blog.dao.mapper.ArticleBodyMapper;
import com.lzm.blog.dao.mapper.ArticleMapper;
import com.lzm.blog.dao.mapper.ArticleTagMapper;
import com.lzm.blog.dao.pojo.*;
import com.lzm.blog.service.*;
import com.lzm.blog.utils.UserThreadLocal;
import com.lzm.blog.vo.*;
import com.lzm.blog.vo.params.ArticlePrams;
import com.lzm.blog.vo.params.PagePrams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private CategoryService categoryService;;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ArticleTagMapper articleTagMapper;





    /*@Override
    public List<ArticleVo> listArticle(PagePrams pagePrams) {
        */

    @Override
    public Result listArticle(PagePrams pagePrams) {
        Page<Article> page = new Page<>(pagePrams.getPage(), pagePrams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticles(
                page,
                pagePrams.getCategoryId(),
                pagePrams.getTagId(),
                pagePrams.getYear(),
                pagePrams.getMonth());


        return Result.success(copyList(articleIPage.getRecords(), true, true));
    }

    /**
         * 1. select page article database table
         *//*
        Page<Article> page = new Page<>(pagePrams.getPage(), pagePrams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();

        if (pagePrams.getCategoryId() != null) {
            queryWrapper.eq(Article::getCategoryId, pagePrams.getCategoryId());
        }
        List<Long> articleIds = new ArrayList<>();

        if (pagePrams.getTagId() != null) {
            //select
            LambdaQueryWrapper<ArticleTag> tagQueryWrapper = new LambdaQueryWrapper<>();
            tagQueryWrapper.eq(ArticleTag::getTagId, pagePrams.getTagId());
            List<ArticleTag> articleTags = articleTagMapper.selectList(tagQueryWrapper);

            for (ArticleTag articleTag : articleTags) {
                articleIds.add(articleTag.getArticleId());
            }
            if (articleIds.size() > 0) {
                queryWrapper.in(Article::getId, articleIds);
            }
        }



        //order by featured and create_data desc
        queryWrapper.orderByDesc(Article::getWeight, Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
         List<ArticleVo> articleVoList = copyList(records, true, true);
        return articleVoList;
    }*/

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        //select id, title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId, Article::getTitle);
        queryWrapper.last("limit " + limit);
        //select id, title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles, false, false));
    }

    @Override
    public Result listarchives() {
        List<Archive> archiveList = articleMapper.listarchives();
        return Result.success(archiveList);
    }


    @Autowired
    private ThreadService threadService;

    @Override
    public Result findArticleById(Long articleId) {
        Article article = this.articleMapper.selectById(articleId);

        // incrementing the viewcounts after viewing
        // it is supposed to return the select data directly, however an update sql is added which will add write lock and block other read operation
        // resulting in decrease of efficiency
        // update operation increase the execution time of using this API
        // if there is exception during the update, the function for viewing article should not be influenced
        // we use Thread Pool to optimize this problem
        threadService.updateArticleViewCount(articleMapper, article);
        return Result.success(copy(article, true, true, true, true));
    }

    @Override
    public Result articlePublish(ArticlePrams articlePrams) {

        //TODO
        SysUser sysUser = UserThreadLocal.get();
        //insert Article
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articlePrams.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        article.setTitle(articlePrams.getTitle());
        article.setSummary(articlePrams.getSummary());
        articleMapper.insert(article);
        //insert tags
        List<TagVo> tagVoList = articlePrams.getTags();
        if (tagVoList != null) {
            for (TagVo vo : tagVoList) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(vo.getId());
                articleTagMapper.insert(articleTag);
            }
        }
       //insert article body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articlePrams.getBody().getContent());
        articleBody.setContentHtml(articlePrams.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());

        return Result.success(articleVo);
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record: records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, isCategory));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record: records) {
            articleVoList.add(copy(record, isTag, isAuthor, false, false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record: records) {
            articleVoList.add(copy(record, isTag, isAuthor, isBody, false));
        }
        return articleVoList;
    }



    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if (isTag) {
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody) {
            ArticleBodyVo articleBodyVo = findArticleBodyById(article.getId());
            articleVo.setBody(articleBodyVo);
        }
        if (isCategory) {
            CategoryVo categoryVo = findCategoryById(article.getCategoryId());
            articleVo.setCategory(categoryVo);
        }

        return articleVo;
    }

    private CategoryVo findCategoryById(Long CategoryId) {
        return categoryService.findCategoryById(CategoryId);
    }


    private ArticleBodyVo findArticleBodyById(Long articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId, articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }
}
