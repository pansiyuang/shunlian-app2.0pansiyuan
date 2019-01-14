package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NoAddressOrderEntity {
    public int status;
    public String id;
    public String desc;
}
