package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.shunlian.app.adapter.TopicAdaper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PagerEntity;
import com.shunlian.app.bean.TopicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/10/18.
 */

public class AddTopicPresenter extends BasePresenter {

    public final String page_size = "20";
    private List<TopicEntity.ItemBean> itemBeans = new ArrayList<>();
    private TopicAdaper adaper;
    public String key_word;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TopicEntity.ItemBean itemBean = itemBeans.get(msg.what);
            Intent intent = new Intent();
            intent.putExtra("title", itemBean.title);
            intent.putExtra("id", itemBean.id);
            ((Activity) context).setResult(Activity.RESULT_OK, intent);
            ((Activity) context).finish();
        }
    };


    public AddTopicPresenter(Context context, IView iView) {
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
        if (itemBeans != null) {
            itemBeans.clear();
            itemBeans = null;
        }

        if (adaper != null) {
            adaper.unbind();
            adaper = null;
        }

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        key_word = null;
        currentPage = 1;
        allPage = 1;
    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        currentPage = 1;
        allPage = 1;
        itemBeans.clear();
        requestData(true);
    }

    public void requestData(boolean isLoad) {
        Map<String, String> map = new HashMap<>();
        if (!isEmpty(key_word))
            map.put("key_word", key_word);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", page_size);
        sortAndMD5(map);

        Call<BaseEntity<TopicEntity>> activitys = getApiService().getTopics(map);
        getNetData(isLoad, activitys, new SimpleNetDataCallback<BaseEntity<TopicEntity>>() {
            @Override
            public void onSuccess(BaseEntity<TopicEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                TopicEntity data = entity.data;
                PagerEntity pager = data.pager;
                currentPage = Integer.parseInt(pager.page);
                allPage = Integer.parseInt(pager.total_page);
                setData(data.list);
                currentPage++;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
            }
        });
    }

    private void setData(List<TopicEntity.ItemBean> list) {
        if (!isEmpty(list))
            itemBeans.addAll(list);

        if (adaper == null) {
            adaper = new TopicAdaper(context, itemBeans);
            if (iView != null) {
                iView.setAdapter(adaper);
            }

            adaper.setOnItemClickListener((view, position) -> {
                adaper.item_id = position;
                adaper.notifyDataSetChanged();
                mHandler.sendEmptyMessageDelayed(position, 400);
            });
        } else {
            adaper.notifyDataSetChanged();
        }
        adaper.setPageLoading(currentPage, allPage);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                requestData(false);
            }
        }
    }
}
