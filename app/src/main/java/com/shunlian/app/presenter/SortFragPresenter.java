package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.SortFragEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISortFragView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/6.
 */

public class SortFragPresenter extends BasePresenter<ISortFragView> {

    private List<SortFragEntity.ItemList> subAllItemLists = new ArrayList<>();

    public SortFragPresenter(Context context, ISortFragView iView) {
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
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<SortFragEntity>> baseEntityCall = getApiService().categoryToplist(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<SortFragEntity>>(){
            @Override
            public void onSuccess(BaseEntity<SortFragEntity> entity) {
                super.onSuccess(entity);
                iView.toplist(entity.data.category_list);
                List<SortFragEntity.Toplist> category_list = entity.data.category_list;
                if (category_list != null && category_list.size() > 0){
                    String id = category_list.get(0).id;
                    categorySubList(id);
                }
            }
        });
    }

    /**
     * 分类子目录
     * @param top_id
     */
    public void categorySubList(String top_id){

        Map<String,String> map = new HashMap<>();
        map.put("top_id",top_id);
        sortAndMD5(map);

        Call<BaseEntity<SortFragEntity>> baseEntityCall = getApiService().categorySubList(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<SortFragEntity>>(){
            @Override
            public void onSuccess(BaseEntity<SortFragEntity> entity) {
                super.onSuccess(entity);
                List<SortFragEntity.SubList> sub_list = entity.data.sub_list;
                subAllItemLists.clear();
                if (sub_list != null && sub_list.size() > 0){
                    for(SortFragEntity.SubList subList : sub_list){
                        List<SortFragEntity.ItemList> item_list = subList.item_list;
                        subAllItemLists.addAll(item_list);
                    }
                }
                iView.subRightList(sub_list,subAllItemLists);
            }
        });
    }
}
