package com.shunlian.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/24.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonalDataEntity {

    public String sex;
    public String tag;
    public String nickname;
    public String avatar;
    public String location;
    public String birth;
    public String signature;

    public ArrayList<TagList> list;



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TagList implements Parcelable {
        public String id;
        public String name;
        public String active;

        public TagList() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.active);
        }

        protected TagList(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.active = in.readString();
        }

        public static final Creator<TagList> CREATOR = new Creator<TagList>() {
            @Override
            public TagList createFromParcel(Parcel source) {
                return new TagList(source);
            }

            @Override
            public TagList[] newArray(int size) {
                return new TagList[size];
            }
        };
    }
}
