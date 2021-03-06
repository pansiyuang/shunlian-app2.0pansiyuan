package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.PicAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.eventbus_bean.CommentEvent;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.CommentRank;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class CommentDetailAct extends BaseActivity {

    @BindView(R.id.mtv_append)
    MyTextView mtv_append;

    @BindView(R.id.mtv_good_comment)
    MyTextView mtv_good_comment;


    @BindView(R.id.comment_rank)
    CommentRank comment_rank;

    @BindView(R.id.mtv_append_comment_staus)
    MyTextView mtv_append_comment_staus;

    @BindView(R.id.mtv_content)
    MyTextView mtv_content;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.recy_view_append)
    RecyclerView recy_view_append;

    @BindView(R.id.mtv_append_time)
    MyTextView mtv_append_time;

    @BindView(R.id.mtv_append_content)
    MyTextView mtv_append_content;

    @BindView(R.id.mtv_goods_detail)
    MyTextView mtv_goods_detail;

    @BindView(R.id.mtv_price)
    MyTextView mtv_price;

    @BindView(R.id.mtv_comment_time)
    MyTextView mtv_comment_time;

    @BindView(R.id.mtv_zan_count)
    MyTextView mtv_zan_count;

    @BindView(R.id.mtv_zan_count1)
    MyTextView mtv_zan_count1;

    @BindView(R.id.mtv_reply_content)
    MyTextView mtv_reply_content;

    @BindView(R.id.miv_goods_pic)
    MyImageView miv_goods_pic;

    @BindView(R.id.mrl_reply_content)
    MyRelativeLayout mrl_reply_content;

    @BindView(R.id.civ_head)
    MyImageView civ_head;

    @BindView(R.id.mtv_nickname)
    MyTextView mtv_nickname;

    @BindView(R.id.llayout_time)
    MyLinearLayout llayout_time;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private CommentListEntity.Data data;

    public static void startAct(Context context, CommentListEntity.Data data,String nickname,String avatar) {
        Intent intent = new Intent(context, CommentDetailAct.class);
        intent.putExtra("data",data);
        intent.putExtra("nickname",nickname);
        intent.putExtra("avatar",avatar);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_comment_detail;
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @OnClick(R.id.rLayout_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.comment();
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        gone(llayout_time);
        EventBus.getDefault().register(this);

        Intent intent = getIntent();
        data = (CommentListEntity.Data) intent.getSerializableExtra("data");
        String nickname = intent.getStringExtra("nickname");
        String avatar = intent.getStringExtra("avatar");

        mtv_nickname.setText(nickname);
        GlideUtils.getInstance().loadCircleHeadImage(this,civ_head,avatar);

        mtv_append_comment_staus.setVisibility(View.GONE);

        String is_change = data.is_change;//是否可改为好评  0不能改好评    1可以改好评
        if ("1".equals(is_change)){
            mtv_good_comment.setVisibility(View.VISIBLE);
            GradientDrawable goodBackground = (GradientDrawable) mtv_good_comment.getBackground();
            goodBackground.setColor(Color.parseColor("#FD523C"));
            mtv_good_comment.setWHProportion(217,73);
        }else {
            mtv_good_comment.setVisibility(View.GONE);
        }

        String is_append = data.is_append;//追评状态  0不能追评   1可以追评 2已经追评
        if ("1".equals(is_append)){
            mtv_append.setVisibility(View.VISIBLE);
            GradientDrawable appendBackground = (GradientDrawable) mtv_append.getBackground();
            appendBackground.setColor(Color.parseColor("#FF8A18"));
            mtv_append.setWHProportion(217,73);
        }else {
            mtv_append.setVisibility(View.GONE);
        }


        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);
        recy_view.setNestedScrollingEnabled(false);
        recy_view.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(this,5),false));


        GridLayoutManager manager1 = new GridLayoutManager(this,3);
        recy_view_append.setLayoutManager(manager1);
        recy_view_append.setNestedScrollingEnabled(false);
        recy_view_append.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(this,5),false));

        setContent();
    }

    private void setContent() {
        if ("5".equals(data.star_level)) {//好评
            comment_rank.praiseRank();
        } else if ("3".equals(data.star_level)) {//中评
            comment_rank.middleRank();
        } else {//差评
            comment_rank.badRank();
        }

        String content = data.content;
        if (isEmpty(content)){//评价内容
            mtv_content.setVisibility(View.GONE);
        }else {
            mtv_content.setVisibility(View.VISIBLE);
            mtv_content.setText(content);
        }

        String append_note = data.append_note;
        if (isEmpty(append_note)){//用户1天前追评
            mtv_append_time.setVisibility(View.GONE);
        }else {
            mtv_append_time.setVisibility(View.VISIBLE);
            mtv_append_time.setText(append_note);
        }

        String append = data.append;
        if (isEmpty(append)){//追评内容
            mtv_append_content.setVisibility(View.GONE);
        }else {
            mtv_append_content.setVisibility(View.VISIBLE);
            mtv_append_content.setText(append);
        }

        GlideUtils.getInstance().loadImage(this,miv_goods_pic,data.thumb);
        mtv_goods_detail.setText(data.title);
        mtv_price.setText(getString(R.string.rmb)+data.price);
        mtv_comment_time.setText(data.add_time);
        mtv_zan_count.setText(data.praise_total);
        mtv_zan_count1.setText(data.praise_total);

        String reply = data.reply;
        if (isEmpty(reply)){
            mrl_reply_content.setVisibility(View.GONE);
        }else {
            mrl_reply_content.setVisibility(View.VISIBLE);
            mtv_reply_content.setText(reply);
        }

        final List<String> pics = data.pics;
        if (isEmpty(pics)){
            recy_view.setVisibility(View.GONE);
        }else {
            recy_view.setVisibility(View.VISIBLE);
            PicAdapter picAdapter = new PicAdapter(this,false,pics);
            recy_view.setAdapter(picAdapter);
            picAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList) pics;
                    bigImgEntity.index = position;
                    bigImgEntity.desc = data.content;
                    LookBigImgAct.startAct(CommentDetailAct.this, bigImgEntity);
                }
            });
        }


        final List<String> append_pics = data.append_pics;
        if (isEmpty(append_pics)){
            recy_view_append.setVisibility(View.GONE);
        }else {
            recy_view_append.setVisibility(View.VISIBLE);
            PicAdapter picAdapter = new PicAdapter(this,false,append_pics);
            recy_view_append.setAdapter(picAdapter);
            picAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList) append_pics;
                    bigImgEntity.index = position;
                    bigImgEntity.desc = data.append;
                    LookBigImgAct.startAct(CommentDetailAct.this, bigImgEntity);
                }
            });
        }
    }

    @OnClick(R.id.mrl_goods)
    public void jumpGoodsDetail(){
        GoodsDetailAct.startAct(this,data.goods_id);
    }

    @OnClick(R.id.mtv_append)
    public void appendComment(){
        ReleaseCommentEntity entity = new ReleaseCommentEntity(data.thumb,data.title,data.price,data.comment_id);
        entity.order = data.ordersn;
        CreatCommentActivity.startAct(this,entity,CreatCommentActivity.APPEND_COMMENT);
    }

    @OnClick(R.id.mtv_good_comment)
    public void goodComment(){
        ReleaseCommentEntity entity = new ReleaseCommentEntity(data.thumb,data.title,data.price,data.comment_id);
        CreatCommentActivity.startAct(this,entity,CreatCommentActivity.CHANGE_COMMENT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(CommentEvent event) {
        if (event.getStatus() == CommentEvent.SUCCESS_CHANGE_STATUS) {
            data.is_change = "0";
            mtv_good_comment.setVisibility(View.GONE);
        } else if (event.getStatus() == CommentEvent.SUCCESS_CHANGE_STATUS) {
            data.is_append = "0";
            mtv_append.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
