package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2016/4/29 0029.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseEntity<T> {
    public Integer code;
    public String message;

    public T data;

    @Override
    public String toString() {
        return "BaseEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
