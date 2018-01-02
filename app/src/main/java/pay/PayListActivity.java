package pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.PayListAdapter;
import com.shunlian.app.bean.PayListEntity;
import com.shunlian.app.presenter.PayListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IPayListView;
import com.shunlian.app.widget.MyImageView;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/7.
 */

public class PayListActivity extends BaseActivity implements View.OnClickListener,IPayListView {


    private static Activity activity;
    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.recy_pay)
    RecyclerView recy_pay;
    private PayListPresenter payListPresenter;
    private IWXAPI wxapi;
    private static final int SDK_PAY_FLAG = 1;
    private String payRequest = "";
    private String order_id;
    private String shop_goods;
    private String addressId;

    public static void startAct(Activity activity, String shop_goods, String addressId){
        PayListActivity.activity = activity;
        Intent intent = new Intent(activity, PayListActivity.class);
        intent.putExtra("shop_goods",shop_goods);
        intent.putExtra("addressId",addressId);
        activity.startActivity(intent);
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
//                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    PayResult payResult = new PayResult((String) msg.obj);
                    LogUtil.zhLogW("msg.obj==========="+ msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    LogUtil.zhLogW("=resultInfo=========="+resultInfo);
                    String resultStatus = payResult.getResultStatus();
                    LogUtil.zhLogW("=resultStatus=========="+resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Common.staticToast("支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Common.staticToast("支付失败");
                        if (activity instanceof ConfirmOrderAct){
                            activity.finish();
                        }
                        finish();
                        if (isEmpty(order_id)){
                            MyOrderAct.startAct(PayListActivity.this, 2);
                        }else {
                            OrderDetailAct.startAct(PayListActivity.this, order_id);
                        }
                    }
                    break;
                }
            }
        };
    };
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

        shop_goods = getIntent().getStringExtra("shop_goods");
        addressId = getIntent().getStringExtra("addressId");

        wxapi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        wxapi.registerApp(Constant.WX_APP_ID);// 注册到微信列表
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
    public void payList(final List<PayListEntity.PayTypes> payTypes) {
        PayListAdapter adapter = new PayListAdapter(this,false,payTypes);
        recy_pay.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PayListEntity.PayTypes pay_types = payTypes.get(position);
                submitOrder(pay_types);
            }
        });
    }

    /**
     * 支付订单
     *
     * @param alipay
     * @param order_id
     */
    @Override
    public void payOrder(String alipay, String order_id) {
        payRequest = alipay;
        this.order_id = order_id;
        alipay();
    }

    private void submitOrder(PayListEntity.PayTypes pay_types) {
        switch (pay_types.code){
            case "alipay":
                payListPresenter.orderCheckout(shop_goods,addressId,pay_types.code);
                break;
            case "wechat":
                break;
            case "unionpay":
                break;
            case "credit":
                break;
        }
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void alipay() {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayListActivity.this);
                // 调用支付接口，获取支付结果
//                Map<String, String> result = alipay.payV2(payRequest, true);
                String result = alipay.pay(payRequest, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
