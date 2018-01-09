package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by gjz on 9/3/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact implements Serializable {
    private String index;
    private String name;


    public Contact(String index, String name) {
        this.index = index;
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

}
