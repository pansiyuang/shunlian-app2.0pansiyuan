package com.shunlian.app.ui.discover;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FindSelectShopAdapter;
import com.shunlian.app.bean.FindSelectShopEntity;
import com.shunlian.app.presenter.FindSelectShopPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFindSelectShopView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/3/15.
 */

public class FindSelectShopAct extends BaseActivity implements IFindSelectShopView{


    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.miv_all_select)
    MyImageView miv_all_select;

    @BindView(R.id.mtv_all_select)
    MyTextView mtv_all_select;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    public static final int REQUEST_CODE = 600;
    private List<FindSelectShopEntity.StoreList> mStoreLists;
    private FindSelectShopAdapter adapter;
    private String format = "（%s/%s）";
    private FindSelectShopPresenter presenter;

    public static void startAct(Activity activity){
        activity.startActivityForResult(new Intent(activity,FindSelectShopAct.class),REQUEST_CODE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_findselectshop;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        presenter = new FindSelectShopPresenter(this,this);
        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);
        int i = TransformUtil.dip2px(this, 30);
        recy_view.addItemDecoration(new GrideItemDecoration(0,i,0,0,false));
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void setList(final List<FindSelectShopEntity.StoreList> storeLists) {
        if (isEmpty(storeLists))return;
        mStoreLists = storeLists;
        mtv_count.setText(String.format(format,"0",
                String.valueOf(isEmpty(mStoreLists)?0:mStoreLists.size())));
        adapter = new FindSelectShopAdapter(this,storeLists);
        recy_view.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FindSelectShopEntity.StoreList storeList = storeLists.get(position);
                storeList.isSelect = !storeList.isSelect;
                mtv_count.setText(String.format(format,selectCount(),String.valueOf(mStoreLists.size())));
                if (Integer.parseInt(selectCount()) < mStoreLists.size()){
                    miv_all_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
                }else {
                    miv_all_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 关注成功
     */
    @Override
    public void followSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }


    private String selectCount(){
        int count = 0;
        for (int i = 0; i < mStoreLists.size(); i++) {
            FindSelectShopEntity.StoreList storeList = mStoreLists.get(i);
            if (storeList.isSelect){
                count++;
            }
        }
        return String.valueOf(count);
    }

    @OnClick({R.id.miv_all_select,R.id.mtv_all_select,R.id.mtv_count})
    public void allSelect(){
        if (!isEmpty(mStoreLists)){
            for (int i = 0; i < mStoreLists.size(); i++) {
                FindSelectShopEntity.StoreList storeList = mStoreLists.get(i);
                storeList.isSelect = true;
            }
            mtv_count.setText(String.format(format,String.valueOf(mStoreLists.size()),
                    String.valueOf(mStoreLists.size())));
            adapter.notifyDataSetChanged();
            miv_all_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
        }
    }

    @OnClick(R.id.mbt_follow)
    public void follow(){
        if (Integer.parseInt(selectCount()) < 3){
            Common.staticToasts(this,"最少关注三家店铺",R.mipmap.icon_common_tanhao);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mStoreLists.size(); i++) {
            FindSelectShopEntity.StoreList storeList = mStoreLists.get(i);
            if (storeList.isSelect){
                sb.append(storeList.id);
                if (i + 1 != mStoreLists.size()){
                    sb.append(",");
                }
            }
        }

        if (presenter != null){
            presenter.followStore(sb.toString());
        }
    }
}
