package com.shunlian.app.ui.start;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.ui.MBaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/4/12 0012.
 */
public class ADAct extends MBaseActivity {
    @BindView(R.id.miv_ad)
    MyImageView miv_ad;
    @BindView(R.id.mtv_count)
    MyTextView mtv_count;
    private AdEntity adEntity;
    private Handler handler;
    private Timer timeAnim;
    private int time=0;
    private boolean isCancel=false;

    public static void startAct(Context context, AdEntity adEntity) {
        Intent intent = new Intent(context, ADAct.class);
        intent.putExtra("adEntity", adEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_ad;
    }

    @Override
    protected void initData() {
        adEntity = (AdEntity) getIntent().getSerializableExtra("adEntity");
        GlideUtils.getInstance().loadImage(getBaseContext(), miv_ad, adEntity.list.ad_img);
        initTimer();
    }

    public void initTimer() {
        mtv_count.setText(getStringResouce(R.string.first_tianguo) +" 3");
        timeAnim = new Timer();
        if (handler == null) {
            handler = new Handler();
        }
        timeAnim.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        time = time+1;
                        LogUtil.augusLogW("--"+ time);
                        if (time!=3)
                        mtv_count.setText(getStringResouce(R.string.first_tianguo) +" "+(3-time));
                    }
                });
            }
        }, 1000, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isCancel){
                    timeAnim.cancel();
                    MainActivity.startAct(getBaseContext(), "");
                    finish();
                }
            }
        }, 3000);
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_ad.setOnClickListener(this);
        mtv_count.setOnClickListener(this);
    }

    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.mtv_count:
                isCancel=true;
                timeAnim.cancel();
                MainActivity.startAct(getBaseContext(), "");
                finish();
                break;
            case R.id.miv_ad:
                isCancel=true;
                timeAnim.cancel();
                Constant.JPUSH = new ArrayList<>();
                Constant.JPUSH.add(adEntity.list.link.type);
                Constant.JPUSH.add(adEntity.list.link.item_id);
                Constant.JPUSH.add("");
                Common.goGoGo(getBaseContext(), adEntity.list.link.type, adEntity.list.link.item_id);
                finish();
                break;
        }
    }


    //禁止返回键
    @Override
    public void onBackPressed() {
        // 这里处理逻辑代码，大家注意：该方法仅适用于2.0或更新版的sdk
//        super.onBackPressed();
    }

}