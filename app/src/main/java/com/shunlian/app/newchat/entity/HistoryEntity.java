package com.shunlian.app.newchat.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2017/9/25.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryEntity {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private String last_id;
        private String surplus;
        private String request_msg_id;
        private String request_uid;
        private String request_direction;
        private List<MsgInfo> message_list;

        public String getLast_id() {
            return last_id;
        }

        public void setLast_id(String last_id) {
            this.last_id = last_id;
        }

        public String getSurplus() {
            return surplus;
        }

        public void setSurplus(String surplus) {
            this.surplus = surplus;
        }

        public String getRequest_msg_id() {
            return request_msg_id;
        }

        public void setRequest_msg_id(String request_msg_id) {
            this.request_msg_id = request_msg_id;
        }

        public String getRequest_uid() {
            return request_uid;
        }

        public void setRequest_uid(String request_uid) {
            this.request_uid = request_uid;
        }

        public String getRequest_direction() {
            return request_direction;
        }

        public void setRequest_direction(String request_direction) {
            this.request_direction = request_direction;
        }

        public List<MsgInfo> getMessage_list() {
            return message_list;
        }

        public void setMessage_list(List<MsgInfo> message_list) {
            this.message_list = message_list;
        }
    }
}
