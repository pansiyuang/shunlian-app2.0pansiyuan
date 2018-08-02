package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.adapter.SysMsgAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.SystemMsgEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.ui.CouponMsgAct;
import com.shunlian.app.ui.activity.DayDayAct;
import com.shunlian.app.ui.confirm_order.OrderLogisticsActivity;
import com.shunlian.app.ui.coupon.CouponListAct;
import com.shunlian.app.ui.discover.other.CommentListAct;
import com.shunlian.app.ui.discover.other.ExperienceDetailAct;
import com.shunlian.app.ui.message.PunishAct;
import com.shunlian.app.ui.order.ExchangeDetailAct;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ISystemMsgView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/9.
 */

public class SystemMsgPresenter extends BasePresenter<ISystemMsgView> {
    public final String page_size = "10";
    private List<SystemMsgEntity.MsgType> msgTypes = new ArrayList<>();
    private SysMsgAdapter adapter;
    private Call<BaseEntity<SystemMsgEntity>> sysmessageCall;

    public SystemMsgPresenter(Context context, ISystemMsgView iView) {
        super(context, iView);
        initApi();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {
        if (sysmessageCall != null)sysmessageCall.cancel();
        if (msgTypes != null){
            msgTypes.clear();
            msgTypes = null;
        }
        if (adapter != null){
            adapter.unbind();
            adapter = null;
        }
    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        currentPage = 1;
        isLoading = false;
        allPage = 1;
        paging(true, 0);
    }

    public void paging(boolean isShow, int empty) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", page_size);
        sortAndMD5(map);

        sysmessageCall = getApiService().sysmessage(map);
        getNetData(empty, empty, isShow, sysmessageCall,
                new SimpleNetDataCallback<BaseEntity<SystemMsgEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<SystemMsgEntity> entity) {
                        super.onSuccess(entity);
                        isLoading = false;
                        SystemMsgEntity data = entity.data;
                        currentPage = Integer.parseInt(data.page);
                        allPage = Integer.parseInt(data.total_page);
                        msgTypes.addAll(data.list);
                        showData();
                        currentPage++;
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        isLoading = false;
                    }

                    @Override
                    public void onErrorCode(int code, String message) {
                        super.onErrorCode(code, message);
                        isLoading = false;
                    }
                });
    }

    private void showData() {
        if (adapter == null) {
            adapter = new SysMsgAdapter(context, msgTypes);
            iView.setAdapter(adapter);
            adapter.setOnReloadListener(() -> onRefresh());
            adapter.setOnItemClickListener((v, position) -> {
                SystemMsgEntity.MsgType msgType = msgTypes.get(position);
                handlerType(msgType,position);
            });
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage, allPage);
        if (!isEmpty(msgTypes)){
            iView.showDataEmptyView(100);
        }else {
            iView.showDataEmptyView(0);
        }
    }

    private void handlerType(SystemMsgEntity.MsgType msgType, int position) {
        SystemMsgEntity.ContentBean body = msgType.body;
        switch (isEmpty(msgType.jump) ? "0" : msgType.jump) {
            case "1":
                if ("1001".equals(body.opt))
                    CouponListAct.startAct(context);
                else if ("1002".equals(body.opt))
                    CouponMsgAct.startAct(context,body.id);
                break;
            case "2":
                OrderDetailAct.startAct(context, body.id);
                break;
            case "4":
                OrderLogisticsActivity.startAct(context, body.id);
                break;
            case "5":
                ExchangeDetailAct.startAct(context, body.id);
                break;
            case "6":
                PunishAct.startAct(context,body.id,body.opt);
                break;
            case "7":
                ExperienceDetailAct.startAct(context, body.id);
                break;
            case "8":
                CommentListAct.startAct((Activity) context, body.id);
                break;
            case "9":
                Common.goGoGo(context,"special",body.id);
                break;
            case "10":
                Common.goGoGo(context, body.target, body.targetId);
                break;
            case "11":
                DayDayAct.startAct(context);
                break;
            case "15":
                analysisUrl(body.url);
                break;
        }
        if ("0".equals(msgType.is_read)) {
            msgRead(msgType.type, msgType.id);
            msgType.is_read = "1";
            adapter.notifyItemChanged(position);
        }
    }

    private void analysisUrl(String url) {
        if (url.startsWith("slmall://")) {
            String type = interceptBody(url);
            if (!TextUtils.isEmpty(type)) {
                String id = "";
                String id1 = "";
                if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id")))
                    id = interceptId(url);
                if (!TextUtils.isEmpty(Common.getURLParameterValue(url, "id1")))
                    id1 = interceptId(url);
                Common.goGoGo(context, type, id, id1);
            }
        }
    }

    private String interceptId(String url) {
        String[] split = url.split("\\?");
        String s = split[1];
        String[] split1 = s.split("=");
        String s1 = split1[1];
        return s1;
    }

    private String interceptBody(String url) {
        String[] split = url.split("\\?");
        String s = split[0];
        if (!TextUtils.isEmpty(s)) {
            String[] split1 = s.split("//");
            if (!TextUtils.isEmpty(split1[1])) {
                return split1[1];
            }
        }
        return null;
    }


    private void msgRead(String type,String msg_id){
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("msg_id",msg_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .messageRead(getRequestBody(map));
        getNetData(false,baseEntityCall,new SimpleNetDataCallback<>());
    }
    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                paging(false, 100);
            }
        }
    }
}
