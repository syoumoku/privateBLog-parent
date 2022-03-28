package com.lzm.blog.dao.pojo;

import lombok.Data;

@Data
public class Article {

    public static final int Article_TOP = 1;

    public static final int Article_Common = 0;

    private Long id;

    private String title;

    private String summary;

    //Will be set zero when doing the insertion operation in database
    private Integer commentCounts;

    private Integer viewCounts;

    /**
     * author id
     */
    private Long authorId;
    /**
     * body id
     */
    private Long bodyId;
    /**
     *category id
     */
    private Long categoryId;

    /**
     * highlight weight
     */
    private Integer weight;


    /**
     * create Date
     */
    private Long createDate;



}
