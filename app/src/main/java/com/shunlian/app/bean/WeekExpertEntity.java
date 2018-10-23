package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/19.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeekExpertEntity {

    public List<Expert> list;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Expert {
        public String member_id;
        public String avatar;
        public String nickname;
        public String hot_val;
        public int focus_status;
    }
}
