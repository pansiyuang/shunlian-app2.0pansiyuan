package com.shunlian.app.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.presenter.TestPresenter;

public class MainActivity extends BaseActivity {


    private long mExitTime;
    private boolean isDoubleBack = false;

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

        TestPresenter testPresenter = new TestPresenter(this,null);


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
                }catch (ActivityNotFoundException e){
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
