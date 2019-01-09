package com.shunlian.app.bean;

public class ShowVoucherSuspension {
    public String suspensionShow;//1,//是否悬浮1是0否
    public Suspension suspension;
    public static class Suspension{
        public String prize;//优惠券金额

        public int finish;//剩余使用时间

        public  Link link;

        public static class Link{
            public String type;
            public String  item_id;
        }
    }
}
