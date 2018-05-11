package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.CustomViewPager;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/10.
 */

public class FoundMsgActivity extends BaseActivity {

    public static final String[] tabTitle = new String[]{"头条", "小店消息"};

    @BindView(R.id.view_pager)
    CustomViewPager view_pager;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.miv_title_right)
    MyImageView miv_title_right;

    @BindView(R.id.rl_topic)
    RelativeLayout rl_topic;

    @BindView(R.id.tv_topic_title)
    TextView tv_topic_title;

    @BindView(R.id.tv_topic_count)
    TextView tv_topic_count;

    @BindView(R.id.line_topic)
    View line_topic;

    @BindView(R.id.rl_comment)
    RelativeLayout rl_comment;

    @BindView(R.id.tv_comment_title)
    TextView tv_comment_title;

    @BindView(R.id.tv_comment_count)
    TextView tv_comment_count;

    @BindView(R.id.line_comment)
    View line_comment;

    @BindView(R.id.line_title)
    View line_title;

    private static List<BaseFragment> mFrags = new ArrayList<>();
    private CommonLazyPagerAdapter mPagerAdapter;
    private int topicCount, commentCount;
    private MessageCountManager messageCountManager;
    private MessageListFragment messageListFragment;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, FoundMsgActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_found_msg;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.message));
        miv_title_right.setVisibility(View.VISIBLE);
        miv_title_right.setImageResource(R.mipmap.icon_found_sousuo);
        line_title.setVisibility(View.GONE);


        if (messageCountManager.isLoad()) {
            topicCount = messageCountManager.getDiscover_topic_msg();
            commentCount = messageCountManager.getDiscover_comment_msg();
        }

        messageListFragment = MessageListFragment.getInstance();
        mFrags.add(messageListFragment);
        mFrags.add(CommentFragment.getInstance());

        mPagerAdapter = new CommonLazyPagerAdapter(getSupportFragmentManager(), mFrags, tabTitle);
        view_pager.setAdapter(mPagerAdapter);
        view_pager.setOffscreenPageLimit(2);

        topicClick();
    }

    private void topicClick() {
        tv_topic_title.setSelected(true);
        tv_comment_title.setSelected(false);

        tv_topic_count.setVisibility(View.GONE);
        tv_comment_count.setVisibility(View.VISIBLE);

        if (isEmpty(Common.formatBadgeNumber(commentCount))) {
            tv_comment_count.setVisibility(View.GONE);
        } else {
            tv_comment_count.setVisibility(View.VISIBLE);
            tv_comment_count.setText(Common.formatBadgeNumber(commentCount));
        }

        line_topic.setBackgroundColor(getColorResouce(R.color.pink_color));
        line_comment.setBackgroundColor(getColorResouce(R.color.light_gray_two));
    }

    private void commentClick() {
        tv_topic_title.setSelected(false);
        tv_comment_title.setSelected(true);

        tv_topic_count.setVisibility(View.VISIBLE);
        tv_comment_count.setVisibility(View.GONE);

        if (isEmpty(Common.formatBadgeNumber(topicCount))) {
            tv_topic_count.setVisibility(View.GONE);
        } else {
            tv_topic_count.setVisibility(View.VISIBLE);
            tv_topic_count.setText(Common.formatBadgeNumber(topicCount));
        }

        line_topic.setBackgroundColor(getColorResouce(R.color.light_gray_two));
        line_comment.setBackgroundColor(getColorResouce(R.color.pink_color));
    }

}
