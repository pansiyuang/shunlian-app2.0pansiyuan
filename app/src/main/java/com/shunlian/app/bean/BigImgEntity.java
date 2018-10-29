package com.shunlian.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class BigImgEntity implements Parcelable {
    public ArrayList<String> itemList;
    public int index;
    public String desc;
    public String content;
    public String id;
    public ArrayList<String> items;
    public Blog blog;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Blog implements Parcelable {
        public String avatar;
        public String nickname;
        public int add_v;
        public String id;
        public String member_id;
        public int type;
        public String text;
        public List<String> pics;
        public String video;
        public String video_thumb;
        public String activity_id;
        public String activity_title;
        public String place;
        public List<GoodsDeatilEntity.Goods> related_goods;
        public int praise_num;
        public int down_num;
        public int fans_num;
        public int share_num;
        public int is_praise;//点赞 1：已点赞
        public int is_focus;//关注1：已关注
        public String time_desc;
        public int total_share_num;
        public int is_self;
        public int is_favo;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.avatar);
            dest.writeString(this.nickname);
            dest.writeInt(this.add_v);
            dest.writeString(this.id);
            dest.writeString(this.member_id);
            dest.writeInt(this.type);
            dest.writeString(this.text);
            dest.writeStringList(this.pics);
            dest.writeString(this.video);
            dest.writeString(this.video_thumb);
            dest.writeString(this.activity_id);
            dest.writeString(this.activity_title);
            dest.writeString(this.place);
            dest.writeTypedList(this.related_goods);
            dest.writeInt(this.praise_num);
            dest.writeInt(this.down_num);
            dest.writeInt(this.fans_num);
            dest.writeInt(this.share_num);
            dest.writeInt(this.is_praise);
            dest.writeInt(this.is_focus);
            dest.writeString(this.time_desc);
            dest.writeInt(this.total_share_num);
            dest.writeInt(this.is_self);
            dest.writeInt(this.is_favo);
        }

        public Blog() {
        }

        protected Blog(Parcel in) {
            this.avatar = in.readString();
            this.nickname = in.readString();
            this.add_v = in.readInt();
            this.id = in.readString();
            this.member_id = in.readString();
            this.type = in.readInt();
            this.text = in.readString();
            this.pics = in.createStringArrayList();
            this.video = in.readString();
            this.video_thumb = in.readString();
            this.activity_id = in.readString();
            this.activity_title = in.readString();
            this.place = in.readString();
            this.related_goods = in.createTypedArrayList(GoodsDeatilEntity.Goods.CREATOR);
            this.praise_num = in.readInt();
            this.down_num = in.readInt();
            this.fans_num = in.readInt();
            this.share_num = in.readInt();
            this.is_praise = in.readInt();
            this.is_focus = in.readInt();
            this.time_desc = in.readString();
            this.total_share_num = in.readInt();
            this.is_self = in.readInt();
            this.is_favo = in.readInt();
        }

        public static final Parcelable.Creator<Blog> CREATOR = new Parcelable.Creator<Blog>() {
            @Override
            public Blog createFromParcel(Parcel source) {
                return new Blog(source);
            }

            @Override
            public Blog[] newArray(int size) {
                return new Blog[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.itemList);
        dest.writeInt(this.index);
        dest.writeString(this.desc);
        dest.writeString(this.content);
        dest.writeString(this.id);
        dest.writeStringList(this.items);
        dest.writeParcelable(this.blog, flags);
    }

    public BigImgEntity() {
    }

    protected BigImgEntity(Parcel in) {
        this.itemList = in.createStringArrayList();
        this.index = in.readInt();
        this.desc = in.readString();
        this.content = in.readString();
        this.id = in.readString();
        this.items = in.createStringArrayList();
        this.blog = in.readParcelable(Blog.class.getClassLoader());
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
