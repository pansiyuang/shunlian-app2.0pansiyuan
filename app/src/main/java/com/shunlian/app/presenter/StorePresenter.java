package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.adapter.StoreFirstAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RegisterFinishEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IRegisterTwoView;
import com.shunlian.app.view.StoreView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/23.
 */

public class StorePresenter extends BasePresenter<StoreView> {

    public StorePresenter(Context context, StoreView iView) {
        super(context, iView);
        initApi();
    }

    @Override
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        map.put("storeId","26");
        sortAndMD5(map);

        Call<BaseEntity<StoreIndexEntity>> baseEntityCall = getApiService().storeIndex(map);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<StoreIndexEntity>>(){
            @Override
            public void onSuccess(BaseEntity<StoreIndexEntity> entity) {
                super.onSuccess(entity);
                StoreIndexEntity data = entity.data;
                if (data != null){
                    LogUtil.httpLogW("StoreIndexEntity:"+data);
                    iView.storeHeader(data.head);
                    if (data.body!=null){
                        typeOneHandle(data.body);
                    }
                }
            }
        });
    }

    public void typeTwoHandle(List<StoreIndexEntity.Body> cbodys) {
        List<StoreIndexEntity.Body> ebodys = new ArrayList<>();
        for (int e = 0; e < cbodys.size(); e++) {
            if ("2".equals(cbodys.get(e).block_module_type)) {
                StoreIndexEntity.Body ebody = new StoreIndexEntity.Body();
                ebody.name = String.valueOf(e);
                ebody.data = cbodys.get(e).data;
                ebody.title = cbodys.get(e).title;
                ebodys.add(ebody);
            }
            if (e >= cbodys.size() - 1) {
                if (ebodys.size() > 0) {
                    List<StoreIndexEntity.Body> fbodys = new ArrayList<>();
                    fbodys.addAll(cbodys);
                    int z = 0;
                    for (int f = 0; f < ebodys.size(); f++) {
                        List<StoreIndexEntity.Body> gbodys = new ArrayList<>();
                        for (int g = 0; g < ebodys.get(f).data.size(); g++) {
                            StoreIndexEntity.Body fbody = new StoreIndexEntity.Body();
                            StoreIndexEntity.Body.Datas adata = new StoreIndexEntity.Body.Datas();
                            if (g == 0) {
                                fbody.title = ebodys.get(f).title;
                            }
                            fbody.block_module_type = "2";
                            adata.id = ebodys.get(f).data.get(g).id;
                            adata.title = ebodys.get(f).data.get(g).title;
                            adata.thumb = ebodys.get(f).data.get(g).thumb;
                            adata.price = ebodys.get(f).data.get(g).price;
                            adata.type = ebodys.get(f).data.get(g).type;
                            adata.store_id = ebodys.get(f).data.get(g).store_id;
                            adata.item_id = ebodys.get(f).data.get(g).item_id;
                            adata.url = ebodys.get(f).data.get(g).url;
                            adata.whole_thumb = ebodys.get(f).data.get(g).whole_thumb;
                            fbody.ldata = adata;
                            gbodys.add(fbody);
                            if (g >= ebodys.get(f).data.size() - 1) {
                                int p = Integer.parseInt(ebodys.get(f).name) + z;
                                LogUtil.augusLogW("yxf**--" + p);
                                LogUtil.augusLogW("yxf//--" + fbodys.size());
                                fbodys.remove(p);
                                fbodys.addAll(p, gbodys);
                                z = z + gbodys.size() - 1;
                                if (f >= ebodys.size() - 1) {
                                    iView.storeFirst(fbodys);
                                }
                            }
                        }
                    }
                }else {
                    iView.storeFirst(cbodys);
                }
            }
        }
    }

    public void typeOneHandle(List<StoreIndexEntity.Body> bodies) {
        List<StoreIndexEntity.Body> abodys = new ArrayList<>();
        for (int a = 0; a < bodies.size(); a++) {
            if ("1".equals(bodies.get(a).block_module_type)) {
                StoreIndexEntity.Body abody = new StoreIndexEntity.Body();
                abody.name = String.valueOf(a);
                abody.data = bodies.get(a).data;
                abody.title = bodies.get(a).title;
                abodys.add(abody);
            }
            if (a >= bodies.size() - 1) {
                if (abodys.size() > 0) {
                    List<StoreIndexEntity.Body> cbodys = new ArrayList<>();
                    cbodys.addAll(bodies);
                    int y = 0;
                    for (int b = 0; b < abodys.size(); b++) {
                        List<StoreIndexEntity.Body> bbodys = new ArrayList<>();
                        for (int c = 0; c < abodys.get(b).data.size(); c = c + 2) {
                            StoreIndexEntity.Body bbody = new StoreIndexEntity.Body();
                            StoreIndexEntity.Body.Datas ldata = new StoreIndexEntity.Body.Datas();
                            StoreIndexEntity.Body.Datas rdata = new StoreIndexEntity.Body.Datas();
                            if (c == 0) {
                                bbody.title = abodys.get(b).title;
                            }
                            bbody.block_module_type = "1";
                            ldata.id = abodys.get(b).data.get(c).id;
                            ldata.title = abodys.get(b).data.get(c).title;
                            ldata.thumb = abodys.get(b).data.get(c).thumb;
                            ldata.price = abodys.get(b).data.get(c).price;
                            ldata.type = abodys.get(b).data.get(c).type;
                            ldata.store_id = abodys.get(b).data.get(c).store_id;
                            ldata.item_id = abodys.get(b).data.get(c).item_id;
                            ldata.url = abodys.get(b).data.get(c).url;
                            ldata.whole_thumb = abodys.get(b).data.get(c).whole_thumb;
                            if ((c + 1) < abodys.get(b).data.size()) {
                                rdata.id = abodys.get(b).data.get(c + 1).id;
                                rdata.title = abodys.get(b).data.get(c + 1).title;
                                rdata.thumb = abodys.get(b).data.get(c + 1).thumb;
                                rdata.price = abodys.get(b).data.get(c + 1).price;
                                rdata.type = abodys.get(b).data.get(c + 1).type;
                                rdata.store_id = abodys.get(b).data.get(c + 1).store_id;
                                rdata.item_id = abodys.get(b).data.get(c + 1).item_id;
                                rdata.url = abodys.get(b).data.get(c + 1).url;
                                rdata.whole_thumb = abodys.get(b).data.get(c + 1).whole_thumb;
                            }
                            bbody.ldata = ldata;
                            bbody.rdata = rdata;
                            bbodys.add(bbody);
                            if (c >= abodys.get(b).data.size() - 2) {
                                int x = Integer.parseInt(abodys.get(b).name) + y;
                                cbodys.remove(x);
                                cbodys.addAll(x, bbodys);
                                LogUtil.augusLogW("11yxf**" + x);
                                LogUtil.augusLogW("11yxf//" + cbodys.size());
                                y = y + bbodys.size() - 1;
                                if (b >= abodys.size() - 1) {
                                    typeTwoHandle(cbodys);
                                }
                            }
                        }
                    }
                } else {
                    typeTwoHandle(bodies);
                }
            }
        }
    }
    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
