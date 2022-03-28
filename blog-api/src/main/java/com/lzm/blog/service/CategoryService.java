package com.lzm.blog.service;

import com.lzm.blog.vo.CategoryVo;
import com.lzm.blog.vo.Result;

public interface CategoryService {
    
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetails();

    Result findAllDetailsById(Long id);
}
