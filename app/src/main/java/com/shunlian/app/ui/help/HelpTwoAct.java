package com.shunlian.app.ui.help;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.HelpQCateAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.HelpClassEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PHelpTwo;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IHelpTwoView;
import com.shunlian.app.widget.MarqueeTextView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpTwoAct extends BaseActivity implements View.OnClickListener, IHelpTwoView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mar_title)
    MarqueeTextView mar_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.rv_qCate)
    RecyclerView rv_qCate;

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
    private PHelpTwo pHelpTwo;
    private LinearLayoutManager linearLayoutManager;
    private String twoId = "", oneId = "", title = "";
    private boolean twoFlag = false;
    private HelpQCateAdapter helpQCateAdapter;
    private MessageCountManager messageCountManager;

    public static void startAct(Context context, String id, String title) {
        Intent intent = new Intent(context, HelpTwoAct.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        pHelpTwo = new PHelpTwo(this, this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("title")))
            title = getIntent().getStringExtra("title");
        setTitle(title);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("id")))
            oneId = getIntent().getStringExtra("id");
        pHelpTwo.getCateOne(oneId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_help_two;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miv_close:
                if (twoFlag) {
                    pHelpTwo.getCateOne(oneId);
                    setTitle(title);
                    twoFlag = false;
                } else {
                    finish();
                }
                break;
            case R.id.mllayout_dianhua:
                pHelpTwo.getHelpPhone();
                break;
            case R.id.mllayout_kefu:
                pHelpTwo.getUserId();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mllayout_kefu.setOnClickListener(this);
        mllayout_dianhua.setOnClickListener(this);
        miv_close.setOnClickListener(this);
        rv_qCate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pHelpTwo != null) {
                            pHelpTwo.refreshBaby(twoId);
                        }
                    }
                }
            }
        });
    }

    public void initDialog(String phone) {
        PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener(phone, "呼叫", view -> {
            Intent intentServePhoneOne = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intentServePhoneOne);
            promptDialog.dismiss();
        }, "取消", view -> promptDialog.dismiss()).show();
    }


    private void setTitle(String title) {
        if (!TextUtils.isEmpty(title) && title.length() > 8) {
            mar_title.setVisibility(View.VISIBLE);
            mtv_title.setVisibility(View.GONE);
            mar_title.setText(title);
        } else {
            mar_title.setVisibility(View.GONE);
            mtv_title.setVisibility(View.VISIBLE);
            mtv_title.setText(title);
        }
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void setCateOne(final HelpcenterQuestionEntity helpcenterQuestionEntity) {
        rv_qCate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        HelpQCateAdapter helpQCateAdapter = new HelpQCateAdapter(this, false, false, helpcenterQuestionEntity.list);
        rv_qCate.setAdapter(helpQCateAdapter);
        helpQCateAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                twoId = helpcenterQuestionEntity.list.get(position).id;
                pHelpTwo.resetBaby(twoId);
                setTitle(helpcenterQuestionEntity.list.get(position).name);
                twoFlag = true;
            }
        });
        rv_qCate.addItemDecoration(new MVerticalItemDecoration(this, 1, 0, 0, getColorResouce(R.color.value_EFEEEE)));
    }

    @Override
    public void setCateTwo(HelpcenterQuestionEntity helpcenterQuestionEntity, final List<HelpcenterQuestionEntity.Question> questions) {
        if (helpQCateAdapter == null) {
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            helpQCateAdapter = new HelpQCateAdapter(this, true, false, questions);
            helpQCateAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    HelpSolutionAct.startAct(getBaseContext(), questions.get(position).id);
                }
            });
        } else {
            helpQCateAdapter.notifyDataSetChanged();
        }
        rv_qCate.setLayoutManager(linearLayoutManager);
        rv_qCate.addItemDecoration(new MVerticalItemDecoration(this, 1, 0, 0, getColorResouce(R.color.value_EFEEEE)));
        rv_qCate.setAdapter(helpQCateAdapter);
        helpQCateAdapter.setPageLoading(Integer.parseInt(helpcenterQuestionEntity.page), Integer.parseInt(helpcenterQuestionEntity.total_page));
    }

    @Override
    public void setClass(HelpClassEntity helpClassEntity, List<HelpClassEntity.Article> articles) {

    }

    @Override
    public void getUserId(String userId) {
        ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();
        chatMember.nickname = "官方客服";
        chatMember.m_user_id = userId;
        chatMember.type = "1";
        ChatManager.getInstance(this).init().MemberChat2Platform(chatMember);
    }

    @Override
    public void setPhoneNum(String phoneNum) {
        initDialog(phoneNum);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && twoFlag) {
            pHelpTwo.getCateOne(oneId);
            twoFlag = false;
            setTitle(title);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
