package com.shunlian.app.ui.help;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.HelpArticleAdapter;
import com.shunlian.app.adapter.HelpQoneAdapter;
import com.shunlian.app.adapter.HelpQtwoAdapter;
import com.shunlian.app.bean.HelpcenterIndexEntity;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.ui.ChatActivity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.presenter.PHelpOne;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.MHorItemDecoration;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IHelpOneView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class HelpOneAct extends BaseActivity implements View.OnClickListener, IHelpOneView {
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
    MyRelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private PHelpOne pHelpOne;
    private PromptDialog promptDialog;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, HelpOneAct.class);
//        intent.putExtra("storeId", storeId);//店铺id
        context.startActivity(intent);
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
                if (promptDialog == null) {
                    initDialog();
                } else {
                    promptDialog.show();
                }
                break;
            case R.id.rl_more:
                quick_actions.setVisibility(View.VISIBLE);
                quick_actions.help();
                break;
            case R.id.miv_search:
                SearchQuestionAct.startAct(this);
                break;
        }
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
    protected void initListener() {
        super.initListener();
        mllayout_kefu.setOnClickListener(this);
        mllayout_dianhua.setOnClickListener(this);
        miv_search.setOnClickListener(this);
        rl_more.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
//        storeId = getIntent().getStringExtra("storeId");
        pHelpOne = new PHelpOne(this, this);
        pHelpOne.getHelpPhone();
        miv_search.setVisibility(View.VISIBLE);
        mtv_title.setText(getStringResouce(R.string.help_bangzhuzhongxin));
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
        helpQoneAdapter.setOnItemClickListener((view, position) -> {
            HelpcenterIndexEntity.QuestionCategory questionCategory = helpcenterIndexEntity.questionCategory.get(position);
            HelpTwoAct.startAct(getBaseContext(), questionCategory.id, questionCategory.name);
        });
        rv_article.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_article.addItemDecoration(new MHorItemDecoration(this, 5, 10, 10));
        HelpArticleAdapter helpArticleAdapter = new HelpArticleAdapter(this, false, helpcenterIndexEntity.articleCategory);
        rv_article.setAdapter(helpArticleAdapter);
        helpArticleAdapter.setOnItemClickListener((view, position) -> {
            HelpcenterIndexEntity.ArticleCategory questionCategory = helpcenterIndexEntity.articleCategory.get(position);
            HelpClassAct.startAct(getBaseContext(), questionCategory.id);
        });
        rv_qTwo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        HelpQtwoAdapter helpQtwoAdapter = new HelpQtwoAdapter(this, false, helpcenterIndexEntity.questionCommon);
        rv_qTwo.setAdapter(helpQtwoAdapter);
        rv_qTwo.addItemDecoration(new MVerticalItemDecoration(this, 0.5f, 0, 0, getColorResouce(R.color.value_EFEEEE)));
        helpQtwoAdapter.setOnItemClickListener((view, position) -> {
            HelpcenterIndexEntity.QuestionCommon common = helpcenterIndexEntity.questionCommon.get(position);
            HelpTwoAct.startAct(getBaseContext(), common.id, common.title);
        });
    }

    @Override
    public void setPhoneNum(String phoneNum) {
        Constant.HELP_PHONE = phoneNum;
    }

    @Override
    public void getUserId(String userId) {
        ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();
        chatMember.nickname = "在线客服";
        chatMember.m_user_id = userId;
        chatMember.type = "1";
        ChatManager.getInstance(this).init().MemberChat2Platform(chatMember);
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }
}
