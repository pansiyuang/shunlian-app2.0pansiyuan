package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderLogisticsEntity {
    public String ordersn;
    public String logistic_code;
    public String shipper_name;
    public List<Trace> traces;
    public String state;
    public String lastupdate;
    public String followed;
    public String shipper_code;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Trace {
        public String AcceptTime;
        public String AcceptStation;
    }
}
