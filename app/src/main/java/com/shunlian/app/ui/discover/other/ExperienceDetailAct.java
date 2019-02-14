package com.shunlian.app.ui.discover.other;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
//import com.shunlian.app.presenter.ExperienceDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IExperienceDetailView;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ExperienceDetailAct extends BaseActivity implements IExperienceDetailView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.met_text)
    MyEditText met_text;

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;
//
    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

//    private ExperienceDetailPresenter presenter;
    private LinearLayoutManager manager;


    public static void startAct(Context context,String experience_id){
        Intent intent = new Intent(context, ExperienceDetailAct.class);
        intent.putExtra("experience_id",experience_id);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_experiencedetail;
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if (manager != null && presenter != null){
//                    int lastPosition = manager.findLastVisibleItemPosition();
//                    if (lastPosition + 1 == manager.getItemCount()){
//                        presenter.onRefresh();
//                    }
//                }
            }
        });

        met_text.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (s.length() > 300){
                    met_text.setText(s.subSequence(0,300));
                    met_text.setSelection(300);
                    Common.staticToast("字数不能超过300");
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        immersionBar.statusBarColor(R.color.white).statusBarView(R.id.view_status_bar)
                .statusBarDarkFont(true, 0.2f)
                .keyboardEnable(true)
                .init();

        mtv_toolbar_title.setText("心得详情");

        String experience_id = getIntent().getStringExtra("experience_id");
//        presenter = new ExperienceDetailPresenter(this,this,experience_id);

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(this, 30);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getColorResouce(R.color.white)));

        GradientDrawable textBG = (GradientDrawable) met_text.getBackground();
        textBG.setColor(getColorResouce(R.color.value_F2F6F9));
    }

    @OnClick(R.id.mrlayout_toolbar_more)
    public void more(){
        visible(quick_actions);
        quick_actions.findCommentList();
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
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }


    @OnClick(R.id.met_text)
    public void onClick(){
        setEdittextFocusable(true,met_text);
    }

    @OnClick(R.id.mtv_send)
    public void send(){
        String s = met_text.getText().toString();
        if (isEmpty(s)){
            Common.staticToast("评论内容不能为空");
            return;
        }
//        presenter.sendExperience(s);
        met_text.setText("");
        met_text.setHint(getStringResouce(R.string.add_comments));
        setEdittextFocusable(false,met_text);
        Common.hideKeyboard(met_text);
    }

    /**
     * 软键盘处理
     */
    @Override
    public void showorhideKeyboard(String hint) {
        setEdittextFocusable(true, met_text);
        met_text.setHint(hint);
        if (!isSoftShowing()) {
            Common.showKeyboard(met_text);
        } else {
            Common.hideKeyboard(met_text);
        }
    }

    /**
     * 删除提示
     */
    @Override
    public void delPrompt() {
        final PromptDialog dialog = new PromptDialog(this);
        dialog.setSureAndCancleListener(getStringResouce(R.string.are_you_sure_del_comment),
                getStringResouce(R.string.confirm), v -> {
//                    if (presenter != null){
//                        presenter.delComment();
//                    }
                    dialog.dismiss();
                }, getStringResouce(R.string.errcode_cancel), v -> dialog.dismiss()).show();
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
//        if (presenter != null){
//            presenter.detachView();
//            presenter = null;
//        }
    }
}
