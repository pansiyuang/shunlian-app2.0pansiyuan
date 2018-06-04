package com.shunlian.app.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class ShareInfoParam implements Serializable {
    public String shareLink;
    public String title;
    public String desc;
    public String img;
    public String photo;
    public String type;
    //店铺分享
    public String shop_star;
    public String shop_logo;
    public String shop_name;
    public String code_link;

    public String userName;//用户名
    public String userAvatar;//用户头像

    public String goodsPrice;//商品价格

    public ArrayList<String> downloadPic;//需要下载的图片

    //发现
    public String thumb_type;//0小图（左右布局，图在右侧），1大图（上下布局，图是通栏显示）
    public String start_time;//活动开始时间
    public String act_label;//活动开始时间

    //是否是优品  默认不是
    public boolean isSuperiorProduct;
}
