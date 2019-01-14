package com.shunlian.app.bean;

import java.util.List;

public class InviteLogUserEntity {
     public String  money;//红包金额

     public String  prize;//已领取的红包总额

     public String  code;//邀请码

    public List<UserList> list;

    public static class UserList{
         public String   date;

         public String   avatar;

         public String   nickname;

         public String   desc;
    }
}
