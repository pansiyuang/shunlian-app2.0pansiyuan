package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2017/12/5.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceInfoEntity implements Serializable{
    public String limit_amount;
    public String balance;
    public String profit;
    public String quota;
    public String total;
    public String mobile;
    public String withdraw_limit;
    public String profit_help_url;
    public String rate;
    public String rate_name;
    public String account_number;
    public boolean havePayAccount;
    public boolean is_set_password;
}
