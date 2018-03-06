package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonShopEntity {
    public String nickname;
    public String avatarl;
    public String level;
    public List<GoodsDeatilEntity.Goods> goods_list;
    public String qrcode;
    public String member_role;
    public ShareInfo shareInfo;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShareInfo {
        public String title;
        public String desc;
        public String img;
        public String qrcode_img;
        public String wx_link;
    }
}
