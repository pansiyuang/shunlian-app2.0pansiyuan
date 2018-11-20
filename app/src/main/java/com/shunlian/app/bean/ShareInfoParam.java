package com.shunlian.app.bean;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shunlian.app.utils.BitmapUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShareInfoParam implements Serializable {
    @JsonProperty("share_url")
    public String shareLink;
    public String title;
    public String special_img_url;//专题页图文分享图片
    @JsonProperty("content")
    public String desc;
    @JsonProperty("pic")
    public String img;
    public String photo;
    public String type;
    public Bitmap bitmap;
    public boolean isCopyTitle;
    //店铺分享
    public String shop_star;
    public String shop_id;
    public String shop_logo;
    public String shop_name;
    public String code_link;
    public String market_price;
    @JsonProperty("nick_name")
    public String userName;//用户名
    @JsonProperty("portrait")
    public String userAvatar;//用户头像
    public String   price;
    public String goodsPrice;//商品价格

    public ArrayList<String> downloadPic;//需要下载的图片

    public String blogId;
    //发现
    public String thumb_type;//0小图（左右布局，图在右侧），1大图（上下布局，图是通栏显示）
    public String start_time;//活动开始时间
    public String act_label;//活动开始时间
    public String video_url;//视频地址

    public boolean isShowTiltle = false;
    public boolean isSuperiorProduct;
    public String goods_id;//商品id

    public String pic;
    public String logo;
    public boolean isSpecial = false;

    public List<StoreGoodsListEntity.MData> share_goods;
    public int egg_type = 0;//0:没有金蛋 1：商品详情有金蛋 2：店铺详情有金蛋
    @Override
    public String toString() {
        return "ShareInfoParam{" +
                "shareLink='" + shareLink + '\'' +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", img='" + img + '\'' +
                ", photo='" + photo + '\'' +
                ", type='" + type + '\'' +
                ", shop_star='" + shop_star + '\'' +
                ", shop_logo='" + shop_logo + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", code_link='" + code_link + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", goodsPrice='" + price + '\'' +
                ", downloadPic=" + downloadPic +
                ", thumb_type='" + thumb_type + '\'' +
                ", start_time='" + start_time + '\'' +
                ", act_label='" + act_label + '\'' +
                ", market_price='" + market_price + '\'' +
                ", isSuperiorProduct=" + isSuperiorProduct +
                '}';
    }


    /**********网络获取分享信息*****************/
    /*
    "title":"标题",
    "content":"内容简述",
    "pic":"配图",
    "logo":"配图",
    "share_url":"分享链接",
    "portrait":"用户头像",
    "nick_name":"用户昵称"
    */
}
