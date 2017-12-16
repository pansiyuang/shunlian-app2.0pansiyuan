package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderLogisticsEntity {
    public String thumb;
    public String express_com;
    public String express_sn;
    public String express_phone;
    public NowStatus now_status;
    public List<Trace> traces;
    public String qty;
    public History history;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NowStatus {
        public String AcceptTime;
        public String AcceptStation;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Trace {
        public String AcceptTime;
        public String AcceptStation;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class History {
        public List<MarkInfo> mark_info;
        public List<FootMark> mark_data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MarkInfo {
        public String date;
        public String date_normal;
        public String count;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FootMark {
        public String id;
        public String goods_id;
        public String title;
        public String thumb;
        public String price;
        public String view_number;
        public String date;
        public String date_normal;
    }
}
