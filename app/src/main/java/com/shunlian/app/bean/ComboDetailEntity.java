package com.shunlian.app.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ComboDetailEntity {

    public String combo_id;
    public String combo_thumb;
    public String combo_title;
    public String combo_price;
    public String start_time;
    public String end_time;
    public String start_unixtime;
    public String end_unixtime;
    public String max_combo_price;
    public String old_combo_price;
    public String max_old_combo_price;

    public List<GoodsDeatilEntity.Goods>  goods;

    public List<OthersCombo> others_combo;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OthersCombo{
        public String combo_id;
        public String combo_thumb;
        public String combo_title;
        public String combo_price;
        public String start_time;
        public String end_time;
        public String start_unixtime;
        public String end_unixtime;
        public String max_combo_price;
        public String old_combo_price;
        public String max_old_combo_price;
        public List<GoodsDeatilEntity.Goods>  goods;
    }


    @Override
    public String toString() {
        return "ComboDetailEntity{" +
                "combo_id='" + combo_id + '\'' +
                ", combo_thumb='" + combo_thumb + '\'' +
                ", combo_title='" + combo_title + '\'' +
                ", combo_price='" + combo_price + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", start_unixtime='" + start_unixtime + '\'' +
                ", end_unixtime='" + end_unixtime + '\'' +
                ", max_combo_price='" + max_combo_price + '\'' +
                ", old_combo_price='" + old_combo_price + '\'' +
                ", max_old_combo_price='" + max_old_combo_price + '\'' +
                ", goods=" + goods +
                '}';
    }


}
