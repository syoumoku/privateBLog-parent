package com.lzm.blog.service;

import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.params.CommentPrams;

public interface CommentsService {
    Result getCommentsByArticleId(Long articleId);

    Result change(CommentPrams commentPrams);
}
