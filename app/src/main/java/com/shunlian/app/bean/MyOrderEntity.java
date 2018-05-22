package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyOrderEntity {

    public String status;
    public String count;
    public String total_page;
    public String page;
    public String page_size;
    public List<Orders> orders;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Orders {
        public String id;
        public String order_sn;
        public String total_amount;
        public String store_name;
        public String shipping_fee;
        public String store_id;
        public String status_text;
        public String is_append;
        public String status;
        public String create_time;
        public String is_edit_praise;
        public String express_sn;
        public String is_postpone; //是否申请过延长收货    1 申请过    0未申请
        public String qty;
        public List<OrderGoodsBean> order_goods;

        @Override
        public String toString() {
            return "Orders{" +
                    "id='" + id + '\'' +
                    ", total_amount='" + total_amount + '\'' +
                    ", store_name='" + store_name + '\'' +
                    ", shipping_fee='" + shipping_fee + '\'' +
                    ", store_id='" + store_id + '\'' +
                    ", status_text='" + status_text + '\'' +
                    ", is_append='" + is_append + '\'' +
                    ", status='" + status + '\'' +
                    ", is_edit_praise='" + is_edit_praise + '\'' +
                    ", qty='" + qty + '\'' +
                    ", order_goods=" + order_goods +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderGoodsBean {
        public String goods_id;
        public String order_sn;
        public String title;
        public String thumb;
        public String qty;
        public String market_price;
        public String price;
        public String sku_desc;
        public String offered;
        public String comment_id;
        public String is_append;
        public String goodsimage;
        public String is_edit_praise;
        public String og_id;//订单商品表id
        public String is_refund;//售后状态
    }
}
