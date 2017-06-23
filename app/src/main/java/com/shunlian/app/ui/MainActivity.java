package com.shunlian.app.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;

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

        ll_special.setWHProportion(720,414);
        special_miaosha.setWHProportion(298,414);
        special_qingliang.setWHProportion(422,207);
        special_man.setWHProportion(211,207);
        special_woman.setWHProportion(211,207);
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
