package com.shunlian.app.ui.help;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.HelpClasAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.HelpClassEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PHelpTwo;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IHelpTwoView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpClassAct extends BaseActivity implements View.OnClickListener, IHelpTwoView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.rv_qCate)
    RecyclerView rv_qCate;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.bottom_layout)
    View bottom_layout;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private MessageCountManager messageCountManager;

    private PHelpTwo pHelpTwo;
    private HelpClasAdapter helpClasAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String cate_id="";
    private PromptDialog promptDialog;


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
        mtv_title.setText("");
//        storeId = getIntent().getStringExtra("storeId");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("cate_id")))
            cate_id=getIntent().getStringExtra("cate_id");
        pHelpTwo = new PHelpTwo(this, this);
        pHelpTwo.getHelpPhone();
//        pHelpTwo.resetBabys("3");
        pHelpTwo.resetBabys(cate_id);

        bottom_layout.setVisibility(View.GONE);
    }

    public static void startAct(Context context,String cate_id) {
        Intent intent = new Intent(context, HelpClassAct.class);
        intent.putExtra("cate_id", cate_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_help_two;
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mllayout_dianhua:
                if (promptDialog == null) {
                    initDialog();
                } else {
                    promptDialog.show();
                }
                break;
            case R.id.mllayout_kefu:
                pHelpTwo.getUserId();
                break;
        }
    }*/

    @Override
    protected void initListener() {
        super.initListener();
        /*mllayout_kefu.setOnClickListener(this);
        mllayout_dianhua.setOnClickListener(this);
        rv_qCate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pHelpTwo != null) {
                            pHelpTwo.refreshBabys(cate_id);
//                            pHelpTwo.refreshBabys("3");
                        }
                    }
                }
            }
        });*/
    }

    public void initDialog() {
        promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener(Constant.HELP_PHONE, "呼叫", view -> {
            Intent intentServePhoneOne = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.HELP_PHONE));
            startActivity(intentServePhoneOne);
            promptDialog.dismiss();
        }, "取消", view -> promptDialog.dismiss()).show();
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setClass(HelpClassEntity helpClassEntity, final List<HelpClassEntity.Article> articles) {
        mtv_title.setText(helpClassEntity.cate);
        if (helpClasAdapter == null) {
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            helpClasAdapter = new HelpClasAdapter(this, false, articles);
            helpClasAdapter.setOnItemClickListener((view, position) -> {
//                    H5Act.startAct(getBaseContext(),articles.get(position).h5_link,H5Act.MODE_SONIC);
                ClassDetailAct.startAct(getBaseContext(),
                        articles.get(position).h5_link,
                        articles.get(position).id,
                        H5Act.MODE_SONIC);
            });
            rv_qCate.setLayoutManager(linearLayoutManager);
            rv_qCate.addItemDecoration(new MVerticalItemDecoration(this, 1, 0, 0, getColorResouce(R.color.value_EFEEEE)));
            rv_qCate.setAdapter(helpClasAdapter);
            helpClasAdapter.setPageLoading(Integer.parseInt(helpClassEntity.page), Integer.parseInt(helpClassEntity.total_page));

        } else {
            helpClasAdapter.notifyDataSetChanged();
        }
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
        Constant.HELP_PHONE = phoneNum;
    }

    @Override
    public void setCateOne(final HelpcenterQuestionEntity helpcenterQuestionEntity) {
      }

    @Override
    public void setCateTwo(HelpcenterQuestionEntity helpcenterQuestionEntity, final List<HelpcenterQuestionEntity.Question> questions) {

    }
}
