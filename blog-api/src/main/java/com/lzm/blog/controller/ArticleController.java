package com.lzm.blog.controller;


import com.lzm.blog.common.aop.LogAnnotation;
import com.lzm.blog.common.cache.Cache;
import com.lzm.blog.dao.pojo.Article;
import com.lzm.blog.service.ArticleService;
import com.lzm.blog.vo.ArticleVo;
import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.params.ArticlePrams;
import com.lzm.blog.vo.params.PagePrams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * HomePage Article List
     *
     * @param pagePrams
     * @return
     */
    @LogAnnotation(module = "articles", operator = "get the list of articles")
    @PostMapping
    public Result listArticle(@RequestBody PagePrams pagePrams) {

        return articleService.listArticle(pagePrams);
    }


    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000, name = "hot_article")
    public Result hotArticle() {
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    @PostMapping("new")
    public Result newArticles() {
        int limit = 5;
        return articleService.newArticles(limit);
    }

    @PostMapping("listArchives")
    public Result Listarchives() {
        return articleService.listarchives();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long findArticleById) {
        return articleService.findArticleById(findArticleById);
    }

    @PostMapping("publish")
    public Result articlePublish(@RequestBody ArticlePrams articlePrams) {
        return articleService.articlePublish(articlePrams);
    }


}
