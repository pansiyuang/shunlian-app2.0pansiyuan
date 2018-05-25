package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;

import com.shunlian.app.adapter.SysMsgAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.SystemMsgEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
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

        Call<BaseEntity<SystemMsgEntity>> sysmessage = getApiService().sysmessage(map);
        getNetData(empty, empty, isShow, sysmessage,
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

            adapter.setOnItemClickListener((v, position) -> {
                SystemMsgEntity.MsgType msgType = msgTypes.get(position);
                handlerType(msgType);
            });
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage, allPage);
    }

    private void handlerType(SystemMsgEntity.MsgType msgType) {
        SystemMsgEntity.ContentBean body = msgType.body;
        switch (isEmpty(msgType.jump) ? "0" : msgType.jump) {
            case "1":
                CouponListAct.startAct(context);
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
//                Common.goGoGo();
                break;
            case "10":
                Common.goGoGo(context, body.target, body.targetId);
                break;
            case "11":
                DayDayAct.startAct(context);
                break;
        }
        if ("0".equals(msgType.is_read))
            msgRead(msgType.type,msgType.id);
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
                paging(false, 0);
            }
        }
    }
}
