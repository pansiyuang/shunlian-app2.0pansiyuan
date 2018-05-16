package com.shunlian.app.ui.help;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.HelpQCateAdapter;
import com.shunlian.app.bean.HelpClassEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.presenter.PHelpTwo;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IHelpTwoView;
import com.shunlian.app.widget.MarqueeTextView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

public class HelpTwoAct extends BaseActivity implements View.OnClickListener, IHelpTwoView {
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

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.rl_more)
    MyRelativeLayout rl_more;

    private PHelpTwo pHelpTwo;
    private LinearLayoutManager linearLayoutManager;
    private String twoId = "", oneId = "", title = "";
    private boolean twoFlag = false;
    private PromptDialog promptDialog;
    private HelpQCateAdapter helpQCateAdapter;

    public static void startAct(Context context, String id, String title) {
        Intent intent = new Intent(context, HelpTwoAct.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
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
                if (promptDialog == null) {
                    initDialog();
                } else {
                    promptDialog.show();
                }
                break;
            case R.id.mllayout_kefu:
                pHelpTwo.getUserId();
                break;
            case R.id.rl_more:
                quick_actions.setVisibility(View.VISIBLE);
                quick_actions.help();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mllayout_kefu.setOnClickListener(this);
        mllayout_dianhua.setOnClickListener(this);
        rl_more.setOnClickListener(this);
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

    @Override
    protected void initData() {
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

    public void initDialog() {
        promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener(Constant.HELP_PHONE, "呼叫", view -> {
            Intent intentServePhoneOne = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Constant.HELP_PHONE));
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
                    HelpSolutionAct.startAct(getBaseContext(),questions.get(position).id);
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
        chatMember.nickname = "在线客服";
        chatMember.m_user_id = userId;
        chatMember.type = "1";
        ChatManager.getInstance(this).init().MemberChat2Platform(chatMember);
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

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }
}
