package com.lzm.blog.dao.dos;

import lombok.Data;

@Data
public class Archive {
    private Integer year;
    private Integer month;
    private Long count;
}
