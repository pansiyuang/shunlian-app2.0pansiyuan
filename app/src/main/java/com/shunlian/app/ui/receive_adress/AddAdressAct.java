package com.shunlian.app.ui.receive_adress;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.store.StoreIntroduceAct;

import butterknife.BindView;

public class AddAdressAct extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.tv_address)
    TextView tv_address;

    public static void startAct(Context context, String storeId) {
        Intent intent = new Intent(context, StoreIntroduceAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_add_adress;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_address:
                StoreIntroduceAct.startAct(this, storeId);
                break;
        }
    }
}
