package com.lzm.blog.service;

import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    List<TagVo> hots(int limit);

    Result listTags();

    Result findAllDetail();

    Result findAllDetailById(Long id);
}
