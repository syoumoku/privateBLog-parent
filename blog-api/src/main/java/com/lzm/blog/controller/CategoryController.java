package com.lzm.blog.controller;

import com.lzm.blog.service.CategoryService;
import com.lzm.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result listCategory() {
        return categoryService.findAll();
    }

    @GetMapping("detail")
    public Result details() {
        return categoryService.findAllDetails();
    }

    @GetMapping("detail/{id}")
    public Result detailsById(@PathVariable("id") Long id) {
        return categoryService.findAllDetailsById(id);
    }
}
