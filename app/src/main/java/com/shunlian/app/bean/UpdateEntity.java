package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateEntity implements Serializable{
    public String needUpdate;
    public String updateType;
    public String changeLog;
    public String localVersion;
    public String newVersion;
    public String updateUrl;
    public String fileMd5;
}
