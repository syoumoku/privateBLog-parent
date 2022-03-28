package com.lzm.blog.service;

import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.params.ArticlePrams;
import com.lzm.blog.vo.params.PagePrams;


public interface ArticleService {

    Result listArticle(PagePrams pagePrams);

    Result hotArticle(int limit);

    Result newArticles(int limit);

    Result listarchives();

    Result findArticleById(Long findArticleById);

    Result articlePublish(ArticlePrams articlePrams);
}
