package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/3/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UseCommentEntity {

    public FindCommentListEntity.ItemComment insert_item;

    @Override
    public String toString() {
        return "UseCommentEntity{" +
                "insert_item=" + insert_item +
                '}';
    }
}
