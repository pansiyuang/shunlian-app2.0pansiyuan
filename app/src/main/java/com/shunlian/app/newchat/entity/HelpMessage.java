package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpMessage extends BaseMessage {
    public HelpBody msg_body;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HelpBody {
        public String help_item;
        public List<HelpEntity> help_list;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HelpEntity {
        public String id;
        public String item;
    }
}
