package com.shunlian.app.ui.discover;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.GuanzhuPresenter;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IGuanzhuView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import butterknife.BindView;


public class DiscoverGuanzhuFrag extends DiscoversFrag implements IGuanzhuView{

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private LinearLayoutManager manager;
    private GuanzhuPresenter presenter;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_guanzhu,null,false);
    }

    @Override
    protected void initData() {
//        CommentListAct.startAct(baseActivity,"1");
//        FindSelectShopAct.startAct(baseActivity);

        presenter = new GuanzhuPresenter(baseContext,this);
        manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);
        int space = TransformUtil.dip2px(baseActivity, 10);
        recy_view.addItemDecoration(new VerticalItemDecoration(space,
                0,0,getColorResouce(R.color.white_ash)));

    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()){
                        if (presenter != null){
                            presenter.onRefresh();
                        }
                    }
                }
            }
        });

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
        if (request_code == 0){//显示空页面
            visible(nestedScrollView);
            gone(recy_view);
            nei_empty.setImageResource(R.mipmap.img_empty_faxian)
                    .setText("咦？还没有关注哦")
                    .setButtonText("马上去关注").setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FindSelectShopAct.startAct(baseActivity);
                }
            });
        }else {
            gone(nestedScrollView);
            visible(recy_view);
        }
    }



    /**
     * 设置adapter
     *
     * @param adapter
     */
    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }
}
