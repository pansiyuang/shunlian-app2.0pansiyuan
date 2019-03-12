package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IVerifyBlogView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class VerifyBlogPresenter extends BasePresenter<IVerifyBlogView> {
    public static final int PAGE_SIZE = 20;

    public VerifyBlogPresenter(Context context, IVerifyBlogView iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {
    }

    public void initPage() {
        currentPage = 1;
    }

    public void getHotBlogList(boolean isFirst) {
        Map<String, String> map = new HashMap<>();
        map.put("status", "0");
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<HotBlogsEntity>> baseEntityCall = getApiService().hotblogs(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HotBlogsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HotBlogsEntity> entity) {
                super.onSuccess(entity);
                HotBlogsEntity hotBlogsEntity = entity.data;
                isLoading = false;
                iView.getBlogList(hotBlogsEntity, hotBlogsEntity.pager.page, hotBlogsEntity.pager.total_page, hotBlogsEntity.pager.count);
                currentPage = hotBlogsEntity.pager.page;
                allPage = hotBlogsEntity.pager.total_page;
                if (currentPage == 1) {
                    iView.refreshFinish();
                }
                currentPage++;
            }

            @Override
            public void onFailure() {
                isLoading = false;
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                isLoading = false;
                super.onErrorCode(code, message);
            }
        });
    }

    public void verifyBlog(String blogIds, String type, String isHot, String remark, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("id", blogIds);
        map.put("type", type);
        if (!isEmpty(isHot)) {
            map.put("hot", "1");
        }
        if (!isEmpty(remark)) {
            map.put("remark", remark);
        }
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().verifyBlog(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(context, entity.message);
                if (position != -1) {
                    if ("PASS".equals(type)) {
                        iView.blogVerifyPass(position, entity.data.count);
                    } else {
                        iView.blogVerifyFail(position, entity.data.count);
                    }
                } else {
                    if ("PASS".equals(type)) {
                        iView.blogVerifyPass(entity.data.count);
                    } else {
                        iView.blogVerifyFail(entity.data.count);
                    }
                }
            }
        });
    }

    //1关注，2取消关注
    public void focusUser(int type, String memberId, int position) {
        Map<String, String> map = new HashMap<>();
        if (type == 0) {
            map.put("type", "1");
        } else {
            map.put("type", "2");
        }
        map.put("member_id", memberId);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().focusUser(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.focusUser(type, position);
                Common.staticToast(entity.message);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void withDrawBlog(String blogId, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("id", blogId);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().retractBlog(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.blogWithDraw(position,entity.data.count);
            }
        });
    }


    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getHotBlogList(false);
            }
        }
    }
}
