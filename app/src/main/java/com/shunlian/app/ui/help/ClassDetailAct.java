package com.shunlian.app.ui.help;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.presenter.ClassDetailPresenter;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.view.IClassDetailView;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ClassDetailAct extends H5Act implements IClassDetailView{


    private ClassDetailPresenter mPresenter;


    public static void startAct(Context context, String url,String id, int mode) {
        Intent intentH5 = new Intent(context, ClassDetailAct.class);
        intentH5.putExtra("url", url);
        intentH5.putExtra("id", id);//课堂id
        intentH5.putExtra("mode", mode);
        intentH5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intentH5);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        super.initData();
        String id = mIntent.getStringExtra("id");
        mPresenter = new ClassDetailPresenter(this,this,id);
    }

    @Override
    protected void initListener() {
        super.initListener();
        rl_title_more.setVisibility(View.VISIBLE);
        rl_title_more.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.rl_title_more:
                quick_actions.setVisibility(View.VISIBLE);
                quick_actions.classDetailShare();
                if (mPresenter != null)
                    quick_actions.shareInfo(mPresenter.getShareInfoParam());
                break;
        }
    }
}
