package com.shunlian.app.ui.help;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.HelpArticleAdapter;
import com.shunlian.app.adapter.HelpQoneAdapter;
import com.shunlian.app.adapter.HelpQtwoAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.HelpcenterIndexEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PHelpOne;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MHorItemDecoration;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IHelpOneView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpOneAct extends BaseActivity implements View.OnClickListener, IHelpOneView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.miv_search)
    MyImageView miv_search;

    @BindView(R.id.rv_qOne)
    RecyclerView rv_qOne;

    @BindView(R.id.rv_article)
    RecyclerView rv_article;

    @BindView(R.id.rv_qTwo)
    RecyclerView rv_qTwo;

    @BindView(R.id.mllayout_dianhua)
    MyLinearLayout mllayout_dianhua;

    @BindView(R.id.mllayout_kefu)
    MyLinearLayout mllayout_kefu;
    @BindView(R.id.rl_more)
    RelativeLayout rl_more;
    @BindView(R.id.quick_actions)
    QuickActions quick_actions;
    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;
    private PHelpOne pHelpOne;
    private MessageCountManager messageCountManager;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, HelpOneAct.class);
//        intent.putExtra("storeId", storeId);//店铺id
        context.startActivity(intent);
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(this);
            if (messageCountManager.isLoad()) {
                String s = messageCountManager.setTextCount(tv_msg_count);
                if (quick_actions != null)
                    quick_actions.setMessageCount(s);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        super.onResume();
    }

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.help();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
//        storeId = getIntent().getStringExtra("storeId");
        pHelpOne = new PHelpOne(this, this);
        miv_search.setVisibility(View.VISIBLE);
        mtv_title.setText(getStringResouce(R.string.help_bangzhuzhongxin));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_help_one;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mllayout_kefu:
                pHelpOne.getUserId();
                break;
            case R.id.mllayout_dianhua:
                pHelpOne.getHelpPhone();
                break;
            case R.id.miv_search:
                SearchQuestionAct.startAct(this);
                break;
        }
    }

    public void initDialog(String phone) {
        PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener(phone, "呼叫", view -> {
            Intent intentServePhoneOne = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intentServePhoneOne);
            promptDialog.dismiss();
        }, "取消", view -> promptDialog.dismiss()).show();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mllayout_kefu.setOnClickListener(this);
        mllayout_dianhua.setOnClickListener(this);
        miv_search.setOnClickListener(this);
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void setApiData(final HelpcenterIndexEntity helpcenterIndexEntity) {
        rv_qOne.setLayoutManager(new GridLayoutManager(this, 2));
        HelpQoneAdapter helpQoneAdapter = new HelpQoneAdapter(this, false, helpcenterIndexEntity.questionCategory);
        rv_qOne.setAdapter(helpQoneAdapter);
        rv_qOne.setNestedScrollingEnabled(false);
        helpQoneAdapter.setOnItemClickListener((view, position) -> {
            HelpcenterIndexEntity.QuestionCategory questionCategory = helpcenterIndexEntity.questionCategory.get(position);
            HelpTwoAct.startAct(baseAct, questionCategory.id, questionCategory.name);
        });
        rv_article.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_article.addItemDecoration(new MHorItemDecoration(this, 5, 10, 10));
        HelpArticleAdapter helpArticleAdapter = new HelpArticleAdapter(this, false, helpcenterIndexEntity.articleCategory);
        rv_article.setAdapter(helpArticleAdapter);
        helpArticleAdapter.setOnItemClickListener((view, position) -> {
            HelpcenterIndexEntity.ArticleCategory questionCategory = helpcenterIndexEntity.articleCategory.get(position);
            HelpClassAct.startAct(baseAct, questionCategory.id);
        });
        rv_qTwo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        HelpQtwoAdapter helpQtwoAdapter = new HelpQtwoAdapter(this, false, helpcenterIndexEntity.questionCommon);
        rv_qTwo.setAdapter(helpQtwoAdapter);
        rv_qTwo.setNestedScrollingEnabled(false);
        rv_qTwo.addItemDecoration(new MVerticalItemDecoration(this, 0.5f, 0, 0, getColorResouce(R.color.value_EFEEEE)));
        helpQtwoAdapter.setOnItemClickListener((view, position) -> {
            HelpcenterIndexEntity.QuestionCommon common = helpcenterIndexEntity.questionCommon.get(position);
            HelpSolutionAct.startAct(baseAct, common.id);
        });
    }

    @Override
    public void setPhoneNum(String phoneNum) {
        initDialog(phoneNum);
    }

    @Override
    public void getUserId(String userId) {
        ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();
        chatMember.nickname = "官方客服";
        chatMember.m_user_id = userId;
        chatMember.type = "1";
        ChatManager.getInstance(this).init().MemberChat2Platform(chatMember);
    }
}
