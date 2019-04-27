package com.shunlian.app.bean;




/**
 * Created by Administrator on 2017/12/5.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetQrCardEntity {
    public String card_path;
    public String qrcode_path;
    public String invited;
}
