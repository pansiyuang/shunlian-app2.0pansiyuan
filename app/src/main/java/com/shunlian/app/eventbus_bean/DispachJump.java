package com.shunlian.app.eventbus_bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

/**
 * Created by Administrator on 2018/4/25.
 * 跳转分发
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DispachJump implements Parcelable {

    /**跳转到个人中心*/
    public final String personal = "personCenter";

    public String jumpType;//跳转类型

    public String[] items;//参数列表

    public DispachJump() {
    }

    @Override
    public String toString() {
        return "DispachJump{" +
                "personal='" + personal + '\'' +
                ", jumpType='" + jumpType + '\'' +
                ", items=" + Arrays.toString(items) +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.personal);
        dest.writeString(this.jumpType);
        dest.writeStringArray(this.items);
    }

    protected DispachJump(Parcel in) {
        this.jumpType = in.readString();
        this.items = in.createStringArray();
    }

    public static final Creator<DispachJump> CREATOR = new Creator<DispachJump>() {
        @Override
        public DispachJump createFromParcel(Parcel source) {
            return new DispachJump(source);
        }

        @Override
        public DispachJump[] newArray(int size) {
            return new DispachJump[size];
        }
    };
}
