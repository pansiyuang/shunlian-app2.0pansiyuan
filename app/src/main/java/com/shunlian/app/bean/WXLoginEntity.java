package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2017/10/24.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WXLoginEntity {


    /**
     * id : 2
     * unique_sign : oofPYwXXvrrGuQ6JzVogOCcrs91A
     * member_id : 0
     */

    public String id;
    public String unique_sign;
    public int member_id;

    @Override
    public String toString() {
        return "WXLoginEntity{" +
                "id='" + id + '\'' +
                ", unique_sign='" + unique_sign + '\'' +
                ", member_id=" + member_id +
                '}';
    }
}
