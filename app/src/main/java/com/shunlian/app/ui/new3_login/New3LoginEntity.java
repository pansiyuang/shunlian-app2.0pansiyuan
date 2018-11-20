package com.shunlian.app.ui.new3_login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zhanghe on 2018/11/20.
 * 第三版登录数据类
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class New3LoginEntity {

    /**
     * 为1时，需要输入图形验证码
     */
    public String vcode_status;

}
