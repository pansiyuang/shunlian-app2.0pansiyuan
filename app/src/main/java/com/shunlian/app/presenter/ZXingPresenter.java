package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ScanCodeEntity;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IZXingView;
import com.shunlian.app.widget.TipDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/6/15.
 */

public class ZXingPresenter extends BasePresenter<IZXingView>{

    private TipDialog tipDialog;

    public ZXingPresenter(Context context, IZXingView iView) {
        super(context, iView);
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

    }

    public void parseUrl(String url){
        Map<String,String> map = new HashMap<>();
        map.put("word",url);
        sortAndMD5(map);

        Call<BaseEntity<ScanCodeEntity>>
                baseEntityCall = getAddCookieApiService().scanCodeParseUrl(getRequestBody(map));

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<ScanCodeEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ScanCodeEntity> entity) {
                super.onSuccess(entity);
                ScanCodeEntity data = entity.data;
                SharedPrefUtil.saveSharedUserString("share_code", data.share_code);
                ScanCodeEntity.Url mUrl = data.url;

                DispachJump dispachJump = new DispachJump();

                if (!isEmpty(mUrl.item_id_list)){
                    String[] temp = new String[mUrl.item_id_list.size()];
                    Common.goGoGo(context,mUrl.type,mUrl.item_id_list.toArray(temp));

                    dispachJump.items = temp;
                }else {
                    Common.goGoGo(context,mUrl.type);
                }
                dispachJump.jumpType = mUrl.type;
                EventBus.getDefault().postSticky(dispachJump);
                ((Activity) context).finish();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                failureTip();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                failureTip();
            }
        });
    }

    private void failureTip(){
        if (tipDialog == null)
            tipDialog = new TipDialog((Activity) context);
        tipDialog.setSureListener("暂不支持识别非顺联动力官方二维码",
                "确定", v -> tipDialog.dismiss()).show();
    }
}
