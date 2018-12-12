package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMemberPageView;
import com.shunlian.app.view.INewUserPageView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/11/5.
 */

public class MemberPagePresenter extends BasePresenter<IMemberPageView> {
    protected int currentPage = 1;//当前页
    protected String  member_code;
    public MemberPagePresenter(Context context, IMemberPageView iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {
        currentPage = 1;
        allPage = 1;
    }

    @Override
    protected void initApi() {
        currentPage = 1;
        allPage = 1;
    }

    public void initApiMemberKey(String member_code) {
        currentPage = 1;
        allPage = 1;
        this.member_code = member_code;
        memberListInfo(true);

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (currentPage <= allPage) {
            memberListInfo(false);
        }
    }

    public void memberListInfo(boolean isLoad) {
        Map<String, String> map = new HashMap<>();
        if(!TextUtils.isEmpty(member_code)){
            map.put("member_code",member_code);
        }
        map.put("page",currentPage+"");
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<MemberInfoEntity>> setinfo = getAddCookieApiService().followerList(requestBody);
        getNetData(isLoad, setinfo, new SimpleNetDataCallback<BaseEntity<MemberInfoEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MemberInfoEntity> entity) {
                super.onSuccess(entity);
                if(entity.data!=null&&entity.data.pager!=null) {
                    currentPage = Integer.valueOf(entity.data.pager.page);
                    allPage = Integer.parseInt(entity.data.pager.total_page);
                    if(currentPage==1){
                        iView.memberDetail(entity.data,entity.data.pager.count);
                    }
                    iView.memberListInfo(entity.data.list,currentPage);
                    if(entity.data.list!=null&&entity.data.list.size()>0) {
                        currentPage++;
                    }
                }
            }
        });
    }

    public void setInfo(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> setinfo = getAddCookieApiService().setinfo(getRequestBody(map));
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                if ("weixin".equals(key)){
                    iView.setWeixin(value);
                }
                Common.staticToast(entity.message);
            }
        });

    }

}
