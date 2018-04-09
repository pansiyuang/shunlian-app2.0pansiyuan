package com.shunlian.app.ui.message;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.R;
import com.shunlian.app.presenter.SystemMsgPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ISystemMsgView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/8.
 */

public class SystemMsgAct extends BaseActivity implements ISystemMsgView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    public static void startAct(Context context){
        context.startActivity(new Intent(context,SystemMsgAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_system_msg;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("系统通知");

        SystemMsgPresenter presenter = new SystemMsgPresenter(this,this);

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
