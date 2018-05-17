package com.shunlian.app.ui;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.UpdateEntity;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.presenter.PMain;
import com.shunlian.app.ui.fragment.DiscoverFrag;
import com.shunlian.app.ui.fragment.PersonalCenterFrag;
import com.shunlian.app.ui.fragment.ShoppingCarFrag;
import com.shunlian.app.ui.fragment.SortFrag;
import com.shunlian.app.ui.fragment.first_page.FirstPageFrag;
import com.shunlian.app.ui.login.LoginAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IMain;
import com.shunlian.app.widget.MyFrameLayout;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.UpdateDialog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements  MessageCountManager.OnGetMessageListener, IMain {
    private static final String[] flags = {"mainPage", "sort", "discover", "shoppingcar", "personCenter"};
    private static Map<String, BaseFragment> fragmentMap = new HashMap<>();
    @BindView(R.id.fl_main)
    MyFrameLayout fl_main;
    @BindView(R.id.ll_tab_main_page)
    LinearLayout ll_tab_main_page;
    @BindView(R.id.miv_tab_main)
    MyImageView miv_tab_main;
    @BindView(R.id.tv_tab_main)
    TextView tv_tab_main;
    @BindView(R.id.ll_tab_sort)
    LinearLayout ll_tab_sort;
    @BindView(R.id.miv_tab_sort)
    MyImageView miv_tab_sort;
    @BindView(R.id.tv_tab_sort)
    TextView tv_tab_sort;
    @BindView(R.id.ll_tab_discover)
    LinearLayout ll_tab_discover;
    @BindView(R.id.miv_tab_discover)
    MyImageView miv_tab_discover;
    @BindView(R.id.tv_tab_discover)
    TextView tv_tab_discover;
    @BindView(R.id.ll_tab_shopping_car)
    LinearLayout ll_tab_shopping_car;
    @BindView(R.id.miv_shopping_car)
    MyImageView miv_shopping_car;
    @BindView(R.id.miv_first)
    MyImageView miv_first;
    @BindView(R.id.tv_shopping_car)
    TextView tv_shopping_car;
    @BindView(R.id.ll_tab_person_center)
    LinearLayout ll_tab_person_center;
    @BindView(R.id.miv_person_center)
    MyImageView miv_person_center;
    @BindView(R.id.tv_person_center)
    TextView tv_person_center;
    //    private MainPageFrag mainPageFrag;
    private FirstPageFrag mainPageFrag;
    private SortFrag sortFrag;
    private DiscoverFrag discoverFrag;
    private ShoppingCarFrag shoppingCarFrag;
    private PersonalCenterFrag personalCenterFrag;
    private long mExitTime;
    private boolean isDoubleBack = false;
    private FragmentManager fragmentManager;
    private int pageIndex;
    private String flag;
    private Dialog dialog_ad;
    private MessageCountManager messageCountManager;
    private PMain pMain;
    private UpdateDialog updateDialogV;//判断是否需要跟新
    private boolean isPerson=false;
    private Handler handler;

    public static void startAct(Context context, String flag) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("flag", flag);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

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
        if (updateDialogV==null)
            updateDialogV = new UpdateDialog(this) {
            @Override
            public void initDialogFinish() {
                if (updateDialogV.updateDialog==null){
                    pMain=new PMain(MainActivity.this,MainActivity.this);
                    pMain.getPopAD();
                }
            }
        };
        fragmentManager = getSupportFragmentManager();
        mainPageClick();

        if (Common.isAlreadyLogin()) {
            EasyWebsocketClient.initWebsocketClient(this); //初始化聊天

            messageCountManager = MessageCountManager.getInstance(this);
            messageCountManager.initData();
            messageCountManager.setOnGetMessageListener(this);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        ll_tab_main_page.setOnClickListener(this);
        ll_tab_sort.setOnClickListener(this);
        ll_tab_discover.setOnClickListener(this);
        ll_tab_shopping_car.setOnClickListener(this);
        ll_tab_person_center.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        if (isPerson&&Common.isAlreadyLogin()){
            personCenterClick();
            isPerson=false;
        }
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        flag = getIntent().getStringExtra("flag");
        if (TextUtils.isEmpty(flag)) {
            mainPageClick();
        } else {
            switch2jump(flag);
        }
    }

    public void switchContent(Fragment show) {
        if (show != null) {
            if (!show.isAdded()) {
                fragmentManager.beginTransaction().remove(show).commitAllowingStateLoss();
                fragmentManager.beginTransaction().add(R.id.fl_main, show).commitAllowingStateLoss();
            } else {
                fragmentManager.beginTransaction().show(show).commitAllowingStateLoss();
            }
            if (fragmentMap != null && fragmentMap.size() > 0) {
                Set<String> keySet = fragmentMap.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    BaseFragment baseFragment = fragmentMap.get(key);
                    if (show != baseFragment) {
                        if (baseFragment != null && baseFragment.isVisible()) {
                            fragmentManager.beginTransaction().hide(baseFragment).commitAllowingStateLoss();
                        }
                    }
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        if (MyOnClickListener.isFastClick()) {
            return;
        }
        if (view.getId()==R.id.ll_tab_main_page){
            miv_first.setVisibility(View.VISIBLE);
            miv_tab_main.setVisibility(View.GONE);
            tv_tab_main.setVisibility(View.GONE);
            miv_first.animate().rotation(0).setDuration(0).start();
            miv_first.animate().rotation(360).setDuration(300).start();
        }else {
            miv_first.setVisibility(View.GONE);
            miv_tab_main.setVisibility(View.VISIBLE);
            tv_tab_main.setVisibility(View.VISIBLE);
            view.animate().scaleX(0.2f).scaleY(0.2f).setDuration(0).start();
            view.animate().scaleX(1).scaleY(1).setDuration(300).start();
        }
        if (handler==null)
            handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (view.getId()) {
                    case R.id.ll_tab_main_page:
                        mainPageClick();
                        break;
                    case R.id.ll_tab_sort:
                        sortClick();
                        break;
                    case R.id.ll_tab_discover:
                        discoverClick();
                        break;
                    case R.id.ll_tab_shopping_car:
                        shoppingCarClick();
                        break;
                    case R.id.ll_tab_person_center:
                        personCenterClick();
                        break;
                }
            }
        },300);
    }
    public void mainPageClick() {
        if (mainPageFrag == null) {
//            mainPageFrag = (MainPageFrag) fragmentMap.get(flags[0]);
            mainPageFrag = (FirstPageFrag) fragmentMap.get(flags[0]);
            if (mainPageFrag == null) {
//                mainPageFrag = new MainPageFrag();
                mainPageFrag = new FirstPageFrag();
                fragmentMap.put(flags[0], mainPageFrag);
            }
        }
        switchContent(mainPageFrag);
        pageIndex = 0;
        chageTabItem(pageIndex);
    }

    public void sortClick() {
        //先判断此碎片是否第一次点击，是的话初始化碎片
        if (sortFrag == null) {
            sortFrag = (SortFrag) fragmentMap.get(flags[1]);
            if (sortFrag == null) {
                sortFrag = new SortFrag();
                fragmentMap.put(flags[1], sortFrag);
            }
        }
        //把当前点击的碎片作为参数，表示显示当前碎片，并且隐藏其他碎片
        switchContent(sortFrag);
        pageIndex = 1;
        chageTabItem(pageIndex);
    }

    public void discoverClick() {
        //先判断此碎片是否第一次点击，是的话初始化碎片
        if (discoverFrag == null) {
            discoverFrag = (DiscoverFrag) fragmentMap.get(flags[2]);
            if (discoverFrag == null) {
                discoverFrag = new DiscoverFrag();
                fragmentMap.put(flags[2], discoverFrag);
            }
        }
        switchContent(discoverFrag);
        pageIndex = 2;
        chageTabItem(pageIndex);
    }

    public void shoppingCarClick() {
        //先判断此碎片是否第一次点击，是的话初始化碎片
        if (shoppingCarFrag == null) {
            shoppingCarFrag = (ShoppingCarFrag) fragmentMap.get(flags[3]);
            if (shoppingCarFrag == null) {
                shoppingCarFrag = new ShoppingCarFrag();
                fragmentMap.put(flags[3], shoppingCarFrag);
            }
        } else {
            LogUtil.httpLogW("shoppingCarClick() ");
            shoppingCarFrag.getShoppingCarData();
        }
        //把当前点击的碎片作为参数，表示显示当前碎片，并且隐藏其他碎片
        switchContent(shoppingCarFrag);
        pageIndex = 3;
        chageTabItem(pageIndex);
    }

    public void personCenterClick() {
        if (!Common.isAlreadyLogin()) {
            LoginAct.startAct(this);
            isPerson=true;
            return;
        }
        //先判断此碎片是否第一次点击，是的话初始化碎片
        if (personalCenterFrag == null) {
            personalCenterFrag = (PersonalCenterFrag) fragmentMap.get(flags[4]);
            if (personalCenterFrag == null) {
                personalCenterFrag = new PersonalCenterFrag();
                fragmentMap.put(flags[4], personalCenterFrag);
            }
        }/*else {
            personalCenterFrag.initData();
        }*/
//        if (personalCenterFrag.personalcenterPresenter!=null){
//            personalCenterFrag.personalcenterPresenter.getApiData();
//        }
        //把当前点击的碎片作为参数，表示显示当前碎片，并且隐藏其他碎片
        switchContent(personalCenterFrag);
        pageIndex = 4;
        chageTabItem(pageIndex);
    }

    private void chageTabItem(int pageIndex) {
//        miv_tab_main.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_1_n));
//        tv_tab_main.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_tab_sort.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_2_n));
        tv_tab_sort.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_tab_discover.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_3_n));
        tv_tab_discover.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_shopping_car.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_4_n));
        tv_shopping_car.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_person_center.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_5_n));
        tv_person_center.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_first.setVisibility(View.GONE);
        miv_tab_main.setVisibility(View.VISIBLE);
        tv_tab_main.setVisibility(View.VISIBLE);

        switch (pageIndex) {
            case 0:
                miv_first.setVisibility(View.VISIBLE);
                miv_tab_main.setVisibility(View.GONE);
                tv_tab_main.setVisibility(View.GONE);
//                miv_tab_main.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_1_h));
//                tv_tab_main.setTextColor(getResources().getColor(R.color.pink_color));
                break;
            case 1:
                miv_tab_sort.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_2_h));
                tv_tab_sort.setTextColor(getResources().getColor(R.color.pink_color));
                break;
            case 2:
                miv_tab_discover.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_3_h));
                tv_tab_discover.setTextColor(getResources().getColor(R.color.pink_color));
                break;
            case 3:
                miv_shopping_car.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_4_h));
                tv_shopping_car.setTextColor(getResources().getColor(R.color.pink_color));
                break;
            case 4:
                miv_person_center.setBackgroundDrawable(getResources().getDrawable(R.mipmap.tab_5_h));
                tv_person_center.setTextColor(getResources().getColor(R.color.pink_color));
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Common.staticToast("再按一次退出顺联动力");
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
                } finally {
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void switch2jump(String flag) {
        switch (flag) {
            case "mainPage":
                mainPageClick();
                break;
            case "sort":
                sortClick();
                break;
            case "discover":
                discoverClick();
                break;
            case "shoppingcar":
                shoppingCarClick();
                break;
            case "personCenter":
                personCenterClick();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (updateDialogV != null) {
            if (updateDialogV.updateDialog != null) {
                updateDialogV.updateDialog.dismiss();
            }
            PromptDialog promptDialog = updateDialogV.getPromptDialog();
            if (promptDialog != null) {
                promptDialog.dismiss();
                promptDialog = null;
            }
        }
        super.onDestroy();
    }


    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        //可以开始设置消息数量的数据了
    }

    @Override
    public void OnLoadFail() {
        //可以开始设置消息数量的数据了
    }

    public void initDialog(AdEntity data) {
        if (dialog_ad == null) {
            dialog_ad = new Dialog(this, R.style.popAd);
            dialog_ad.setContentView(R.layout.dialog_ad);
            MyImageView miv_close = (MyImageView) dialog_ad.findViewById(R.id.miv_close);
            MyImageView miv_photo = (MyImageView) dialog_ad.findViewById(R.id.miv_photo);
            MyImageView miv_button = (MyImageView) dialog_ad.findViewById(R.id.miv_button);
            GlideUtils.getInstance().loadImage(getBaseContext(), miv_photo, data.list.ad_img);
            GlideUtils.getInstance().loadImage(getBaseContext(), miv_button, data.list.button);
            miv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_ad.dismiss();
                }
            });
            miv_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.goGoGo(getBaseContext(), data.list.link.type, data.list.link.item_id);
                    dialog_ad.dismiss();
                }
            });
            dialog_ad.setCancelable(false);
        }
        dialog_ad.show();
    }

    @Override
    public void setAD(AdEntity data) {
        if ("1".equals(data.show)){
            initDialog(data);
        }
    }

    @Override
    public void setUpdateInfo(UpdateEntity data) {
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
