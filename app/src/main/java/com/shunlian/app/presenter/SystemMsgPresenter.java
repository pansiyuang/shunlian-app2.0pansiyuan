package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.SysMsgAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.SystemMsgEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
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
                        allPage = Integer.parseInt(data.total);
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
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage, allPage);
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
