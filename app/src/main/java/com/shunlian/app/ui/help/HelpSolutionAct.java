package com.shunlian.app.ui.help;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.HelpSolutionAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.HelpcenterSolutionEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PHelpSolution;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IHelpSolutionView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.MyWebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpSolutionAct extends BaseActivity implements View.OnClickListener, IHelpSolutionView, MessageCountManager.OnGetMessageListener {
    private static final int TEXT_TOTAL = 140;//文字总数
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;
    @BindView(R.id.mtv_question)
    MyTextView mtv_question;
    @BindView(R.id.mtv_answer)
    MyTextView mtv_answer;
    @BindView(R.id.mtv_shi)
    MyTextView mtv_shi;
    @BindView(R.id.mtv_fou)
    MyTextView mtv_fou;
    @BindView(R.id.miv_shi)
    MyImageView miv_shi;
    @BindView(R.id.miv_fou)
    MyImageView miv_fou;
    @BindView(R.id.rv_about)
    RecyclerView rv_about;
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
    @BindView(R.id.mwv_h5)
    MyWebView mwv_h5;
    private PHelpSolution pHelpSolution;
    private Dialog dialog_feedback;
    private boolean isChosen = false;
    private ShareInfoParam shareInfoParam;
    private MessageCountManager messageCountManager;


    public static void startAct(Context context, String id) {
        Intent intent = new Intent(context, HelpSolutionAct.class);
        intent.putExtra("id", id);
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
        shareInfoParam.isCopyTitle = true;
        quick_actions.shareInfo(shareInfoParam);
        quick_actions.shareHelp();
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
        mtv_title.setText(getStringResouce(R.string.help_jiejuefangan));
//        storeId = getIntent().getStringExtra("storeId");
        pHelpSolution = new PHelpSolution(this, getIntent().getStringExtra("id"), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_help_solution;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mllayout_dianhua:
                pHelpSolution.getHelpPhone();
                break;
            case R.id.mllayout_kefu:
                pHelpSolution.getUserId();
                break;
            case R.id.mtv_fou:
            case R.id.miv_fou:
                if (!isChosen)
                    pHelpSolution.isSolved("2");
                break;
            case R.id.miv_shi:
            case R.id.mtv_shi:
                if (!isChosen)
                    pHelpSolution.isSolved("1");
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mllayout_kefu.setOnClickListener(this);
        mllayout_dianhua.setOnClickListener(this);
        mtv_fou.setOnClickListener(this);
        miv_shi.setOnClickListener(this);
        miv_fou.setOnClickListener(this);
        mtv_shi.setOnClickListener(this);
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void setApiData(final HelpcenterSolutionEntity solution) {
        mtv_question.setText(solution.question);
        mtv_answer.setText(solution.answer);

        if (isEmpty(solution.answer)){
            mwv_h5.setVisibility(View.GONE);
        }else {
            mwv_h5.setVisibility(View.VISIBLE);
            mwv_h5.getSettings().setDefaultTextEncodingName("UTF-8");
            //web_view.loadData(map.get("NEWS_CONTENT"), "text/html", "UTF-8") ; 有时会遇到乱码问题 具体好像与sdk有关系
            mwv_h5.loadDataWithBaseURL(null, solution.answer, "text/html", "UTF-8", null);
        }
        rv_about.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        HelpSolutionAdapter helpQtwoAdapter = new HelpSolutionAdapter(this, false, solution.about);
        rv_about.setAdapter(helpQtwoAdapter);
        rv_about.setNestedScrollingEnabled(false);
        rv_about.addItemDecoration(new MVerticalItemDecoration(this, 0.5f, 0, 0, getColorResouce(R.color.value_EFEEEE)));
        helpQtwoAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HelpcenterSolutionEntity.Question question = solution.about.get(position);
                HelpSolutionAct.startAct(getBaseContext(), question.id);
                finish();
            }
        });
        if (shareInfoParam == null)
            shareInfoParam = new ShareInfoParam();
        shareInfoParam.shareLink = solution.share_url;
        shareInfoParam.title = solution.question;
        shareInfoParam.desc = solution.answer;
    }

    public void initDialogs(String phone) {
        PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener(phone, "呼叫", view -> {
            Intent intentServePhoneOne = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intentServePhoneOne);
            promptDialog.dismiss();
        }, "取消", view -> promptDialog.dismiss()).show();
    }

    public void initDialog() {
        dialog_feedback = new Dialog(this, R.style.Mydialog);
        dialog_feedback.setContentView(R.layout.dialog_help_feedback);
        MyTextView mtv_sure = (MyTextView) dialog_feedback.findViewById(R.id.mtv_sure);
        final EditText et_moto = (EditText) dialog_feedback.findViewById(R.id.et_moto);
        final MyTextView mtv_length = (MyTextView) dialog_feedback.findViewById(R.id.mtv_length);
        final MyTextView mtv_error = (MyTextView) dialog_feedback.findViewById(R.id.mtv_error);
        mtv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_moto.length() < 5) {
                    mtv_error.setVisibility(View.VISIBLE);
                    mtv_error.setText(getStringResouce(R.string.help_qingzhishao));
                } else {
                    pHelpSolution.submitFeedback(et_moto.getText().toString());
                }
            }
        });
        et_moto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mtv_length.setVisibility(View.VISIBLE);
                } else {
                    mtv_length.setVisibility(View.GONE);
                }

                if (s.length() > TEXT_TOTAL) {
                    mtv_error.setVisibility(View.VISIBLE);
                    mtv_error.setText(getStringResouce(R.string.help_zishuchaoguo));
                    et_moto.setText(s.subSequence(0, TEXT_TOTAL));
                } else {
                    mtv_length.setText(s.length() + "/" + TEXT_TOTAL);
                    if (s.length() < TEXT_TOTAL) {
                        mtv_error.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > TEXT_TOTAL) {
                    et_moto.setSelection(TEXT_TOTAL);
                }
                  /*else{
                    et_moto.setSelection(s.length());
                 }*/
            }
        });
    }

    @Override
    public void initFeedback(boolean isSolved) {
        isChosen = true;
        if (isSolved) {
            mtv_shi.setTextColor(getColorResouce(R.color.pink_color));
            miv_shi.setImageResource(R.mipmap.img_bangzhu_shi_h);
        } else {
            mtv_fou.setTextColor(getColorResouce(R.color.pink_color));
            miv_fou.setImageResource(R.mipmap.img_bangzhu_fou_h);
            if (dialog_feedback == null) {
                initDialog();
            }
            dialog_feedback.show();
        }
    }

    @Override
    public void callFeedback() {
        dialog_feedback.dismiss();
        Common.staticToasts(getBaseContext(), getStringResouce(R.string.help_xiexiefankui), R.mipmap.icon_common_duihao);
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
        initDialogs(phoneNum);
    }

}
