package com.shunlian.app.ui.my_profit;

import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.presenter.ProfitExtractPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IProfitExtractView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/16.
 */

public class ProfitExtractAct extends BaseActivity implements IProfitExtractView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.rlayout_extract)
    RelativeLayout rlayout_extract;

    @BindView(R.id.mtv_extract_num)
    MyTextView mtv_extract_num;

    @BindView(R.id.mbtn_sure_extract)
    MyButton mbtn_sure_extract;

    @BindView(R.id.miv_success)
    MyImageView miv_success;

    @BindView(R.id.mtv_go_balance)
    MyTextView mtv_go_balance;
    private ProfitExtractPresenter presenter;


    public static void startAct(Context context,String balance){
        Intent intent = new Intent(context,ProfitExtractAct.class);
        intent.putExtra("balance",balance);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_profitextract;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("收益提现");
        gone(mrlayout_toolbar_more);

        String balance = getIntent().getStringExtra("balance");
        mtv_extract_num.setText(balance);

        presenter = new ProfitExtractPresenter(this,this);
    }

    @OnClick(R.id.mbtn_sure_extract)
    public void sureExtract(){
        if (presenter != null){
            presenter.initApi();
        }
    }

    @OnClick(R.id.mtv_go_balance)
    public void goBalance(){
        Common.staticToast("跳到余额");
    }

    @Override
    public void extractSuccess() {
        gone(rlayout_extract,mbtn_sure_extract);
        visible(miv_success,mtv_go_balance);
        mtv_toolbar_title.setText("提现结果");
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

}
