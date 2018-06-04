package com.shunlian.app.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.SelectLikePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ISelectLikeView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/23.
 */

public class SelectLikeAct extends BaseActivity implements ISelectLikeView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.mtv_select)
    MyTextView mtv_select;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mbtn_sure)
    MyButton mbtn_sure;

    public static final int REQUEST_CODE = 300;
    private final int maxCount = 100000;//最多可选标签
    private final String format = "%d/%d";
    private SelectLikePresenter presenter;

    public static void startAct(Activity activity,String tag){
        Intent intent = new Intent(activity, SelectLikeAct.class);
        intent.putExtra("tag",tag);
        activity.startActivityForResult(intent,REQUEST_CODE);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_selectlike;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("选择我喜欢的");
        gone(mrlayout_toolbar_more);
        presenter = new SelectLikePresenter(this,this);

        String tag = getIntent().getStringExtra("tag");

        if (!isEmpty(tag)){
            String s = tag.replaceAll("、", "/");
            mtv_select.setText(s);
            mtv_select.setTextColor(getColorResouce(R.color.pink_color));
            String[] split = tag.split("、");
            mtv_count.setText(String.format(format,split.length,maxCount));
            presenter.currentCount = split.length;
        }

        GradientDrawable sureBG = (GradientDrawable) mbtn_sure.getBackground();
        sureBG.setColor(getColorResouce(R.color.pink_color));
        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);
    }



    @OnClick(R.id.mbtn_sure)
    public void sure(){
        if (presenter != null){
            String[] tag = presenter.getCount();
            if (tag != null && tag.length >= 2) {
                Intent intent = new Intent();
                intent.putExtra("name", tag[0]);
                intent.putExtra("id", tag[1]);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
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
    public void setTextTag(String tag,int count) {
        if (count == 0){
            mtv_select.setTextColor(getColorResouce(R.color.share_text));
        }else {
            mtv_select.setTextColor(getColorResouce(R.color.pink_color));
        }
        mtv_select.setText(tag);
        mtv_count.setText(String.format(format,count,maxCount));
    }
}
