package com.lzm.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzm.blog.dao.mapper.CommentMapper;
import com.lzm.blog.dao.pojo.Comment;
import com.lzm.blog.dao.pojo.SysUser;
import com.lzm.blog.service.CommentsService;
import com.lzm.blog.service.SysUserService;
import com.lzm.blog.utils.UserThreadLocal;
import com.lzm.blog.vo.CommentVo;
import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.params.CommentPrams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result getCommentsByArticleId(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, articleId);
        queryWrapper.eq(Comment::getLevel, 1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return Result.success(copyList(comments));
    }

    @Override
    public Result change(CommentPrams commentPrams) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setContent(commentPrams.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        comment.setArticleId(commentPrams.getArticleId());
        comment.setAuthorId(sysUser.getId());
        if (commentPrams.getParent() != null && commentPrams.getParent() != 0){
            comment.setParentId(commentPrams.getParent());
            comment.setLevel(2);
            comment.setToUid(commentPrams.getToUserId());
        } else {
            comment.setLevel(1);
            comment.setToUid(0L);
            comment.setParentId(0L);
        }
        commentMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment com: comments
             ) {
            commentVoList.add(copy(com));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment com) {
        CommentVo commentVo = new CommentVo();
//        commentVo.setId(com.getId());
//        commentVo.setContent(com.getContent());
        BeanUtils.copyProperties(com, commentVo);
        commentVo.setAuthor(sysUserService.findUserVoById(com.getAuthorId()));
        commentVo.setCreateDate(new DateTime(com.getCreateDate()).toString("yyyy-MM-ddHH:mm"));
        if (com.getLevel() == 1) {
            List<CommentVo> childrenVo = findCommentsByParentId(com.getId());
            commentVo.setChildrens(childrenVo);
        }
        if (com.getLevel() > 1) {
            commentVo.setToUser(sysUserService.findUserVoById(com.getToUid()));
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        List<Comment> childrenComments = commentMapper.selectList(queryWrapper);
        return copyList(childrenComments);
    }
}
