package com.shunlian.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by zhanghe on 2018/12/10.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UrlType implements Parcelable {
    public String type;
    public String item_id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.item_id);
    }

    public UrlType() {
    }

    protected UrlType(Parcel in) {
        this.type = in.readString();
        this.item_id = in.readString();
    }

    public static final Parcelable.Creator<UrlType> CREATOR = new Parcelable.Creator<UrlType>() {
        @Override
        public UrlType createFromParcel(Parcel source) {
            return new UrlType(source);
        }

        @Override
        public UrlType[] newArray(int size) {
            return new UrlType[size];
        }
    };
}
