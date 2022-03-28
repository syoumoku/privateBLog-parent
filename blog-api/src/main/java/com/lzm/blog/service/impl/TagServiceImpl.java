package com.lzm.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzm.blog.dao.mapper.TagMapper;
import com.lzm.blog.dao.pojo.Tag;
import com.lzm.blog.service.TagService;
import com.lzm.blog.vo.Result;
import com.lzm.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {
        //mybaitsplus 无法进行多表查询
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
    }

    @Override
    public List<TagVo> hots(int limit) {
        List<Long> tagIdsList = tagMapper.findHotsTagIds(limit);
        if (CollectionUtils.isEmpty(tagIdsList)) {
            return Collections.emptyList();
        }
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIdsList);
        return copyList(tagList);
    }

    @Override
    public Result listTags() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getTagName);
        List<Tag> tagList = tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tagList));
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tagList = tagMapper.selectList(new QueryWrapper<>());

        return Result.success(copyList(tagList));
    }

    @Override
    public Result findAllDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }


    public List<TagVo> copyList(List<Tag> tags) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag record: tags
             ) {
            tagVoList.add(copy(record));
        }
        return tagVoList;
    }

    private TagVo copy(Tag record) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(record, tagVo);
        return tagVo;
    }



}
