package pay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.PayListAdapter;
import com.shunlian.app.bean.PayListEntity;
import com.shunlian.app.presenter.PayListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IPayListView;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/7.
 */

public class PayListActivity extends BaseActivity implements View.OnClickListener,IPayListView {


    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.recy_pay)
    RecyclerView recy_pay;
    private PayListPresenter payListPresenter;

    public static void startAct(Context context){
        Intent intent = new Intent(context, PayListActivity.class);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_pay_list;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        payListPresenter = new PayListPresenter(this,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_pay.setLayoutManager(manager);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.miv_close:
                final PromptDialog promptDialog = new PromptDialog(this);
                promptDialog.setSureAndCancleListener("确定要取消支付吗？", "继续支付", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promptDialog.dismiss();
                    }
                }, "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
                break;
        }
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
     * 支付列表
     *
     * @param payTypes
     */
    @Override
    public void payList(List<PayListEntity.PayTypes> payTypes) {
        PayListAdapter adapter = new PayListAdapter(this,false,payTypes);
        recy_pay.setAdapter(adapter);
    }
}
