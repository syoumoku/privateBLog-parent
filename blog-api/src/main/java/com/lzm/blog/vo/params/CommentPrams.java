package com.lzm.blog.vo.params;

import com.lzm.blog.dao.pojo.Article;
import lombok.Data;

@Data
public class CommentPrams {
    private Long articleId;
    private String content;
    private Long parent;
    private Long toUserId;
}
