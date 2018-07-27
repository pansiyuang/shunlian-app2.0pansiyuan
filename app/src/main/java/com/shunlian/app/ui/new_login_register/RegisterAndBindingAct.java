package com.shunlian.app.ui.new_login_register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/7/23.
 */

public class RegisterAndBindingAct extends BaseActivity {

    @BindView(R.id.mtv_register)
    MyTextView mtv_register;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    /***********登录*************/
    public static final int FLAG_LOGIN = 1;

    /*******注册*********/
    public static final int FLAG_REGISTER = 1 << 1;

    /*
     *绑定标记
     */
    /*******绑定手机号*********/
    public static final int FLAG_BIND_MOBILE = 1 << 2;
    /*******绑定推荐人*********/
    public static final int FLAG_BIND_ID = 1 << 3;
    /*******绑定手机号和推荐人*********/
    public static final int FLAG_BIND_MOBILE_ID = 1 << 4;

    private FragmentManager mFragmentManager;
    private static Map<String, BaseFragment> fragmentMap = new HashMap<>();
    private static final String[] flags = {"one_frag", "two_frag"};
    private int mFlag;
    private OnePageFrag onePageFrag;
    private TwoPageFrag twoPageFrag;
    private boolean isCanBack = false;//是否可以返回 默认不可以
    private String mUniqueSign;
    private String mMobile;
    private String mMember_id;

    public static void startAct(Context context, int flag,String mobile,String unique_sign,String member_id) {
        Intent intent = new Intent(context,RegisterAndBindingAct.class);
        intent.putExtra("flag",flag);
        intent.putExtra("mobile",mobile);
        intent.putExtra("unique_sign",unique_sign);
        intent.putExtra("member_id",member_id);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_register_binding;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(v -> {
            if (isCanBack) {
                oneFrag();
            }else {
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mFlag = getIntent().getIntExtra("flag", FLAG_LOGIN);
        mUniqueSign = getIntent().getStringExtra("unique_sign");
        mMobile = getIntent().getStringExtra("mobile");
        mMember_id = getIntent().getStringExtra("member_id");

        mFragmentManager = getSupportFragmentManager();
        visible(mtv_register);
        oneFrag();
    }

    @OnClick(R.id.mtv_register)
    public void register(){
        mFlag = FLAG_REGISTER;
        oneFrag();
        gone(mtv_register);
    }

    private void oneFrag() {
        isCanBack = false;
        if (onePageFrag == null){
            onePageFrag = (OnePageFrag) fragmentMap.get(flags[0]);
            if (onePageFrag == null){
                onePageFrag = new OnePageFrag();
                fragmentMap.put(flags[0],onePageFrag);
                Bundle bundle = new Bundle();
                bundle.putInt("flag",mFlag);
                bundle.putString("mobile",mMobile);
                bundle.putString("unique_sign",mUniqueSign);
                bundle.putString("member_id",mMember_id);
                onePageFrag.setArguments(bundle);
            }else {
                onePageFrag.resetPage(mFlag,mMobile,mUniqueSign,mMember_id);
            }
        }else {
            onePageFrag.resetPage(mFlag,mMobile,mUniqueSign,mMember_id);
        }
        switchContent(onePageFrag);
    }

    /**
     *
     * @param refereesId 推荐人id
     * @param mobile 手机号
     * @param picCode 图形验证码
     * @param unique_sign 微信登录code
     * @param flag 状态
     */
    public void twoFrag(String refereesId,String mobile,String picCode,
                        String unique_sign,String member_id,int flag) {
        isCanBack = true;
        if (twoPageFrag == null){
            twoPageFrag = (TwoPageFrag) fragmentMap.get(flags[1]);
            if (twoPageFrag == null){
                twoPageFrag = new TwoPageFrag();
                fragmentMap.put(flags[1],twoPageFrag);
                Bundle bundle = new Bundle();
                bundle.putString("mobile",mobile);
                bundle.putInt("flag",flag);
                bundle.putString("refereesId",refereesId);
                bundle.putString("picCode",picCode);
                bundle.putString("unique_sign",unique_sign);
                bundle.putString("member_id",member_id);
                twoPageFrag.setArguments(bundle);
            }else {
                twoPageFrag.resetPage(refereesId,mobile,picCode,unique_sign,member_id,flag);
            }
        }else {
            twoPageFrag.resetPage(refereesId,mobile,picCode,unique_sign,member_id,flag);
        }

        switchContent(twoPageFrag);
    }


    public void switchContent(Fragment show) {
        if (show != null) {
            if (!show.isAdded()) {
                mFragmentManager.beginTransaction().remove(show)
                        .commitAllowingStateLoss();
                mFragmentManager.beginTransaction().add(R.id.frame_content, show)
                        .commitAllowingStateLoss();
            } else {
                mFragmentManager.beginTransaction().show(show).commitAllowingStateLoss();
            }
            if (fragmentMap != null && fragmentMap.size() > 0) {
                Set<String> keySet = fragmentMap.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    BaseFragment baseFragment = fragmentMap.get(key);
                    if (show != baseFragment) {
                        if (baseFragment != null && baseFragment.isVisible()) {
                            mFragmentManager.beginTransaction().hide(baseFragment)
                                    .commitAllowingStateLoss();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (isCanBack) {
                oneFrag();
                return true;
            }else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragmentMap != null){
            fragmentMap.clear();
        }
    }
}
