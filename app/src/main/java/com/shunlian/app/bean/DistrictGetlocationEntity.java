package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6 0006.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DistrictGetlocationEntity {
    public List<String> district_ids;
    public List<String> district_names;

    @Override
    public String toString() {
        return "DistrictGetlocationEntity{" +
                "district_ids=" + district_ids +
                ", district_names=" + district_names +
                '}';
    }
}
