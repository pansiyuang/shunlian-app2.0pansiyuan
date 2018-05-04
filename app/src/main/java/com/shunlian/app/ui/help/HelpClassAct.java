package com.shunlian.app.ui.help;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.HelpClasAdapter;
import com.shunlian.app.bean.HelpClassEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;
import com.shunlian.app.presenter.PHelpTwo;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IHelpTwoView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

public class HelpClassAct extends BaseActivity implements View.OnClickListener, IHelpTwoView {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.rv_qCate)
    RecyclerView rv_qCate;

    @BindView(R.id.mllayout_dianhua)
    MyLinearLayout mllayout_dianhua;

    @BindView(R.id.mllayout_kefu)
    MyLinearLayout mllayout_kefu;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.mrlayout_news)
    MyRelativeLayout mrlayout_news;

    private PHelpTwo pHelpTwo;
    private HelpClasAdapter helpClasAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String cate_id="";

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mllayout_dianhua:

                break;
            case R.id.mrlayout_news:
                quick_actions.setVisibility(View.VISIBLE);
                quick_actions.help();
                break;
            case R.id.mllayout_kefu:

                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mllayout_kefu.setOnClickListener(this);
        mrlayout_news.setOnClickListener(this);
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
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
//        storeId = getIntent().getStringExtra("storeId");
        if (!TextUtils.isEmpty(getIntent().getStringExtra("cate_id")))
        cate_id=getIntent().getStringExtra("cate_id");
        pHelpTwo = new PHelpTwo(this, this);
//        pHelpTwo.resetBabys("3");
        pHelpTwo.resetBabys(cate_id);
        mtv_title.setText(getStringResouce(R.string.help_xinshouketang));
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setClass(HelpClassEntity helpClassEntity, final List<HelpClassEntity.Article> articles) {
        if (helpClasAdapter == null) {
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            helpClasAdapter = new HelpClasAdapter(this, false, articles);
            helpClasAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    H5Act.startAct(getBaseContext(),articles.get(position).h5_link,H5Act.MODE_SONIC);
                }
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
    public void setCateOne(final HelpcenterQuestionEntity helpcenterQuestionEntity) {
      }

    @Override
    public void setCateTwo(HelpcenterQuestionEntity helpcenterQuestionEntity, final List<HelpcenterQuestionEntity.Question> questions) {

    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }
}
