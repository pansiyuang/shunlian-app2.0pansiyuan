package com.shunlian.app.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Administrator on 2018/10/16.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotBlogsEntity {
    public Pager pager;
    public List<Blog> list;
    public List<Ad> ad_list;
    public List<String> expert_list;
    public List<RecomandFocus> recomand_focus_list;
    public DiscoveryInfo discovery_info;
    public MemberInfo member_info;
    public Detail detail;
    public BaseInfo base_info;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pager {
        public int page;
        public int page_size;
        public int count;
        public int total_page;
    }

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Ad {
        public String id;
        public String ad_img;
        public AdLink ad_link;
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AdLink {
        public String type;
        public String item_id;
        public String item_type;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecomandFocus {
        public String member_id;
        public String avatar;
        public String nickname;
        public String signature;
        public String follow_num;
        public String blog_num;
        public int focus_status;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DiscoveryInfo {
        public String fans_num;
        public String praise_num;
        public String down_num;
        public String focus_num;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MemberInfo {
        public String member_id;
        public String avatar;
        public String nickname;
        public String signature;
        public int is_focus;
        public int is_fans;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Detail {
        public String id;
        public String title;
        public String content;
        public String thumb;
        public String author_name;
        public String author_user;
        public String count;
        public int status;
        public String refer_member_num;
        public String refer_num;
        public String add_time;
        public String update_time;
        public List<DiscoverActivityEntity.Member> members;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BaseInfo {
        public String member_id;
        public int white_list;
        public String avatar;
        public String nickname;
    }
}
