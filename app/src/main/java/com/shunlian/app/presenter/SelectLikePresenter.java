package com.shunlian.app.presenter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PersonalDataEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISelectLikeView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/24.
 */

public class SelectLikePresenter extends BasePresenter<ISelectLikeView> {

    private final int maxCount = 100000000;//最多可选标签
    private int topMargin;
    public int currentCount = 0;//当前选择数量
    private ArrayList<PersonalDataEntity.TagList> mTagLists;
    private final StringBuilder sb_tag;

    public SelectLikePresenter(Context context, ISelectLikeView iView) {
        super(context, iView);
        topMargin = TransformUtil.dip2px(context, 15);
        sb_tag = new StringBuilder();
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

        Call<BaseEntity<PersonalDataEntity>> baseEntityCall = getApiService().gettaglist(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<PersonalDataEntity>>(){
            @Override
            public void onSuccess(BaseEntity<PersonalDataEntity> entity) {
                super.onSuccess(entity);
                mTagLists = entity.data.list;
                if (!isEmpty(mTagLists)){
                    selectTag();
                }
                setAdapter(mTagLists);
            }
        });
    }

    private void setAdapter(ArrayList<PersonalDataEntity.TagList> mTagLists) {
        int w = TransformUtil.dip2px(context, 0.5f);
        SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter<PersonalDataEntity.TagList>
                (context, R.layout.text_simple,mTagLists) {
            @Override
            public void convert(SimpleViewHolder holder,
                                PersonalDataEntity.TagList tagList, int position) {
                holder.addOnClickListener(R.id.mtv_simple);
                MyTextView mtv_text = holder.getView(R.id.mtv_simple);
                mtv_text.setText(tagList.name);
                mtv_text.setGravity(Gravity.CENTER);
                mtv_text.setMaxLines(1);
                mtv_text.setWHProportion(200,70);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)
                        mtv_text.getLayoutParams();
                layoutParams.topMargin = topMargin;

                GradientDrawable background = (GradientDrawable) mtv_text.getBackground();
                if ("1".equals(tagList.active)){//选中
                    background.setColor(getColorResouce(R.color.category_reset));
                    background.setStroke(w,getColorResouce(R.color.category_reset));
                    mtv_text.setTextColor(getColorResouce(R.color.pink_color));
                }else {
                    background.setColor(getColorResouce(R.color.white));
                    background.setStroke(w,getColorResouce(R.color.light_gray_three));
                    mtv_text.setTextColor(getColorResouce(R.color.new_text));
                }
            }
        };
        iView.setAdapter(adapter);
        adapter.setOnItemClickListener((v,p)->{
            PersonalDataEntity.TagList tagList = mTagLists.get(p);
            if ("1".equals(tagList.active)){
                tagList.active = "0";
            }else {
                if (currentCount >= maxCount){
                    Common.staticToast("最多选择"+maxCount+"个");
                }else {
                    tagList.active = "1";
                }
            }
            adapter.notifyDataSetChanged();
            selectTag();
        });
    }

    public String[] getCount(){
        if (currentCount <= 0){
            Common.staticToast("至少选择一个");
            return null;
        }
        String[] str = new String[2];
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < mTagLists.size(); i++) {
            PersonalDataEntity.TagList tagList = mTagLists.get(i);
            if ("1".equals(tagList.active)){
                sb1.append(tagList.name+"、");
                sb2.append(tagList.id+",");
            }
        }
        str[0] = sb1.substring(0,sb1.length()-1);//名字
        str[1] = sb2.substring(0,sb2.length()-1);//id
        return str;
    }

    private void selectTag(){
        sb_tag.delete(0,sb_tag.length());
        currentCount = 0;
        for (int i = 0; i < mTagLists.size(); i++) {
            PersonalDataEntity.TagList tagList = mTagLists.get(i);
            if ("1".equals(tagList.active)){
                sb_tag.append(tagList.name);
                sb_tag.append("/");
                currentCount++;
            }
        }
        if (sb_tag.length() > 0) {
            String s = sb_tag.substring(0, sb_tag.length() - 1);
            iView.setTextTag(s,s.split("/").length);
        }else {
            iView.setTextTag("请选择",0);
        }
    }
}
