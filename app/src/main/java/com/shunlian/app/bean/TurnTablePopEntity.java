package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/9/4.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TurnTablePopEntity {
    public int show;
    public String text;
    public Popup list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Popup {
        public String meg;
        public String tmt_id;
        public String thumb;
    }
}
