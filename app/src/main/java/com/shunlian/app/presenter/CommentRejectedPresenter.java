package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommentRejectedEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.eventbus_bean.RejectedNotifyEvent;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ICommentRejectedView;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2019/1/23.
 */

public class CommentRejectedPresenter extends BasePresenter<ICommentRejectedView> {
    private List<CommentRejectedEntity.RejectedList> mList;

    public CommentRejectedPresenter(Context context, ICommentRejectedView iView) {
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
        Call<BaseEntity<CommentRejectedEntity>>
                baseEntityCall = getApiService().checkSetList(map);

        getNetData(true,baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<CommentRejectedEntity>>(){

                    @Override
            public void onSuccess(BaseEntity<CommentRejectedEntity> entity) {
                super.onSuccess(entity);
                CommentRejectedEntity data = entity.data;
                if (data != null && !isEmpty(data.list)){
                    mList = data.list;
                }
                SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter
                        <CommentRejectedEntity.RejectedList>(context,
                        R.layout.text_line_simpe,mList) {

                    @Override
                    public void convert(SimpleViewHolder holder,
                                        CommentRejectedEntity.RejectedList rejectedList,
                                        int position) {
                        holder.getView(R.id.ll_root).setBackgroundColor(Color.WHITE);

                        MyTextView mtv_simple = holder.getView(R.id.mtv_content);
                        mtv_simple.setTextSize(15);
                        mtv_simple.setTextColor(getColorResouce(R.color.value_343434));
                        int left = TransformUtil.dip2px(context, 16);
                        int top = TransformUtil.dip2px(context, 15);
                        mtv_simple.setPadding(left,top,0,top);
                        mtv_simple.setText(rejectedList.value);
                        mtv_simple.setSingleLine();
                        mtv_simple.setEllipsize(TextUtils.TruncateAt.END);

                        View view = holder.getView(R.id.line);
                        LinearLayout.LayoutParams layoutParams =
                                (LinearLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = TransformUtil.dip2px(context,12);
                        view.setLayoutParams(layoutParams);

                        holder.addOnClickListener(R.id.ll_root);
                    }
                };

                iView.setAdapter(adapter);

                adapter.setOnItemClickListener((view, position) -> {
                    CommentRejectedEntity.RejectedList rejectedList = mList.get(position);
                    iView.selectRejectedContent(rejectedList);
                });
            }
        });
    }



    public void commentCheck(String comment_id,String remark){
        Map<String,String> map = new HashMap<>();
        map.put("comment_id",comment_id);
        map.put("check_status","2");
        map.put("remark",remark);

        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().commentCheck(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                EventBus.getDefault().post(new RejectedNotifyEvent(true, entity.data.comment_id, entity.data.reply_parent_comment_id));
                ((Activity) context).finish();
            }
        });
    }
}
