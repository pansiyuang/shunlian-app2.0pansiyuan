package com.shunlian.app.ui.new_user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class NewRuleActivity extends BaseActivity {
    private SimpleRecyclerAdapter adapter;

    @BindView(R.id.recy_view_invitaion)
    RecyclerView recy_view_invitaion;

    private List<String> invitationData;
    @Override
    protected int getLayoutId() {
        return R.layout.act_user_invitation_layout;
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        invitationData = new ArrayList<>();
        for (int i=0;i<40;i++){
            invitationData.add(i+"");
        }
        recy_view_invitaion.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SimpleRecyclerAdapter<String>(this, R.layout.item_tag_layout, this.invitationData) {
            @Override
            public void convert(SimpleViewHolder holder, String  goods, int position) {
              TextView textView =  holder.getView(R.id.tv_history_tag);
                textView.setText(goods+"信息");
            }
        };
        recy_view_invitaion.setAdapter(adapter);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NewRuleActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }


}
