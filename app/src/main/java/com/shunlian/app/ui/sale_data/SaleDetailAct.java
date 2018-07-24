package com.shunlian.app.ui.sale_data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.SaleDetailPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ISaleDetailView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/13.
 */

public class SaleDetailAct extends BaseActivity implements ISaleDetailView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_total_profit)
    MyTextView mtv_total_profit;

    @BindView(R.id.mtv_total_sale)
    MyTextView mtv_total_sale;

    @BindView(R.id.llayout_classify1)
    LinearLayout llayout_classify1;

    @BindView(R.id.mtv_classify1)
    MyTextView mtv_classify1;

    @BindView(R.id.miv_classify1)
    MyImageView miv_classify1;

    @BindView(R.id.llayout_classify2)
    LinearLayout llayout_classify2;

    @BindView(R.id.mtv_classify2)
    MyTextView mtv_classify2;

    @BindView(R.id.miv_classify2)
    MyImageView miv_classify2;

    @BindView(R.id.llayout_classify3)
    LinearLayout llayout_classify3;

    @BindView(R.id.mtv_classify3)
    MyTextView mtv_classify3;

    @BindView(R.id.miv_classify3)
    MyImageView miv_classify3;

    @BindView(R.id.mtv_type1)
    MyTextView mtv_type1;

    @BindView(R.id.mtv_type2)
    MyTextView mtv_type2;

    @BindView(R.id.mtv_type3)
    MyTextView mtv_type3;

    @BindView(R.id.mtv_type4)
    MyTextView mtv_type4;

    @BindView(R.id.llayout_column)
    LinearLayout llayout_column;

    @BindView(R.id.view_line)
    View view_line;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private LinearLayoutManager manager;
    private SaleDetailPresenter presenter;
    public static final int SALE_DETAIL = 100;//销售明细
    public static final int REWARD_DETAIL = 400;//奖励明细
    private int type;

    public static void startAct(Context context,int type){
        Intent intent = new Intent(context, SaleDetailAct.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_saledetail;
    }

    @Override
    protected void initListener() {
        super.initListener();
        llayout_classify1.setOnClickListener(this);
        llayout_classify2.setOnClickListener(this);
        llayout_classify3.setOnClickListener(this);
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()&&presenter!=null){
                        presenter.onRefresh();
                    }
                }
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

        type = getIntent().getIntExtra("type", 0);
        String classify1 = "";
        String classify2 = "";
        String classify3 = "";
        String tv_type1 = "";
        String tv_type2 = "";
        String tv_type3 = "";
        if (type == SALE_DETAIL){
            mtv_toolbar_title.setText("销售明细");
            classify1="总销量";
            classify2="小店销量";
            classify3="分店销量";
            tv_type1 = "销售金额";
            tv_type2 = "销售类型";
            tv_type3 = "销售时间";
            visible(mtv_type4);
            gone(llayout_classify3);
        }else if (type == REWARD_DETAIL){
            mtv_toolbar_title.setText("奖励明细");
            classify1="总奖励";
            classify2="周奖励";
            classify3="月奖励";
            tv_type1 = "奖励类型";
            tv_type2 = "奖励发放时间";
            tv_type3 = "金额";
            gone(mtv_type4);
        }
        gone(mrlayout_toolbar_more);
        mtv_classify1.setText(classify1);
        mtv_classify2.setText(classify2);
        mtv_classify3.setText(classify3);

        mtv_type1.setText(tv_type1);
        mtv_type2.setText(tv_type2);
        mtv_type3.setText(tv_type3);

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int i = TransformUtil.dip2px(this, 0.5f);
        recy_view.addItemDecoration(new VerticalItemDecoration(i,
                0,0,getColorResouce(R.color.light_gray_three)));

        presenter = new SaleDetailPresenter(this,this, type);

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
        if (request_code == 100){
            gone(llayout_column,view_line,recy_view);
            visible(nei_empty);
            String msg = null;
            if (type == SALE_DETAIL && presenter != null) {
                switch (presenter.curType){
                    case "1":
                        msg = "您还没有领取过小店销售~\n是不是你太懒了";
                        break;
                    case "2":
                        msg = "您还没有领取过分店销售~\n是不是你太懒了";
                        break;
                    case "3":
                        msg = "您还没有领取过总销售~\n是不是你太懒了";
                        break;
                }
            }else if (type == REWARD_DETAIL && presenter != null){
                switch (presenter.curType){
                    case "1":
                        msg = "您还没有领取过周奖励~\n是不是你太懒了";
                        break;
                    case "2":
                        msg = "您还没有领取过月奖励~\n是不是你太懒了";
                        break;
                    case "3":
                        msg = "您还没有领取过总奖励~\n是不是你太懒了";
                        break;
                }
            }
            nei_empty.setImageResource(R.mipmap.img_empty_common).setText(msg).setButtonText("");
        }else {
            visible(llayout_column,view_line,recy_view);
            gone(nei_empty);
        }
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }

    /**
     * 设置总销售 总收益
     *
     * @param sale
     * @param profit
     */
    @Override
    public void setTotalSale_Profit(String sale, String profit) {
        if (type == SALE_DETAIL) {
            mtv_total_sale.setText("累计销售:"+sale);
            mtv_total_profit.setText("累计收益:"+profit);
        }else {
            gone(mtv_total_sale);
            mtv_total_profit.setText("累计奖励:"+profit);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.llayout_classify1:
                if (miv_classify1.getVisibility()==View.VISIBLE){
                    return;
                }
                changeSate(1);
                if (presenter != null){
                    presenter.curType = presenter.type3;
                    presenter.initApi();
                }
                break;
            case R.id.llayout_classify2:
                if (miv_classify2.getVisibility()==View.VISIBLE){
                    return;
                }
                changeSate(2);
                if (presenter != null){
                    presenter.curType = presenter.type1;
                    presenter.initApi();
                }
                break;
            case R.id.llayout_classify3:
                if (miv_classify3.getVisibility()==View.VISIBLE){
                    return;
                }
                changeSate(3);
                if (presenter != null){
                    presenter.curType = presenter.type2;
                    presenter.initApi();
                }
                break;
        }
    }


    private void changeSate(int state){
        int white = getColorResouce(R.color.white);
        int value_BDBDBD = getColorResouce(R.color.value_BDBDBD);
        int pink_color = getColorResouce(R.color.pink_color);
        int colorAccent = getColorResouce(R.color.colorAccent);

        mtv_classify1.setTextColor(state == 1?white:value_BDBDBD);
        miv_classify1.setVisibility(state==1?View.VISIBLE:View.GONE);
        mtv_classify1.setBackgroundColor(state==1?pink_color:colorAccent);


        mtv_classify2.setTextColor(state == 2?white:value_BDBDBD);
        miv_classify2.setVisibility(state==2?View.VISIBLE:View.GONE);
        mtv_classify2.setBackgroundColor(state==2?pink_color:colorAccent);


        mtv_classify3.setTextColor(state == 3?white:value_BDBDBD);
        miv_classify3.setVisibility(state==3?View.VISIBLE:View.GONE);
        mtv_classify3.setBackgroundColor(state==3?pink_color:colorAccent);
    }
}
