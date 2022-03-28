package com.lzm.blog.vo.params;


import lombok.Data;

@Data
public class PagePrams {

    private int page = 1;

    private int pageSize = 10;

    private Long categoryId;

    private Long tagId;

    private String year;

    private String month;

    public String getMonth() {
        if (this.month != null && this.month.length() == 1) {
            return "0" + this.month;
        } else {
            return this.month;
        }
    }
}
