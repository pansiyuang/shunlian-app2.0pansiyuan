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
            commonEntities.add(addAccount("15805729571","a1234567"));
            commonEntities.add(addAccount("15068713363","a1234567"));
            commonEntities.add(addAccount("13007562706","zh123456"));
        }else if (netState.contains("api-front.v2")){//预发布
            commonEntities.add(addAccount("15805729571","123456"));
            commonEntities.add(addAccount("15068713363","123456"));
            commonEntities.add(addAccount("13007562706","123456"));
            commonEntities.add(addAccount("15058113375","a1234567890"));
            commonEntities.add(addAccount("17601357886","aini1314"));
        }else {//正式
            commonEntities.add(addAccount("15805729571","123456"));
            commonEntities.add(addAccount("15068713363","123456"));
            commonEntities.add(addAccount("13007562706","123456"));
            commonEntities.add(addAccount("15058113375","a1234567890"));
            commonEntities.add(addAccount("17601357886","aini1314"));
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

    private CommonEntity addAccount(String account,String account_number){
        CommonEntity commonEntity=new CommonEntity();
        commonEntity.account_name=account;
        commonEntity.account_number=account_number;
        return commonEntity;
    }
}
