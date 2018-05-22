package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shunlian.app.bean.MyOrderEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderMessage extends BaseMessage {
    public OrderMessageBody msg_body;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderMessageBody {
        public Order order;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Order {
        public String ordersn;
        public String orderId;
        public String realname;
        public String store_name;
        public String create_time;
        public String store_id;
        public String price;
        public String phone;
        public String address;
        public String status;
        public String express_com;
        public String express_sn;
        public String express_code;
        public List<OrderGoods> orderGoods;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderGoods {
        public String goodsImage;
        public String title;
        public String price;
        public String goodsId;
    }
}
