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
        public int expert;
        public String v_icon;
        public String expert_icon;
        public CommentEntity comment_list;

        public Blog() {
        }

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
            dest.writeInt(this.expert);
            dest.writeString(this.v_icon);
            dest.writeString(this.expert_icon);
            dest.writeParcelable(this.comment_list, flags);
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
            this.expert = in.readInt();
            this.v_icon = in.readString();
            this.expert_icon = in.readString();
            this.comment_list = in.readParcelable(CommentEntity.class.getClassLoader());
        }

        public static final Creator<Blog> CREATOR = new Creator<Blog>() {
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

    public BigImgEntity() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommentEntity implements Parcelable {
        public int total;
        public List<CommentItem> list;

        public CommentEntity() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.total);
            dest.writeList(this.list);
        }

        protected CommentEntity(Parcel in) {
            this.total = in.readInt();
            this.list = new ArrayList<CommentItem>();
            in.readList(this.list, CommentItem.class.getClassLoader());
        }

        public static final Creator<CommentEntity> CREATOR = new Creator<CommentEntity>() {
            @Override
            public CommentEntity createFromParcel(Parcel source) {
                return new CommentEntity(source);
            }

            @Override
            public CommentEntity[] newArray(int size) {
                return new CommentEntity[size];
            }
        };
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CommentItem {
        public String id;
        public String member_id;
        public String avatar;
        public String nickname;
        public String content;

        public CommentItem() {

        }

        public CommentItem(FindCommentListEntity.ItemComment itemComment) {
            this.avatar = itemComment.avatar;
            this.content = itemComment.content;
            this.id = itemComment.id;
            this.member_id = itemComment.member_id;
            this.nickname = itemComment.nickname;
        }
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

    protected BigImgEntity(Parcel in) {
        this.itemList = in.createStringArrayList();
        this.index = in.readInt();
        this.desc = in.readString();
        this.content = in.readString();
        this.id = in.readString();
        this.items = in.createStringArrayList();
        this.blog = in.readParcelable(Blog.class.getClassLoader());
    }

    public static final Creator<BigImgEntity> CREATOR = new Creator<BigImgEntity>() {
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
