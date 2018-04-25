package com.shunlian.app.ui.setting;

import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.presenter.BusinessCardPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IBusinessCardView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/25.
 */

public class BusinessCardAct extends BaseActivity implements IBusinessCardView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.miv_code)
    MyImageView miv_code;

    @BindView(R.id.llayout_share)
    LinearLayout llayout_share;

    @BindView(R.id.llayout_save)
    LinearLayout llayout_save;
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_business_card;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("把APP推荐给好友");
        gone(mrlayout_toolbar_more);

        BusinessCardPresenter presenter = new BusinessCardPresenter(this,this);
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

    /**
     * 二维码连接
     *
     * @param card_path
     */
    @Override
    public void codePath(String card_path) {

    }
}
