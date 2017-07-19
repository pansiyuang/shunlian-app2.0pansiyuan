package com.shunlian.app.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.refresh.PullToRefreshView;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    private long mExitTime;
    private boolean isDoubleBack = false;

    @BindView(R.id.ll_special)
    MyRelativeLayout ll_special;

    @BindView(R.id.special_miaosha)
    MyImageView special_miaosha;

    @BindView(R.id.special_qingliang)
    MyImageView special_qingliang;

    @BindView(R.id.special_man)
    MyImageView special_man;

    @BindView(R.id.special_woman)
    MyImageView special_woman;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.ll_layout)
    PullToRefreshView ll_layout;

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

//        TestPresenter testPresenter = new TestPresenter(this,null);

//        ll_special.setWHProportion(720,414);
//        special_miaosha.setWHProportion(298,414);
//        special_qingliang.setWHProportion(422,207);
//        special_man.setWHProportion(211,207);
//        special_woman.setWHProportion(211,207);

        List<String> items = DataUtil.getListString(40, "条目");


        SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter<String>(this,android.R.layout.simple_list_item_1,items) {

            @Override
            public void convert(SimpleViewHolder holder, String s) {
                holder.setText(android.R.id.text1,s);
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recy_view.setLayoutManager(manager);
        recy_view.setAdapter(simpleRecyclerAdapter);

        ll_layout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ll_layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_layout.setRefreshing(false);
                    }
                },3000);
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出顺联动力", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                isDoubleBack = true;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    finish();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
