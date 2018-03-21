package com.shunlian.app.ui.discover;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/21.
 */

public class ExperiencePublishActivity extends BaseActivity {

    @BindView(R.id.edt_content)
    EditText edt_content;

    @BindView(R.id.tv_content_count)
    TextView tv_content_count;

    @BindView(R.id.grid_imgs)
    GridView grid_imgs;

    @BindView(R.id.rl_add_goods)
    RecyclerView rl_add_goods;

    private SingleImgAdapter singleImgAdapter;
    private List<ImageEntity> imgList = new ArrayList();

    public static void startAct(Context context) {
        Intent intent = new Intent(context, ExperiencePublishActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_experience_publish;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        singleImgAdapter = new SingleImgAdapter(this, imgList);
        grid_imgs.setAdapter(singleImgAdapter);
    }
}
