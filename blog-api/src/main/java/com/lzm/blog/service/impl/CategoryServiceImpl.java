package com.lzm.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzm.blog.dao.mapper.CategoryMapper;
import com.lzm.blog.dao.pojo.Category;
import com.lzm.blog.service.CategoryService;
import com.lzm.blog.vo.CategoryVo;
import com.lzm.blog.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
       /* categoryVo.setCategoryName(category.getCategoryName());
        categoryVo.setAvatar(category.getAvatar());
        categoryVo.setId(category.getId());*/
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }





    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId, Category::getCategoryName);
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);


        return Result.success(copyList(categoryList));
    }

    @Override
    public Result findAllDetails() {
        List<Category> categoryList = categoryMapper.selectList(new QueryWrapper<>());

        return Result.success(copyList(categoryList));
    }

    @Override
    public Result findAllDetailsById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }

    private List<CategoryVo> copyList(List<Category> categoryList) {
        List<CategoryVo> result = new ArrayList<>();
        for (Category c: categoryList
             ) {
            result.add(copy(c));
        }
        return result;
    }

    private CategoryVo copy(Category c) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(c, categoryVo);
        return categoryVo;
    }
}
