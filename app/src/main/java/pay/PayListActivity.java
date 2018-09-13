package pay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.PayListAdapter;
import com.shunlian.app.bean.BuyGoodsParams;
import com.shunlian.app.bean.PayListEntity;
import com.shunlian.app.bean.PayOrderEntity;
import com.shunlian.app.presenter.PayListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.confirm_order.PLUSConfirmOrderAct;
import com.shunlian.app.ui.confirm_order.PaySuccessAct;
import com.shunlian.app.ui.more_credit.PhonePaySuccessAct;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IPayListView;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.unionpay.UPQuerySEPayInfoCallback;
import com.unionpay.UPSEInfoResp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/7.
 */

public class PayListActivity extends BaseActivity implements View.OnClickListener, IPayListView {


    public static final int FINISH_ACT_WHAT = 100;//finish act
    private static final int SDK_PAY_FLAG = 1;
    private static Activity activity;
    @BindView(R.id.miv_close)
    MyImageView miv_close;
    @BindView(R.id.recy_pay)
    RecyclerView recy_pay;
    @BindView(R.id.h5_pay)
    WebView h5_pay;
    @BindView(R.id.lLayout_pay)
    LinearLayout lLayout_pay;
    private PayListPresenter payListPresenter;
    private IWXAPI wxapi;
    //订单内id
    private String order_id;
    //外部穿过来的订单id
    private String orderId;
    private String price;
    private String shop_goods;
    private String addressId;
    private String currentPayType="";//当前支付方式
    private String pay_sn;
    private boolean isPLUS=false;
    /****plus id****/
    private String mProductId;
    private String mSkuId;
    /**
     * 充值手机号
     **/
    private String mPhoneNumber;
    /**
     * 充值金额
     **/
    private String mTopUpPrice;
    /********平台优惠券id***********/
    private String stageVoucherId;
    /*********匿名购买**********/
    private String anonymous;
    /*********使用金蛋*****************/
    private String use_egg;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UNIONPAY_FLAG:
                    String tn = "";
                    if (msg.obj == null || ((String) msg.obj).length() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PayListActivity.this);
                        builder.setTitle("错误提示");
                        builder.setMessage("网络连接失败,请重试!");
                        builder.setNegativeButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                    } else {
                        tn = (String) msg.obj;
                        /*************************************************
                         * 步骤2：通过银联工具类启动支付插件
                         ************************************************/
                        if (isUnion){
                            doStartUnionPayPlugin(PayListActivity.this, tn, "00");
                        }else {
                            doStartPhonePayPlugin(PayListActivity.this, tn, "00");
                        }
                    }
                    break;
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((String) msg.obj);
                    LogUtil.zhLogW("msg.obj===========" + msg.obj);
                    /**u4332882527
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    LogUtil.zhLogW("=resultStatus==========" + resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        paySuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        payFail();
                    }
                    break;
                case FINISH_ACT_WHAT:
                    if (activity instanceof ConfirmOrderAct ||
                            activity instanceof PLUSConfirmOrderAct) {
                        activity.finish();
                    }
                    finish();
                    break;
            }
        }
    };

//    银联支付
    private String seType="";
    private String unionUrl="";
    private  boolean isUnion ;
    private static final int UNIONPAY_FLAG = 666;
    public static final int PLUGIN_NOT_INSTALLED = -1;
    public static final int PLUGIN_NEED_UPGRADE = 2;

    /**
     * 传参使用json格式，减少字段
     *
     * @param activity
     * @param params
     */
    public static void startAct(Activity activity, String params) {
        PayListActivity.activity = activity;
        Intent intent = new Intent(activity, PayListActivity.class);
        intent.putExtra("params", params);
        activity.startActivity(intent);
    }

    /**
     * 支付失败
     */
    private void payFail() {
        Common.staticToast(getStringResouce(R.string.pay_fail));
        if (!isEmpty(mProductId)) {
            //支付失败不做任何操作
        } else if (isEmpty(order_id)) {
            MyOrderAct.startAct(PayListActivity.this, 2);
        } else if (!isEmpty(mPhoneNumber)) {
            //支付失败不做任何操作
        } else {
            OrderDetailAct.startAct(PayListActivity.this, order_id);
        }
        mHandler.sendEmptyMessageDelayed(FINISH_ACT_WHAT, 100);
    }

    /*
    支付成功
     */
    private void paySuccess() {
        Common.staticToast(getStringResouce(R.string.pay_success));
        if (!isEmpty(mPhoneNumber)) {//手机充值成功
            PhonePaySuccessAct.startAct(this, order_id);
        } else if (!isEmpty(mProductId)) {
            String plus = SharedPrefUtil.getSharedUserString("plus_role", "");
            if (isEmpty(plus) || Integer.parseInt(plus) <= 1)
                SharedPrefUtil.saveSharedUserString("plus_role", "1");
            PaySuccessAct.startAct(this, order_id, price, pay_sn, true);
        } else {
            PaySuccessAct.startAct(this, order_id, price, pay_sn, false);
        }
        mHandler.sendEmptyMessageDelayed(FINISH_ACT_WHAT, 100);
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
        setStatusBarColor(R.color.transparent);
        Intent intent = getIntent();
        //支付
        String paramsStr = intent.getStringExtra("params");
        parseParams(paramsStr);

        if (!isEmpty(mProductId)) isPLUS = true;
        UPPayAssistEx.getSEPayInfo(activity, new UPQuerySEPayInfoCallback() {
            @Override
            public void onResult(String seName, String seTypes, int cardNumbers, Bundle reserved) {
                LogUtil.augusLogW("androidPayName---"+seName);
                LogUtil.augusLogW("androidPayType---"+seTypes);
                LogUtil.augusLogW("androidPayNum---"+cardNumbers);
                seType=seTypes;
                payListPresenter = new PayListPresenter(PayListActivity.this,PayListActivity. this, isPLUS,seType);
            }

            @Override
            public void onError(String seName, String seType, String errorCode, String errorDesc) {
                LogUtil.augusLogW("androidPayName---"+seName);
                LogUtil.augusLogW("androidPayType---"+seType);
                LogUtil.augusLogW("androidPayCode---"+errorCode);
                LogUtil.augusLogW("androidPayDesc---"+errorDesc);
                payListPresenter = new PayListPresenter(PayListActivity.this,PayListActivity. this, isPLUS,"");
            }
        });
        wxapi = WXAPIFactory.createWXAPI(this, Constant.WX_APP_ID, true);
        wxapi.registerApp(Constant.WX_APP_ID);// 注册到微信列表
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_pay.setLayoutManager(manager);
    }

    /*
    解析参数
     */
    private void parseParams(String paramsStr) {
        if (!isEmpty(paramsStr)) {
            ObjectMapper om = new ObjectMapper();
            try {
                BuyGoodsParams params = om.readValue(paramsStr, BuyGoodsParams.class);
                shop_goods = params.shop_goods;
                addressId = params.addressId;
                price = params.price;
                stageVoucherId = params.stage_voucher_id;
                orderId = params.order_id;
                anonymous = params.anonymous;
                //plus支付
                mProductId = params.product_id;
                mSkuId = params.sku_id;
                //手机充值
                mPhoneNumber = params.phoneNum;
                mTopUpPrice = params.face_price;
                use_egg = params.use_egg;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miv_close:
                backSelect();
                break;
        }
    }

    private void backSelect() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setTvSureIsBold(false).setTvCancleIsBold(false)
                .setSureAndCancleListener(getStringResouce(R.string.cancel_the_pay),
                        getStringResouce(R.string.next_the_pay), (v) -> promptDialog.dismiss()
                        , getStringResouce(R.string.SelectRecommendAct_sure), (v) -> {
                            promptDialog.dismiss();
                            finish();
                        })
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (h5_pay.getVisibility() == View.VISIBLE) {
                payFail();
            } else {
                backSelect();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
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
        PayListAdapter adapter = new PayListAdapter(this, false, payTypes);
        recy_pay.setAdapter(adapter);

        adapter.setOnItemClickListener((view, position) -> {
            PayListEntity.PayTypes pay_types = payTypes.get(position);
            submitOrder(pay_types);
        });
    }


    public void unionPay(String tn) {
        Message msg = mHandler.obtainMessage();
        msg.what = UNIONPAY_FLAG;
        msg.obj = tn;
        mHandler.sendMessage(msg);
    }


    public void doStartPhonePayPlugin(Activity activity, String tn, String mode) {
        UPPayAssistEx.startSEPay(activity, null, null, tn, mode,seType);
    }
    public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
        // mMode参数解释：
        // 0 - 启动银联正式环境
        // 1 - 连接银联测试环境
        int ret = UPPayAssistEx.startPay(activity, null, null, tn, mode);
        if (PLUGIN_NEED_UPGRADE ==ret  || PLUGIN_NOT_INSTALLED ==ret ) {
            // 需要重新安装控件

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("完成购买需要安装银联支付控件，是否安装？");

            builder.setNegativeButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UPPayAssistEx.installUPPayPlugin(PayListActivity.this);
                            dialog.dismiss();
                        }
                    });

            builder.setPositiveButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str="";
        if (data.getExtras()!=null&&!isEmpty(data.getExtras().getString("pay_result")))
         str= data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {

            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            // result_data结构见c）result_data参数说明
//            if (data.hasExtra("result_data")) {
//                String result = data.getExtras().getString("result_data");
//                try {
//                    JSONObject resultJson = new JSONObject(result);
//                    String sign = resultJson.getString("sign");
//                    String dataOrg = resultJson.getString("data");
//                    // 此处的verify建议送去商户后台做验签
//                    // 如要放在手机端验，则代码必须支持更新证书
//                    boolean ret = verify(dataOrg, sign, mMode);
//                    if (ret) {
//                        // 验签成功，显示支付结果
//                        msg = "支付成功！";
//                    } else {
//                        // 验签失败
//                        msg = "支付失败！";
//                    }
//                } catch (JSONException e) {
//                }
//            }
            // 结果result_data为成功时，去商户后台查询一下再展示成功
//            msg = "支付成功！";
            payListPresenter.payVerify(unionUrl);
//            payListPresenter.payVerify("http://pay.v2.shunliandongli.com/unionpay/query?pay_sn=18091057097548690T6");
        } else if (str.equalsIgnoreCase("fail")) {
//            msg = "支付失败！";
            payFail();
        } else if (str.equalsIgnoreCase("cancel")) {
            payFail();
//            msg = "用户取消了支付";
        }

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("支付结果通知");
//        builder.setMessage(msg);
//        builder.setInverseBackgroundForced(true);
//        // builder.setCustomTitle();
//        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.create().show();
    }
    private boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;

    }


    /**
     * 支付订单
     *
     * @param entity
     */
    @Override
    public void payOrder(PayOrderEntity entity) {
        this.order_id = entity.order_id;
        pay_sn = entity.pay_sn;
        if (!isEmpty(entity.zero_pay)) {
            paySuccess();
            return;
        }
        if (currentPayType.contains("unionpay")&&entity.unionpay!=null){
            isUnion = !currentPayType.equals("unionpay_" + seType);
            unionUrl=entity.unionpay.query_url;
            unionPay(entity.unionpay.tn);
            return;
        }
        switch (currentPayType) {
            case "pay_url":
                callbackH5Pay(entity.pay_url, false);
                break;
            case "alipay":
                alipay(entity.alipay);
                break;
            case "wechat":
                break;
//            case "unionpay":
//                callbackH5Pay(entity.unionpay, true);
//                callbackH5Pay("http://pay-test.shunliandongli.com/app_jump_test.php");
//                break;
            case "credit":
                paySuccess();
                break;
        }
    }

    /**
     * 支付订单失败
     *
     * @param entity
     */
    @Override
    public void payOrderFail(PayOrderEntity entity) {
        this.order_id = entity.order_id;
        payFail();
    }

    @Override
    public void paySuccessCall() {
        paySuccess();
    }

    @Override
    public void payFailCall() {
        payFail();
    }

    /**
     * 调起银联支付
     *
     * @param unionpay
     */
    private void callbackH5Pay(final String unionpay, boolean is_unionpay) {
        if (is_unionpay) {
            visible(h5_pay);
        }
        lLayout_pay.setVisibility(View.GONE);
        final HttpDialog httpDialog = new HttpDialog(this);
        WebSettings mWebSettings = h5_pay.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        h5_pay.setWebChromeClient(new WebChromeClient());
        h5_pay.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.zhLogW("=url===" + url);
                httpDialog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.zhLogW("====h5---" + url);
                if (url.startsWith("slmall://")) {
                    if (url.contains("success")) {
                        paySuccess();
                    } else {
                        payFail();
                    }
                } else if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    final PayTask task = new PayTask(PayListActivity.this);
                    boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                        @Override
                        public void onPayResult(final H5PayResultModel result) {
                            // 支付结果返回
                            if ("9000".equals(result.getResultCode())) {
                                PayListActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        paySuccess();
                                    }
                                });
                            } else {
                                final String url = result.getReturnUrl();
                                LogUtil.augusLogW("---h5---" + url);
                                if (!TextUtils.isEmpty(url)) {
                                    PayListActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.loadUrl(url);
                                        }
                                    });
                                } else {
                                    PayListActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            payFail();
                                        }
                                    });
                                }
                            }
                        }
                    });

                    /**
                     * 判断是否成功拦截
                     * 若成功拦截，则无需继续加载该URL；否则继续加载
                     */
                    if (!isIntercepted) {
                        view.loadUrl(url, setWebviewHeader());
                    }
                    return true;
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                httpDialog.dismiss();
            }
        });
        h5_pay.loadUrl(unionpay, setWebviewHeader());
    }

    public void payCommon(String code){
        if (!isEmpty(shop_goods)) {
            payListPresenter.orderCheckout(shop_goods, addressId, stageVoucherId, anonymous, use_egg, code);
        } else if (!isEmpty(orderId)) {
            payListPresenter.fromOrderListGoPay(orderId, code);
        } else if (!isEmpty(mPhoneNumber)) {//手机充值
            payListPresenter.phoneTopUp(mPhoneNumber, mTopUpPrice, code);
        }
    }
    private void submitOrder(final PayListEntity.PayTypes pay_types) {
        if (pay_types!=null&&!isEmpty(pay_types.code)){
            currentPayType = pay_types.code;
        }else {
            Common.staticToast("参数有误，请稍后再试...");
            return;
        }
        if (currentPayType.contains("unionpay")){
            payCommon(pay_types.code);
            return;
        }
        switch (pay_types.code) {
            case "pay_url":
                payListPresenter.submitPLUSOrder(mProductId, mSkuId, addressId, pay_types.code);
                break;
            case "alipay":
                payCommon(pay_types.code);
//                else if (!isEmpty(mProductId)){
//                    payListPresenter.submitPLUSOrder(mProductId,mSkuId,addressId,pay_types.code);
//                }
                break;
            case "wechat":
                break;
            case "credit":
                final PromptDialog promptDialog = new PromptDialog(this);
                promptDialog.setTvSureIsBold(false).setTvCancleIsBold(false)
                        .setSureAndCancleListener("确认用余额支付吗？\n￥" + price,
                                getString(R.string.SelectRecommendAct_sure), (v) -> {
                                    if (!isEmpty(shop_goods)) {
                                        payListPresenter.orderCheckout(shop_goods,
                                                addressId, stageVoucherId, anonymous, use_egg, pay_types.code);
                                    } else if (!isEmpty(orderId)) {
                                        payListPresenter.fromOrderListGoPay(orderId,
                                                pay_types.code);
                                    } else if (!isEmpty(mPhoneNumber)) {//手机充值
                                        payListPresenter.phoneTopUp(mPhoneNumber, mTopUpPrice, pay_types.code);
                                    }
                                    promptDialog.dismiss();
                                }, getString(R.string.errcode_cancel), (v) -> promptDialog.dismiss()
                        ).show();
                break;
        }
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     *
     * @param alipayRequest
     */
    public void alipay(final String alipayRequest) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayListActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(alipayRequest, true);
                LogUtil.zhLogW("result============:" + result);
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
