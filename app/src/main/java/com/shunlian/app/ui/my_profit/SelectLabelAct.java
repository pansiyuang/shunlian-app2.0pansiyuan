package com.shunlian.app.ui.my_profit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.SelectLabelPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISelectLabelView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/19.
 */

public class SelectLabelAct extends BaseActivity implements ISelectLabelView{

    @BindView(R.id.miv_pic)
    MyImageView miv_pic;

    @BindView(R.id.mtv_sex_set)
    MyTextView mtv_sex_set;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mbtn_sure)
    MyButton mbtn_sure;
    private SelectLabelPresenter presenter;
    private int label;


    public static void startAct(Activity context, int label){
        Intent intent = new Intent(context,SelectLabelAct.class);
        intent.putExtra("label",label);
        context.startActivityForResult(intent,100);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_select_label;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);
        int i = TransformUtil.dip2px(this, 27);
        recy_view.addItemDecoration(new GridSpacingItemDecoration(3,i,false));

        label = getIntent().getIntExtra("label", -1);
        int picId = 0;
        String text = "";
        if (label == 1){//男
            picId = R.mipmap.img_biaoqian_nan;
            text = "您选择的性别为帅哥";
        }else if (label == 2){//女
            picId = R.mipmap.img_biaoqian_nv;
            text = "您选择的性别为美女";
        }else {
            picId = R.mipmap.img_biaoqian_mimi;
            text = "您选择的性别为秘密";
        }
        miv_pic.setImageResource(picId);
        mtv_sex_set.setText(text);
        presenter = new SelectLabelPresenter(this,this);
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
    public void setAdapter(RecyclerView.Adapter adapter) {
        recy_view.setAdapter(adapter);
    }

    @Override
    public void success() {
        setResult(Activity.RESULT_OK);
        Common.goGoGo(this,"mainPage");
        finish();
    }

    @Override
    public void failure() {

    }

    @OnClick(R.id.mbtn_sure)
    public void sure(){
        if (presenter != null){
            presenter.submit(label);
        }
    }
}
