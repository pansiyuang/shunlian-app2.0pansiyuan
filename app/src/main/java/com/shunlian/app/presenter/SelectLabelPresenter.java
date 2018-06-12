package com.shunlian.app.presenter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.ArtTagEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISelectLabelView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/23.
 */

public class SelectLabelPresenter extends BasePresenter<ISelectLabelView> {

    private final int w;
    private List<ArtTagEntity.Item> list;
    private int selCount= 0;//选择标签数量
    private List<String> tag;
    public SelectLabelPresenter(Context context, ISelectLabelView iView) {
        super(context, iView);
        initApi();
        w = TransformUtil.dip2px(context, 0.5f);
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
        Call<BaseEntity<ArtTagEntity>> baseEntityCall = getAddCookieApiService()
                .portraitArtTag(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<ArtTagEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ArtTagEntity> entity) {
                super.onSuccess(entity);
                list = entity.data.list;
                setAdapter();
            }
        });

    }

    private void setAdapter() {
        SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter<ArtTagEntity.Item>
                (context,R.layout.comment_class,list) {

            @Override
            public void convert(SimpleViewHolder holder, ArtTagEntity.Item item, int position) {
                holder.addOnClickListener(R.id.mtv_text);
                MyTextView mtv_text = holder.getView(R.id.mtv_text);
                mtv_text.setText(item.name);
                mtv_text.setWHProportion(154,77);
                mtv_text.setGravity(Gravity.CENTER);
                GradientDrawable background = (GradientDrawable) mtv_text.getBackground();
                if (item.isSelect){
                    background.setColor(getColorResouce(R.color.value_FEF2F4));
                    mtv_text.setTextColor(getColorResouce(R.color.pink_color));
                    background.setStroke(w,getColorResouce(R.color.value_FEF2F4));
                }else {
                    background.setColor(getColorResouce(R.color.white));
                    background.setStroke(w,getColorResouce(R.color.value_E5E5E5));
                    mtv_text.setTextColor(getColorResouce(R.color.new_text));
                }
            }
        };
        iView.setAdapter(adapter);
        adapter.setOnItemClickListener((v,position)->{
            ArtTagEntity.Item item = list.get(position);
            item.isSelect = !item.isSelect;
            adapter.notifyDataSetChanged();
        });
    }

    private String iteratorList(int label){
        if (!isEmpty(list)){
            StringBuilder sb = new StringBuilder();
            tag = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                ArtTagEntity.Item item = list.get(i);
                if (item.isSelect){
                    selCount++;
                    sb.append(item.id);
                    sb.append(",");
                    tag.add(item.name);
                }
            }
            if (sb.length() == 0){
                return "";
            }

            if (label == 1){
                tag.add("男");
            }else if (label == 2){
                tag.add("女");
            }
            return sb.substring(0,sb.length()-1);
        }
        return "";
    }

    public void submit(int label) {
        String s = iteratorList(label);
        if (selCount < 3){
            Common.staticToast("标签至少选择3个");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("tag",s);
        map.put("sex",label+"");
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .addPortrait(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                SharedPrefUtil.saveSharedPrfStringss("tags", new HashSet<>(tag));
                JpushUtil.setJPushAlias();
                iView.success();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.failure();
            }
        });

    }
}
