package com.shunlian.app.ui.help;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IHelpSolutionView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

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
    @BindView(R.id.mlLayout_bottom)
    MyLinearLayout mlLayout_bottom;
    @BindView(R.id.mwv_h5)
    X5WebView mwv_h5;
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
    public static void startAct(Context context, String id,boolean isShow) {
        Intent intent = new Intent(context, HelpSolutionAct.class);
        intent.putExtra("id", id);
        intent.putExtra("isShow", isShow);
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
        if (getIntent().getBooleanExtra("isShow",false)){
            mlLayout_bottom.setVisibility(View.VISIBLE);
        }else {
            mlLayout_bottom.setVisibility(View.GONE);
        }

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

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    public void setApiData(final HelpcenterSolutionEntity solution) {
        mtv_question.setText(solution.question);
        mtv_answer.setText(solution.answer);

        if (isEmpty(solution.answer)){
            mwv_h5.setVisibility(View.GONE);
        }else {
            mwv_h5.setVisibility(View.VISIBLE);
            mwv_h5.getSettings().setDefaultTextEncodingName("UTF-8");
            mwv_h5.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
            //web_view.loadData(map.get("NEWS_CONTENT"), "text/html", "UTF-8") ; 有时会遇到乱码问题 具体好像与sdk有关系
            mwv_h5.getSettings().setAppCacheMaxSize(Long.MAX_VALUE);
            mwv_h5.getSettings().setAppCachePath(Constant.CACHE_PATH_EXTERNAL);
//        h5_mwb.removeJavascriptInterface("searchBoxJavaBridge_");
//        h5_mwb.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, getIntent()), "sonic");
            mwv_h5.getSettings().setAppCacheEnabled(true);
            mwv_h5.getSettings().setAllowFileAccess(true);
            //开启DOM缓存，关闭的话H5自身的一些操作是无效的
            mwv_h5.getSettings().setDomStorageEnabled(true);
            mwv_h5.getSettings().setAllowContentAccess(true);
            mwv_h5.getSettings().setDatabaseEnabled(true);
            mwv_h5.getSettings().setSavePassword(false);
            mwv_h5.getSettings().setSaveFormData(false);
            mwv_h5.getSettings().setUseWideViewPort(true);
            mwv_h5.getSettings().setLoadWithOverviewMode(true);

            mwv_h5.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            mwv_h5.getSettings().setSupportZoom(false);
            mwv_h5.getSettings().setBuiltInZoomControls(false);
            mwv_h5.getSettings().setSupportMultipleWindows(false);
            mwv_h5.getSettings().setGeolocationEnabled(true);
            mwv_h5.getSettings().setDatabasePath(this.getDir("databases", 0).getPath());
            mwv_h5.getSettings().setGeolocationDatabasePath(this.getDir("geolocation", 0)
                    .getPath());
//            mwv_h5.setWebViewClient(new WebViewClient());
            // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
            mwv_h5.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            String contentHtml = "<header><meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no'></header>"+
                    solution.answer;
            mwv_h5.loadDataWithBaseURL(null, contentHtml, "text/html", "UTF-8", null);
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
                HelpSolutionAct.startAct(baseAct, question.id);
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
        Common.staticToasts(baseAct, getStringResouce(R.string.help_xiexiefankui), R.mipmap.icon_common_duihao);
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
