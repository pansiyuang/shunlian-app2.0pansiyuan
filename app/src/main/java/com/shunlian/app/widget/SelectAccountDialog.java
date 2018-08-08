package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AccountAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.ui.login.LoginPswFrag;
import com.shunlian.app.utils.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class SelectAccountDialog extends Dialog  {


    public SelectAccountDialog(Fragment fragment) {
        this(fragment.getContext(), R.style.MyDialogStyleBottom);
        initView(fragment);
    }


    public SelectAccountDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void initView(Fragment fragment){
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_account, null, false);
        setContentView(view);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rv_account);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getContext(),LinearLayoutManager.VERTICAL,false));
        List<CommonEntity> commonEntities=new ArrayList<>();
        String netState = SharedPrefUtil.getCacheSharedPrf("netState","");
        if (netState.contains("v20-front-api")){//测试
            commonEntities.add(addAccount("15805729571","a1234567","15805729571-augus"));
            commonEntities.add(addAccount("15068713363","a1234567","15068713363-吴小坚"));
            commonEntities.add(addAccount("13007562706","zh123456","13007562706-张贺"));
        }else if (netState.contains("api-front.v2")){//预发布
            commonEntities.add(addAccount("15805729571","123456","15805729571-augus"));
            commonEntities.add(addAccount("15068713363","123456","15068713363-吴小坚"));
            commonEntities.add(addAccount("13007562706","123456","13007562706-张贺"));
            commonEntities.add(addAccount("15058113375","a1234567890","15058113375"));
            commonEntities.add(addAccount("17601357886","aini1314","17601357886-刘利明"));
            commonEntities.add(addAccount("18238602190","a11111111","18238602190"));
            commonEntities.add(addAccount("18200000000","a11111111","18200000000-若男0"));
            commonEntities.add(addAccount("18519122401","123456","18519122401-刘群2"));
            commonEntities.add(addAccount("18070509322","a1234567","18070509322-陈镇"));
            commonEntities.add(addAccount("18238602190","a11111111","18238602190-若男"));
            commonEntities.add(addAccount("17316906575","a1234567","17316906575-秦小龙"));
            commonEntities.add(addAccount("15162472997","123456","15162472997-永志"));
        }else {//正式
            commonEntities.add(addAccount("15805729571","123456","15805729571-augus"));
            commonEntities.add(addAccount("15068713363","123456","15068713363-吴小坚"));
            commonEntities.add(addAccount("13007562706","a12345678","13007562706-张贺"));
            commonEntities.add(addAccount("15058113375","a1234567890","15058113375"));
            commonEntities.add(addAccount("17601357886","aini1314","17601357886-刘利明"));
            commonEntities.add(addAccount("15883829691","tl123456","15883829691-唐姐"));
            commonEntities.add(addAccount("13097200676","aini1314","13097200676-刘利明"));
            commonEntities.add(addAccount("15669900181","123456","15669900181-刘群"));
            commonEntities.add(addAccount("18519122401","123456","18519122401-刘群2"));
            commonEntities.add(addAccount("18070509322","a1234567","18070509322-陈镇"));
            commonEntities.add(addAccount("18238602190","a11111111","18238602190-若男"));
            commonEntities.add(addAccount("17316906575","a1234567","17316906575-秦小龙"));
            commonEntities.add(addAccount("15162472997","123456","15162472997-永志"));
        }
        setCancelable(true);
        AccountAdapter accountAdapter=new AccountAdapter(fragment.getContext(),false,commonEntities);
        recyclerView.setAdapter(accountAdapter);
        accountAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LoginPswFrag loginPswFrag= (LoginPswFrag) fragment;
                loginPswFrag.loginPresenter.LoginUserName(commonEntities.get(position).account_name,commonEntities.get(position).account_number);
                dismiss();
            }
        });
    }

    private CommonEntity addAccount(String account,String account_number,String account_type){
        CommonEntity commonEntity=new CommonEntity();
        commonEntity.account_name=account;
        commonEntity.account_number=account_number;
        commonEntity.account_type=account_type;
        return commonEntity;
    }
}
