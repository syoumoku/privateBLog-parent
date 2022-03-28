package com.lzm.blog.controller;

import com.lzm.blog.service.TagService;
import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.TagVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tags")
public class TagsController {

    @Autowired
    private TagService tagService;

    @GetMapping("/hot")
    public Result listHotTags() {
        int limit = 6;
        List<TagVo> hots = tagService.hots(limit);
        return Result.success(hots);
    }


    @GetMapping("detail")
    public Result findAllDetail() {
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result findAllDetail(@PathVariable("id") Long id) {
        return tagService.findAllDetailById(id);
    }


    @GetMapping
    public Result listTags() {
        return tagService.listTags();
    }
}
