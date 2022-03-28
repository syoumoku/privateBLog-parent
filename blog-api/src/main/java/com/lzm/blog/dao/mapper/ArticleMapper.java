package com.lzm.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzm.blog.dao.dos.Archive;
import com.lzm.blog.dao.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    List<Archive> listarchives();

    IPage<Article> listArticles(Page<Article> page,
                                Long categoryId,
                                Long tagId,
                                String year,
                                String month);
}
