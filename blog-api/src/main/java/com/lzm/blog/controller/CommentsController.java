package com.lzm.blog.controller;

import com.lzm.blog.service.CommentsService;
import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.params.CommentPrams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long articleId) {
        return commentsService.getCommentsByArticleId(articleId);
    }

    @PostMapping("create/change")
    public Result change(@RequestBody CommentPrams commentPrams) {

        return commentsService.change(commentPrams);

    }
}
