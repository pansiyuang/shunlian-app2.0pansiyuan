package com.shunlian.app.ui.goods_detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ComboDetailAdapter;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.utils.DataUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/24.
 */

public class ComboDetailAct extends SideslipBaseActivity {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;


    public static void startAct(Context context){
        Intent intent = new Intent(context,ComboDetailAct.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_combo_detail;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        List<String> aa = DataUtil.getListString(20, "aa");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        ComboDetailAdapter adapter = new ComboDetailAdapter(this,false,aa);
        recy_view.setAdapter(adapter);
    }
}
