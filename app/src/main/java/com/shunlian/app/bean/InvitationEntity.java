package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shunlian.app.newchat.entity.BaseEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvitationEntity extends BaseEntity {
    public List<Invitation> list;
    public Pager pager;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Invitation {
        public String avatar;
        public String nickname;
        public String role_desc;
        public int role;
        public String enter_time;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public int page;
        public int page_size;
        public int count;
        public int total_page;
    }
}
