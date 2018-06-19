package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommondEntity implements Serializable{
    public String type;
    public String share_code;
    public TypeData type_data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TypeData implements Serializable{
        public String id;
        public String title;
        public String thumb;
        public String price;
        public String from_user;
    }
}
