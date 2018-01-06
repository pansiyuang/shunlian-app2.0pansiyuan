package com.shunlian.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/15.
 */

public class BigImgEntity implements Parcelable {
    public ArrayList<String> itemList;
    public int index;
    public String desc;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.itemList);
        dest.writeInt(this.index);
        dest.writeString(this.desc);
    }

    public BigImgEntity() {
    }

    protected BigImgEntity(Parcel in) {
        this.itemList = in.createStringArrayList();
        this.index = in.readInt();
        this.desc = in.readString();
    }

    public static final Parcelable.Creator<BigImgEntity> CREATOR = new Parcelable.Creator<BigImgEntity>() {
        @Override
        public BigImgEntity createFromParcel(Parcel source) {
            return new BigImgEntity(source);
        }

        @Override
        public BigImgEntity[] newArray(int size) {
            return new BigImgEntity[size];
        }
    };
}