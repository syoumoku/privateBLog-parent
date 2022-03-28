package com.lzm.blog.vo.params;

import com.lzm.blog.dao.pojo.ArticleBody;
import com.lzm.blog.dao.pojo.Category;
import com.lzm.blog.vo.CategoryVo;
import com.lzm.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticlePrams {

    private String title;
    private Long id;
    private ArticleBodyPrams body;
    private CategoryVo category;
    private String summary;
    private List<TagVo> tags;

}
