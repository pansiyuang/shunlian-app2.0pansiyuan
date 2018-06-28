package com.shunlian.app.ui.returns_order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SelectLogisticsAdapter;
import com.shunlian.app.bean.Contact;
import com.shunlian.app.bean.LogisticsNameEntity;
import com.shunlian.app.presenter.SelectLogisticsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ISelectLogisticsView;
import com.shunlian.app.widget.WaveSideBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/4.
 */

public class SelectLogisticsAct extends BaseActivity implements ISelectLogisticsView {

    @BindView(R.id.rv_contacts)
    RecyclerView rv_contacts;

    @BindView(R.id.side_bar)
    WaveSideBar side_bar;

    private SelectLogisticsPresenter presenter;
    private LinearLayoutManager manager;

    private List<Contact> mLogisticsName = new ArrayList<>();


    public static void startAct(Activity activity,int requestCode){
        Intent intent = new Intent(activity, SelectLogisticsAct.class);
        activity.startActivityForResult(intent,requestCode);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_select_logistics;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        presenter = new SelectLogisticsPresenter(this,this);
        manager = new LinearLayoutManager(this);
        rv_contacts.setLayoutManager(manager);

        side_bar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i=0; i<mLogisticsName.size(); i++) {
                    if (mLogisticsName.get(i).getIndex().equals(index)) {
                        manager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }





    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    /**
     * 选择物流
     *
     * @param logistics
     */
    @Override
    public void selectLogistics(List<LogisticsNameEntity.LogisticsName> logistics) {

        for(LogisticsNameEntity.LogisticsName nameItem : logistics){
            for(String name : nameItem.item_list){
                //System.out.println("name=========="+name);
                Contact contact = new Contact(nameItem.first_letter,name);
                mLogisticsName.add(contact);
            }
        }

        SelectLogisticsAdapter adapter = new SelectLogisticsAdapter(this, mLogisticsName);
        rv_contacts.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mLogisticsName.get(position).getName();
                Intent intent = new Intent();
                intent.putExtra("name",name);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    /**
     * 设置字母排序
     *
     * @param first_letter_list
     */
    @Override
    public void setLetterSort(List<String> first_letter_list) {
        side_bar.setIndexItems((ArrayList<String>) first_letter_list);
    }
}
