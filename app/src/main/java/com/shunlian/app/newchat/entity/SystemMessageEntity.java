package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2018/5/22.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemMessageEntity {
    public SystemMsg sysMsg;
    public DiscoveryMsg discovery;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SystemMsg {
        public String title;
        public String type;
        public int is_read;
        public String date;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DiscoveryMsg {
        public String title;
        public String type;
        public int is_read;
        public String date;
    }
}
