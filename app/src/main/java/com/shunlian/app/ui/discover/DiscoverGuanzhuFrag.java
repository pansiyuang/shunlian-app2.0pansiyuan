package com.shunlian.app.ui.discover;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.GuanzhuAdapter;
import com.shunlian.app.bean.GuanzhuEntity;
import com.shunlian.app.presenter.GuanzhuPresenter;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IGuanzhuView;

import java.util.List;

import butterknife.BindView;


public class DiscoverGuanzhuFrag extends DiscoversFrag implements IGuanzhuView{

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_guanzhu,null,false);
    }

    @Override
    protected void initData() {
//        CommentListAct.startAct(baseActivity,"1");

        GuanzhuPresenter presenter = new GuanzhuPresenter(baseContext,this);
        LinearLayoutManager manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(baseActivity, 10);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getColorResouce(R.color.white_ash)));
    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        return false;
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    /**
     * 关注列表
     *
     * @param listBeans
     * @param page
     * @param allpage
     */
    @Override
    public void setGuanzhuList(List<GuanzhuEntity.DynamicListBean> listBeans, int page, int allpage) {
        GuanzhuAdapter adapter = new GuanzhuAdapter(baseActivity,listBeans);
        recy_view.setAdapter(adapter);
    }
}
